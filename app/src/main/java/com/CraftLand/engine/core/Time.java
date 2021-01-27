package com.craftland.engine.core;

public class Time
{
    private long beginTime;
    private long now;

    public Time()
    {
        beginTime = System.currentTimeMillis();
    }

    public void updateDeltaTime()
    {
        now = System.currentTimeMillis();
        Render.timeDelta = now - beginTime;
        beginTime = now;
    }
}
