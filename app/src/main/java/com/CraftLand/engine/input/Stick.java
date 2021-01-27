package com.craftland.engine.input;

import com.craftland.engine.core.Render;
import com.craftland.engine.core.Vector2;
import com.craftland.engine.gfx.Color;
import com.craftland.engine.gfx.Screen;
import com.craftland.engine.gfx.Texture;

public class Stick {

    public float axeX,axeY;
    public Color color;
    private Texture texture, textureOrigin;
    private Vector2 vec_origin, firstTouchOrigin;
    private boolean input;
    private int index;
    private byte direction;
    private int w,h;

    public Stick(int width, int height)
    {
        texture = new Texture("stick.png");
        textureOrigin = new Texture("stick.png");
        vec_origin = new Vector2(0,0);
        firstTouchOrigin = new Vector2(50,Render.zoom_height-50);
        input = false;
        color = new Color(255,255,255,255);
        w = width;
        h = height;
    }

    public void draw(Screen screen) {
        update();
        screen.draw(textureOrigin,firstTouchOrigin.X - ((w + 20) / 2),
                firstTouchOrigin.Y - ((h + 20) / 2),
                w + 20, h + 20, 0, color);
        if(input) {
            screen.draw(texture, Render.inputManager.touch[index].X - (w / 2),
                    Render.inputManager.touch[index].Y - (h / 2),
                    w, h, 0, color);
        }
        else {
            screen.draw(texture, firstTouchOrigin.X - (w / 2),
                    firstTouchOrigin.Y - (h / 2),
                    w, h, 0, color);
        }
    }

    public boolean left(){
        if(input && direction == 1){
            return true;
        }
        return false;
    }

    public boolean right(){
        if(input && direction == 2){
            return true;
        }
        return false;
    }

    public boolean up(){
        if(input && direction == 3){
            return true;
        }
        return false;
    }

    public boolean down(){
        if(input && direction == 4){
            return true;
        }
        return false;
    }

    private void update()
    {
        if(!Render.pause) {
            for (int n = 0; n < 1; n++) {
                if (inputOnLeftArea(Render.inputManager.touch[n].X) &&
                        Render.inputManager.touchPointer[n]) {
                    if (!input) {
                        vec_origin.X = Render.inputManager.touch[n].X;
                        vec_origin.Y = Render.inputManager.touch[n].Y;
                        firstTouchOrigin.X = vec_origin.X;
                        firstTouchOrigin.Y = vec_origin.Y;
                        index = n;
                        input = true;
                        break;
                    }
                }

                if (input) {
                    if(Render.inputManager.touch[index].X <= firstTouchOrigin.X - 20)
                        Render.inputManager.touch[index].X = firstTouchOrigin.X - 20;
                    if(Render.inputManager.touch[index].X >= firstTouchOrigin.X + 20)
                        Render.inputManager.touch[index].X = firstTouchOrigin.X + 20;

                    if(Render.inputManager.touch[index].Y <= firstTouchOrigin.Y - 20)
                        Render.inputManager.touch[index].Y = firstTouchOrigin.Y - 20;
                    if(Render.inputManager.touch[index].Y >= firstTouchOrigin.Y + 20)
                        Render.inputManager.touch[index].Y = firstTouchOrigin.Y + 20;

                    float x = Render.inputManager.touch[index].X;
                    float y = Render.inputManager.touch[index].Y;
                    axeX = x - firstTouchOrigin.X;
                    axeY = y - firstTouchOrigin.Y;
                    if (!Render.inputManager.touchPointer[index]) {
                        input = false;
                        direction = 0;
                        axeX = 0;
                        axeY = 0;
                    }
                }
            }
        }
    }

    private boolean inputOnLeftArea(float x) {
        if (x < (Render.zoom_width / 2) - 50) {
            return true;
        } else {
            return false;
        }
    }
}
