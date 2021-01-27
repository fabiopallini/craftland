package com.craftland.engine.input;

import com.craftland.engine.core.Vector2;

public class InputManager
{
    public boolean[] touchPointer = new boolean[2];
    public Vector2[] touch = new Vector2[2];
    public int dpadLeft, dpadRight, dpadUp, dpadDown, z, x;

    public InputManager()
    {
        touchPointer[0] = false;
        touchPointer[1] = false;
        touch[0] = new Vector2(0, 0);
        touch[1] = new Vector2(0, 0);
    }
}
