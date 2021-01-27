package com.craftland.rpg.ui;

import com.craftland.engine.core.Render;
import com.craftland.engine.gfx.Screen;
import com.craftland.engine.input.Button;
import com.craftland.rpg.Resource;
import com.craftland.rpg.core.Game;

public class BoxButton
{
    public Item getItem(){return item;}
    private Button button;
    private Item item;

    public BoxButton(int x, int y, Item item)
    {
        button = new Button(Resource.assets_Gui + "box.png", 50, 50, x, y);
        this.item = item;
    }

    public void draw(Screen screen)
    {
        button.draw(screen, button.pos.X, button.pos.Y);
        item.draw(screen, button.pos.X + 9, button.pos.Y + 9);
        if(item.counter > 1) {
            Game.gui.draw_string(screen, ""+item.counter, (int)button.pos.X+22, (int)button.pos.Y+33);
        }
    }

    public boolean isUp()
    {
        return button.isUp();
    }

    public boolean isLongClick()
    {
        return button.isLongClick();
    }

    public void movePosY(float speed){
        button.pos.Y += speed * (float) Render.timeDelta / 1000;
    }

    public float getPosY()
    {
        return button.pos.Y;
    }
}
