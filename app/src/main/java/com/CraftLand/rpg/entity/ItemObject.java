package com.craftland.rpg.entity;

import com.craftland.engine.core.Collision;
import com.craftland.engine.core.Object;
import com.craftland.engine.core.Vector2;
import com.craftland.engine.gfx.Screen;
import com.craftland.engine.gfx.Texture;
import com.craftland.rpg.Resource;
import com.craftland.rpg.core.Game;

public class ItemObject extends Object
{
    private String name;

    public ItemObject(String name, int x, int y)
    {
        texture = new Texture(Resource.assets_Items + name + ".png");
        position = new Vector2(x, y);
        this.name = name;
        width = 20;
        height = 20;
        collisionX = 5;
        collisionY = 5;
        collisionWidth = 10;
        collisionHeight = 10;
        if(texture.bitmapWidth > 32){
            animationSet(1,1,32,0,false);
        }
    }

    public void update()
    {
        if(isOnCameraView()) {
            if(Collision.pixel(this, Game.player) && !Game.inventory.isFull(name)){
                Game.inventory.addItem(name, 1);
                dispose();
            }
        }
    }

    @Override
    public void draw(Screen screen) {
        super.draw(screen);
    }
}
