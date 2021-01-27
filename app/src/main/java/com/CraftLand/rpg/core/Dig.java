package com.craftland.rpg.core;

import java.util.Random;
import com.craftland.rpg.Resource;
import com.craftland.rpg.Tile;

public class Dig
{
    private Random random;

    private double[] drop_chances = {
            20, //stone
            2.0, //bronze
            0.65, //iron
            0.25, //gold
            0.24, //platinum
            0.23, //obsidian
            0.22, //cobalt
            0.21, //mythril
            0.20 //adamantite
    };

    public Dig()
    {
        random = new Random();
    }

    public void search(byte tileID)
    {
        Game.setTileBlock(Tile.Ground);
        if(tileID == Tile.Ground) {
            for (int n = 0; n < drop_chances.length; n++) {
                if (drop_chances[n] > random.nextDouble() * 100) {
                    switch (n) {
                        case 0:
                            Game.inventory.addItem(Resource.Stone, 1);
                            break;
                        case 1:
                            Game.inventory.addItem(Resource.Bronze, 1);
                            break;
                        case 2:
                            Game.inventory.addItem(Resource.Iron, 1);
                            break;
                        case 3:
                            Game.inventory.addItem(Resource.Gold, 1);
                            break;
                        case 4:
                            Game.inventory.addItem(Resource.Platinum, 1);
                            break;
                        case 5:
                            Game.inventory.addItem(Resource.Obsidian, 1);
                            break;
                        case 6:
                            Game.inventory.addItem(Resource.Cobalt, 1);
                            break;
                        case 7:
                            Game.inventory.addItem(Resource.Mythril, 1);
                            break;
                        case 8:
                            Game.inventory.addItem(Resource.Adamantite, 1);
                            break;
                        default:
                            break;
                    }
                    break;
                }
            }
        }
    }

    public boolean isDigTile(byte id) {
        if (id == Tile.Compost_0 ||
            id == Tile.Compost_1 ||
            id == Tile.Compost_2 ||
            id == Tile.Compost_3 ||
            id == Tile.ChestWood ||
            id == Tile.TombStone ||
            id == Tile.Pier ||
            id == Tile.Water_header ||
            id == Tile.Lava_header)
                return false;
        return true;
    }
}
