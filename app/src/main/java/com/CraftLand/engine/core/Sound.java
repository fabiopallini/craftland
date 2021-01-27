package com.craftland.engine.core;

import com.craftland.rpg.core.Game;

public class Sound
{
    private int id;
    public Sound(int id)
    {
        this.id = id;
    }

    public void play()
    {
        Game.sfx.play(id);
    }

    public int getId(){return id;}
}
