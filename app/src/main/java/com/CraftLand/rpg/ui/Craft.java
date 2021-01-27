package com.craftland.rpg.ui;

import java.util.ArrayList;
import java.util.List;

import com.craftland.engine.parser.CsvReader;
import com.craftland.rpg.Resource;

public class Craft
{
    private CsvReader csvReader;
    private Inventory inventory;
    public List<Item> pots;

    public Craft(Inventory inventory)
    {
        this.inventory = inventory;
        pots = new ArrayList<>();
    }

    public void generateCraftingPots()
    {
        csvReader = new CsvReader(Resource.assets_Csv + "craft.csv");
        for(int index = 0; index < inventory.getSize(); index++) {
            Item itemA = inventory.items.get(index);
            if (itemA != null) {
                //compare inventory items
                String combine[] = csvReader.selectAll(itemA.getName(), "combine");
                String result[] = csvReader.selectAll(itemA.getName(), "result");
                int qt1[] = csvReader.selectAll_int(itemA.getName(), "qt1");
                int qt2[] = csvReader.selectAll_int(itemA.getName(), "qt2");
                for (int n = 0; n < combine.length; n++) {
                    Item itemB = inventory.getItem(combine[n]);
                    if (itemB != null && n < result.length &&
                            !isPotOnList(result[n]))
                    {
                        if(itemA.counter >= qt1[n] &&
                                itemB.counter >= qt2[n]){
                            pots.add(new Item(result[n]));
                        }
                    }
                }
            }
        }
        csvReader.close();
    }

    public boolean build(String resultItem, Inventory inventory) {
        if(!inventory.isFull()) {
            csvReader = new CsvReader(Resource.assets_Csv + "craft.csv");
            String results[] = csvReader.select("result");
            for (int index = 0; index < results.length; index++) {
                if (results[index].equals(resultItem)) {
                    String row[] = csvReader.select(index + 1); // jump one line below due to csv header
                    String itemA = row[0];
                    int qt1 = Integer.parseInt(row[1]);
                    String itemB = row[2];
                    int qt2 = Integer.parseInt(row[3]);
                    if (itemA.equals(itemB)) {
                        inventory.removeItem(itemA, qt1);
                    } else {
                        inventory.removeItem(itemA, qt1);
                        inventory.removeItem(itemB, qt2);
                    }
                    break;
                }
            }
            if (!resultItem.startsWith("Ammo")) {
                inventory.addItem(resultItem, 1);
            } else {
                inventory.addItem(resultItem, 25);
            }
            csvReader.close();
            return true;
        }else
        {
            return false;
        }
    }

    private boolean isPotOnList(String name)
    {
        for(int n = 0; n < pots.size(); n++){
            if(pots.get(n).getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public void clean()
    {
        if(!pots.isEmpty()){
            pots.clear();
        }
    }
}
