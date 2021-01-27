package com.craftland.engine.core;

import com.craftland.engine.gfx.Color;
import com.craftland.engine.gfx.Font;
import com.craftland.engine.gfx.Screen;

public class Gui
{
    public Font font;
    private Color color;

    public Gui()
    {
        font = new Font("fonts/Minefont.png");
        color = new Color(255,255,255,255);
    }

    public void draw_string(Screen screen, String str, int x, int y)
    {
        font.draw(screen, str, x, y, color);
    }

    public void draw_string(Screen screen, String str, int x, int y, int size)
    {
        font.draw(screen, str, x, y, size);
    }
}
