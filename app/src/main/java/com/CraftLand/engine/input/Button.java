package com.craftland.engine.input;

import com.craftland.engine.core.Render;
import com.craftland.engine.core.Vector2;
import com.craftland.engine.gfx.Screen;
import com.craftland.engine.gfx.Texture;

public class Button
{
	private int width;
	private int height;
	private int w = 0;
	private int h = 0;
	private boolean key_button = false;
	private boolean prev_key_button = false;
	private float timer_longClick = 0;
	private float longClick_last = 800;
	private boolean release = false;
	private boolean T = false;

	public Texture texture;
	public Vector2 pos = new Vector2(0,0);
	public float rotation;
	
	public Button(String path, int _width, int _height, float x, float y)
	{
        texture = new Texture(path);
		rotation = 0;
		width = _width;
		height = _height;
		pos.X = x;
		pos.Y = y;
	}

	public Button(String path, float x, float y)
	{
		texture = new Texture(path);
		rotation = 0;
		width = texture.bitmapWidth;
		height = texture.bitmapHeight;
		pos.X = x;
		pos.Y = y;
	}

	public Button(String path)
	{
		texture = new Texture(path);
		rotation = 0;
		width = texture.bitmapWidth;
		height = texture.bitmapHeight;
		pos.X = 0;
		pos.Y = 0;
	}
	
	public void delete()
	{
		texture.delete();
		texture = null;
	}
	
	public boolean isDown()
	{		
		if(touched(0) || touched(1))
		{
			T = true;
			return true;
		}
		else
		{
			T = false;
			return false;
		}		
	}

	public boolean isDownOnce()
	{
		boolean result = false;
		prev_key_button = key_button;
		if(touched(0) && !prev_key_button){
			key_button = true;
			result = key_button;
		}
		if(!touched(0)){
			key_button = false;
		}
		return result;
	}

	public boolean isLongClick()
	{
		boolean b = false;

		if(touched(0))
		{
			T = true;
			timer_longClick += Render.timeDelta;
			if(timer_longClick >= longClick_last){
				b = true;
			}
		}
		else
		{
			timer_longClick = 0;
			T = false;
			b = false;
		}

		return b;
	}
	
	public boolean isUp()
	{
		prev_key_button = key_button;
		if(!prev_key_button){
			release = false;
		}

		if(touched(0) && !prev_key_button)
		{
			key_button = true;
			release = false;
			T = true;
		}
		else
		{
			if(!Render.inputManager.touchPointer[0] && prev_key_button){
				if(timer_longClick < longClick_last) {
					if(Render.inputManager.touch[0].X >= pos.X - w && Render.inputManager.touch[0].X <= pos.X+width+w &&
							Render.inputManager.touch[0].Y >= pos.Y - h && Render.inputManager.touch[0].Y <= pos.Y+height+h) {
						release = true;
					}
				}
				key_button = false;
				T = false;
			}
		}
		return release;
	}

	private boolean touched(int n)
	{
		if(Render.inputManager.touch[n].X >= pos.X - w && Render.inputManager.touch[n].X <= pos.X+width+w &&
				Render.inputManager.touch[n].Y >= pos.Y - h && Render.inputManager.touch[n].Y <= pos.Y+height+h
				&& Render.inputManager.touchPointer[n])
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void draw(Screen screen, float x, float y)
	{
		pos.X = x;
		pos.Y = y;
		if(T)
		{
			texture.color.R = 0;
			texture.color.G = 0;
			texture.color.B = 127;
		}
		else
		{
			texture.color.R = 255;
			texture.color.G = 255;
			texture.color.B = 255;
		}
		screen.draw(texture, pos.X, pos.Y, width, height, rotation);
	}

	public void draw(Screen screen)
	{
		draw(screen, pos.X, pos.Y);
	}

	public int getWidth()
	{
		return  width;
	}
	public int getHeight()
	{
		return  height;
	}

	public void setIncreaseArea(int _w, int _h)
	{
		w = _w;
		h = _h;
	}

	public void setLongTimeLast(float value)
	{
		longClick_last = value;
	}
}

