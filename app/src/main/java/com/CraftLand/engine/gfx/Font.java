package com.craftland.engine.gfx;

import java.util.ArrayList;
import java.util.List;

public class Font
{
    private Texture textureFont;
    private List<Texture> characters;
    public int drawSize;

    public Font(String fontType)
    {
        characters = new ArrayList<>();
        textureFont = new Texture(fontType);
        int size = 32;
        drawSize = 12;
        int w = textureFont.bitmapWidth / size;
        int h = textureFont.bitmapHeight / size;
        for(int y = 0; y < h; y++)
        {
            for(int x = 0; x < w; x++)
            {
                characters.add(new Texture(""));
                characters.get(characters.size()-1).setPixel(textureFont, x * size, y * size, size, size);
            }
        }
    }

    public void draw(Screen screen, String str, float x, float y)
    {
        draw(screen, str, x, y, drawSize);
    }

    public void draw(Screen screen, String str, float x, float y, Color color)
    {
        for(int i = 0; i < str.length(); i++)
        {
            int value = str.charAt(i);
            screen.draw(characters.get(value-32),
                    x + (i*drawSize), y,
                    drawSize, drawSize,
                    0,
                    color);
        }
    }

    public void draw(Screen screen, String str, float x, float y, int size, Color color)
    {
        for(int i = 0; i < str.length(); i++)
        {
            int value = str.charAt(i);
            screen.draw(characters.get(value-32),
                    x + (i*size), y,
                    size, size,
                    0,
                    color);
        }
    }

    public void draw(Screen screen, String str, float x, float y, int size)
    {
        for(int i = 0; i < str.length(); i++)
        {
            int value = str.charAt(i);
            screen.draw(characters.get(value-32), x + (i*size), y, size, size);
        }
    }
}
