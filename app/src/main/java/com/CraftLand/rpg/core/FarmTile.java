package com.craftland.rpg.core;

import com.craftland.engine.core.Render;
import com.craftland.rpg.Tile;
import com.craftland.rpg.entity.ItemObject;

public class FarmTile {
    private String resultName;
    private int x, y;
    private short fase;
    private float waitTime;

    public FarmTile(String resultName,
                    int x, int y) {
        this.resultName = resultName;
        this.x = x;
        this.y = y;
        fase = 0;
    }

    public void update() {
        waitTime += 1 * (float) Render.timeDelta / 1000;
        if(waitTime >= 2 && fase == 0) {
            Game.setTileBlock(x, y, Tile.Compost_3);
            fase++;
        }
        else if (waitTime >= 4 && fase == 1) {
            Game.setTileBlock(x, y, Tile.Ground);
            fase++;
        }
        if (fase == 2) {
            for (int i = 0; i < 5; i++)
                Game.stage.addObject(new ItemObject(resultName,(x * Tile.SIZE) + (3 * i), (y * Tile.SIZE) + (3 * i)));
        }
    }

    public boolean isGrown() {
        if(fase >= 2)
            return true;
        return false;
    }
}