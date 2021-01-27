package com.craftland.engine.core;

public class Camera extends Vector2
{
    private float shakeTimer;
    private float shakeInterval;
    private int shakeDirection;

    public Camera()
    {
        super(0,0);
    }

    protected void update()
    {
        if(shakeTimer > 0){
            shakeTimer -= (float) Render.timeDelta;
            switch (shakeDirection){
                case 0:
                    Render.camera.X -= 0.1f * (float) Render.timeDelta;
                    break;
                case 1:
                    Render.camera.X += 0.1f * (float) Render.timeDelta;
                    break;
            }
            shakeInterval += (float) Render.timeDelta;
            if(shakeInterval > 40){
                shakeInterval = 0;
                switch (shakeDirection){
                    case 0:
                        shakeDirection++;
                        break;
                    case 1:
                        shakeDirection--;
                        break;
                }
            }
        }
    }

    public void shake()
    {
        if(shakeTimer <= 0) {
            shakeTimer = 200;
        }
    }

    public boolean isShaking()
    {
        if(shakeTimer > 0){
            return true;
        }
        return false;
    }
}
