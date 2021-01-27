package com.craftland.rpg.entity;

import java.util.List;
import com.craftland.engine.core.Render;
import com.craftland.engine.core.Object;
import com.craftland.engine.core.Vector2;
import com.craftland.engine.gfx.Particles;
import com.craftland.engine.gfx.Screen;
import com.craftland.engine.gfx.Texture;
import com.craftland.rpg.Resource;
import com.craftland.rpg.Tile;
import com.craftland.rpg.core.Action;
import com.craftland.rpg.core.Game;

public class Entity extends Object
{
    public int animation_speed = 150;
    public short CURRENT_ACTION;
    public short PREV_ACTION;
    public short FACE_TO;
    public List<Texture> textures_blood;
    public Particles particles_blood;
    public final int walkSize = 8;
    private float walkTile;

    public Entity(String spritePath, int _size)
    {
        if(spritePath != null) {
            texture = new Texture(Resource.assets + spritePath);
        }
        else
        {
            texture = new Texture(null);
        }
        position = new Vector2(0,0);
        setSize(_size);
        walkTile = walkSize;
        FACE_TO = Action.WALK_DOWN;
    }

    public void update()
    {
        if(isOnCameraView()) {
            walk();
        }
    }

    public void draw(Screen screen)
    {
        if(isOnCameraView()){
            super.draw(screen);
        }
    }

    public void particles_BloodEffect()
    {
        if(particles_blood != null) {
            particles_blood.setSpawn(centerX(), centerY());
        }
    }

    public void push_up(float force){
        if (!Game.terrainCollision(this, Game.CollisionUp)){
            position.Y -= force;
        }
    }

    public void push_down(float force){
        if (!Game.terrainCollision(this, Game.CollisionDown)){
            position.Y += force;
        }
    }

    public void push_left(float force){
        if (!Game.terrainCollision(this, Game.CollisionLeft)){
            position.X -= force;
        }
    }

    public void push_right(float force){
        if (!Game.terrainCollision(this, Game.CollisionRight)){
            position.X += force;
        }
    }

    public void setSize(int _size)
    {
        int size;
        if(_size != 0) {
            size = _size;
        }
        else
        {
            size = texture.bitmapWidth;
        }
        width = size;
        height = size;
        collisionWidth = size;
        collisionHeight = size;
    }

    public void setPosInTile(int x, int y) {
        position.X = x * Tile.SIZE;
        position.Y = y * Tile.SIZE;
    }

    public void walk() {
        if (CURRENT_ACTION == Action.WALK_LEFT ||
                CURRENT_ACTION == Action.WALK_RIGHT ||
                CURRENT_ACTION == Action.WALK_UP ||
                CURRENT_ACTION == Action.WALK_DOWN) {
            if (walkTile > 0.0f) {
                float value = 24 * (float) (Render.timeDelta / 1000);
                switch (CURRENT_ACTION) {
                    case Action.WALK_LEFT:
                        position.X -= value;
                        break;
                    case Action.WALK_RIGHT:
                        position.X += value;
                        break;
                    case Action.WALK_UP:
                        position.Y -= value;
                        break;
                    case Action.WALK_DOWN:
                        position.Y += value;
                        break;
                }
                walkTile -= value;

            } else {
                CURRENT_ACTION = 0;
                walkTile = walkSize;
            }
        }
    }
}
