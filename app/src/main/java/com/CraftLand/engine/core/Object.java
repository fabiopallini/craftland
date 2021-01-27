package com.craftland.engine.core;

import com.craftland.engine.gfx.Screen;
import com.craftland.engine.gfx.Texture;

public class Object
{
    public Texture texture;
    public Vector2 position;
    public int width;
    public int height;
    public float rotation;
    public int HP, HP_MAX;
    //animation
    public boolean animated;
    public int animFrames;
    public int animRow;
    public int animSize;
    public int animInterval;
    public boolean animLoop;
    private float animTimer;
    private int animFrame;
    private boolean animRun;
    private boolean dispose;
    //prev animation
    private int prevFrames, prevRow, prevSize, prevInterval;
    private boolean prevLoop;
    //collision
    public int collisionX, collisionY, collisionWidth, collisionHeight;

    public Object()
    {
        position = new Vector2(0,0);
        width = 0;
        height = 0;
        rotation = 0.0f;
        //animation
        animated = false;
        animFrames = 0;
        animRow = 0;
        animSize = 0;
        animInterval = 0;
        animTimer = 0.0f;
        animFrame = 0;
        animRun = false;
        animLoop = false;
    }

    public void dispose()
    {
        dispose = true;
    }

    public void update()
    {

    }

    public void animationSet(int frames, int row, int size, int interval, boolean loop)
    {
        if(texture != null &&
                prevFrames != frames || prevRow != row ||
                prevSize != size || prevInterval != interval ||
                prevLoop != loop)
        {
            prevFrames = frames;
            prevRow = row;
            prevSize = size;
            prevInterval = interval;
            prevLoop = loop;

            width = size;
            height = size;
            animated = true;
            animFrame = 0;
            animFrames = frames - 1;
            animRow = row - 1;
            animSize = size;
            animInterval = interval;
            animLoop = loop;
            animRun = true;
            animTimer = 0;
            if (interval == 0) {
                animFrame = animFrames;
                animRun = false;
            }
        }
    }

    public void draw(Screen screen) {
        if (texture != null) {
            if (!Render.pause && animRun) {
                animTimer += (float) Render.timeDelta;
                if (animTimer >= animInterval) {
                    animTimer = 0;
                    if (animFrame <= animFrames) {
                        animFrame++;
                    }
                    if (animFrame > animFrames) {
                        animFrame = 0;
                        if (!animLoop) {
                            animRun = false;
                        }
                    }
                }
            }

            if (animated) {
                screen.draw(texture,
                        position.X,
                        position.Y,
                        width,
                        height,
                        animFrame * animSize,
                        animRow * animSize,
                        animSize,
                        animSize,
                        rotation,
                        texture.color);
            } else {
                if (width == 0 || height == 0) {
                    screen.draw(texture,
                            position.X,
                            position.Y,
                            texture.bitmapWidth,
                            texture.bitmapHeight,
                            0,
                            0,
                            texture.bitmapWidth,
                            texture.bitmapHeight,
                            rotation,
                            texture.color);
                } else {
                    screen.draw(texture,
                            position.X,
                            position.Y,
                            width,
                            height,
                            0,
                            0,
                            texture.bitmapWidth,
                            texture.bitmapHeight,
                            rotation,
                            texture.color);
                }
            }
        }
    }

    public void animationStop()
    {
        animated = false;
    }

    public boolean animationOver()
    {
        if(animFrame >= animFrames){
            return true;
        }
        return false;
    }

    public boolean isOnCameraView()
    {
        if(position != null) {
            if (position.X + width >= Render.camera.X && position.X <= Render.camera.X + Render.zoom_width
                    && position.Y + height >= Render.camera.Y && position.Y <= Render.camera.Y + Render.zoom_height) {
                return true;
            }
        }
        return false;
    }

    public int centerX()
    {
        return (int)position.X + (width / 2);
    }

    public int centerY()
    {
        return (int)position.Y + (height / 2);
    }

    public int left()
    {
        return (int)position.X;
    }

    public int right()
    {
        return (int)position.X + width;
    }

    public int top()
    {
        return (int)position.Y;
    }

    public int bottom()
    {
        return (int)position.Y + height;
    }

    public boolean isDispose()
    {
        return dispose;
    }
}
