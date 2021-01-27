package com.craftland.engine.input;

import com.craftland.engine.core.Render;
import com.craftland.engine.gfx.Screen;

public class VirtualGamepad
{
    private Stick stick;
    private Button A;
    private Button B;

    public VirtualGamepad()
    {
        stick = new Stick(32,32);
        stick.color.A = 130;
        A = new Button("button_circle.png", 42, 42, Render.zoom_width - 52, Render.zoom_height - 62);
        A.texture.color.A = 130;
        B = new Button("button_circle.png", 42, 42, Render.zoom_width - 105, Render.zoom_height - 62);
        B.texture.color.A = 130;
    }
    
    public void draw(Screen screen) {
        if (stick != null)
            stick.draw(screen);
        A.draw(screen);
        B.draw(screen);
    }

    public boolean b_isDown()
    {
        if (Render.inputManager.x == 1 || B.isDown()) {
            Render.inputManager.x = 0;
            return true;
        }
        return false;
    }

    public boolean a_isDownOneTime()
    {
        if (Render.inputManager.z == 1 || A.isDownOnce()) {
            Render.inputManager.z = 0;
            return true;
        }
        return false;
    }

    public float axeX(){
        if(stick != null)
            return stick.axeX;
        return 0;
    }

    public float axeY(){
        if(stick != null)
            return stick.axeY;
        return 0;
    }
}
