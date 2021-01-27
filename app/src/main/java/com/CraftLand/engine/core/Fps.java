package com.craftland.engine.core;

import com.craftland.engine.gfx.Font;
import com.craftland.engine.gfx.Screen;

public class Fps
{
	private float timer = 0;
	private int fps = 0;
	private int value = 0;

    public Fps() { }

	public void draw(Screen screen, Font font)
	{
        timer += (float) Render.timeDelta;
        fps++;
        if(timer >= 1000) {
            value = fps;
            fps = 0;
            timer = 0;
        }
        font.draw(screen, "fps " + value, 5, 5);
	}
}
