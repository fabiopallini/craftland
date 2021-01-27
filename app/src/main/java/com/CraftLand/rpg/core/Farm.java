package com.craftland.rpg.core;

import java.util.ArrayList;
import java.util.List;
import com.craftland.rpg.Resource;
import com.craftland.rpg.Tile;

public class Farm
{
    private List<FarmTile> farmTiles;

    public Farm(){
        farmTiles = new ArrayList<>();
    }

    public void update()
    {
        for (int n = 0; n < farmTiles.size(); n++) {
            farmTiles.get(n).update();
            if(farmTiles.get(n).isGrown())
                farmTiles.remove(n);
        }
    }

    public void farming(String itemName)
    {
        switch (itemName) {
            // compost prepare
            case Resource.Poo:
                if (Game.getTileBlock() == Tile.Ground) {
                    Game.inventory.useItem(itemName);
                    Game.setTileBlock(Tile.Compost_0);
                }
                break;
            case Resource.Water:
                if(Game.getTileBlock() == Tile.Compost_0){
                    Game.inventory.useItem(itemName);
                    Game.inventory.addItem(Resource.Bottle, 1);
                    Game.setTileBlock(Tile.Compost_1);
                }
                break;

            // ----- SEEDS ------

            case Resource.SeedsTree:
                if (compostReady(itemName)) {
                    addFarmTile(Resource.Wood);
                }
                break;

            case Resource.SeedCarrot:
                if (compostReady(itemName)) {
                    addFarmTile(Resource.Carrot);
                }
                break;

            // --------------------
            default:
                break;
        }
    }

    private boolean compostReady(String itemName)
    {
        if (Game.getTileBlock() == Tile.Compost_1) {
            Game.inventory.useItem(itemName);
            Game.setTileBlock(Tile.Compost_2);
            return true;
        }
        return false;
    }

    private void addFarmTile(String name){
        farmTiles.add(new FarmTile(name,
                Game.getTileX(),
                Game.getTileY()));
    }
}
