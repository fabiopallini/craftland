package com.craftland.rpg.ui;

import java.util.ArrayList;
import java.util.List;
import com.craftland.rpg.Resource;
import com.craftland.rpg.core.Game;

public class Inventory
{
    public List<Item> items = new ArrayList<>();
    private final int maxSize = 60;

    public Inventory(List<String> data)
    {
        if(data != null) {
            for (int n = 0; n < data.size(); n++) {
                _addItem(data.get(n));
            }
        }
    }

    public void useItem(String name){
        for(int n = 0; n < items.size(); n++){
            if(items.get(n).getName().equals(name)){
                if(items.get(n).counter > 1){
                    items.get(n).counter--;
                }
                else
                {
                    if(Game.menu.getItemB() != null) {
                        if (items.get(n).getName().equals(Game.menu.getItemB().getName())) {
                            Game.menu.setItemB(null);
                        }
                    }
                    items.remove(n);
                }
                break;
            }
        }
    }

    public void addItem(String name, int quantity){
        for(int n = 0; n < quantity; n++){
            _addItem(name);
        }
    }

    public void removeItem(String name, int quantity)
    {
        if(!items.isEmpty() && !name.equals(Resource.Pickaxe)) {
            for (int n = 0; n < items.size(); n++) {
                if (name.equals(items.get(n).getName())) {
                    items.get(n).counter -= quantity;
                    if(items.get(n).counter <= 0){
                        items.remove(n);
                    }
                    break;
                }
            }
        }
    }

    public void removeItem_All(String name){
        if(!items.isEmpty() && !name.equals(Resource.Pickaxe)) {
            for (int n = 0; n < items.size(); n++) {
                if (name.equals(items.get(n).getName())) {
                    items.remove(n);
                    break;
                }
            }
        }
    }

    public Item getItem(String name){
        Item i = null;
        for(int n = 0; n < items.size(); n++){
            if(items.get(n).getName().equals(name)){
                i = items.get(n);
                break;
            }
        }
        return i;
    }

    public int getSize()
    {
        return items.size();
    }

    public boolean isFull()
    {
        if(items.size() < maxSize){
            return false;
        }
        return true;
    }

    public boolean isFull(String name)
    {
        boolean result = false;
        if(isFull())
        {
            result = true;
            for(int n = 0; n < items.size(); n++)
            {
                if(items.get(n).getName().equals(name) && items.get(n).counter < 99){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    private void _addItem(String name)
    {
        boolean add = false;
        if(!isFull(name)) {
            for(int n = 0; n < items.size(); n++){
                if(items.get(n).getName().equals(name) && items.get(n).counter < 99){
                    items.get(n).counter++;
                    add = true;
                    break;
                }
            }
            if(!add)
                items.add(new Item(name));
        }
    }
}
