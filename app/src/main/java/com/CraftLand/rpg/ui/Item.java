package com.craftland.rpg.ui;

import com.craftland.engine.core.Object;
import com.craftland.engine.gfx.Screen;
import com.craftland.engine.gfx.Texture;
import com.craftland.rpg.Resource;

public class Item extends Object
{
    public Texture getTexture(){return texture;}
    public int getType(){return type;}
    public String getName(){return name;}
    public int counter;

    private String name;
    private int type;

    public Item(String name)
    {
        super();
        this.name = name;
        counter = 1;
        if(name.startsWith("Sword") ||
                name.startsWith("Gun") ||
                name.startsWith("Bow") ||
                name.startsWith("Staff")){
            type = Resource.WEAPON;
        }
        else
        {
            if(name.startsWith("Armor")){
                type = Resource.ARMOR;
            }
            else {
                type = Resource.ITEM;
            }
        }
        switch (type)
        {
            case Resource.WEAPON:
                texture = new Texture(Resource.assets_Items + name + ".png");
                if(name.startsWith("Gun")){
                    animationSet(1, 1, 32, 0, false);
                }
                break;
            case Resource.ARMOR:
                texture = new Texture(Resource.assets_Items + name + ".png");
                break;
            case Resource.ITEM:
                texture = new Texture(Resource.assets_Items + name + ".png");
                break;
            default:
                break;
        }
        if(texture != null){
            if(texture.bitmapWidth > 32){
                animationSet(1,1,32,0,false);
            }
        }
    }

    public void draw(Screen screen, float x, float y)
    {
        position.X = x;
        position.Y = y;
        super.draw(screen);
    }

    public void draw(Screen screen, float x, float y, int w, int h)
    {
        position.X = x;
        position.Y = y;
        int prevW = width;
        int prevH = height;
        width = w;
        height = h;
        super.draw(screen);
        width = prevW;
        height = prevH;
    }
}
