package com.craftland.rpg.entity;

import com.craftland.engine.core.Render;
import com.craftland.engine.core.Object;
import com.craftland.engine.core.Vector2;
import com.craftland.engine.gfx.Texture;
import com.craftland.rpg.core.Action;

public class Bullet extends Object {
    private float speed;
    private boolean ranged;
    private float ttl;
    private int size;
    private double angle;
    private Vector2 target;
    public int direction;
    public boolean isActive;

    public Bullet(Texture texture, float x, float y,
                  int size,
                  float _ttl,
                  int _direction, int _speed) {
        super();
        this.texture = texture;
        isActive = true;
        ttl = _ttl;
        if (ttl <= 0) {
            ranged = true;
        } else {
            ranged = false;
        }
        this.size = size;
        width = size;
        height = size;
        angle = 0;
        direction = _direction;
        speed = _speed;
        position = new Vector2(x, y);
    }

    @Override
    public void update() {
        super.update();
        if(getOutOfRange()){
            dispose();
        }
    }

    public void update_direction() {
        if (direction == Action.WALK_DOWN) {
            position.Y += speed * (float) Render.timeDelta / 1000;
        }
        if (direction == Action.WALK_UP) {
            position.Y -= speed * (float) Render.timeDelta / 1000;
        }
        if (direction == Action.WALK_LEFT) {
            position.X -= speed * (float) Render.timeDelta / 1000;
        }
        if (direction == Action.WALK_RIGHT) {
            position.X += speed * (float) Render.timeDelta / 1000;
        }

        if (ttl > 0) {
            ttl -= (float) Render.timeDelta;
        }
    }

    public void update_direction(float x, float y)
    {
        if(target == null) {
            position.X = centerX();
            position.Y = centerY();
            target = new Vector2(x - position.X,
                    y - position.Y);
            angle = Math.atan2(target.Y, target.X);
        }
        position.X += Math.cos(angle) * ((float) Render.timeDelta / 1000) * speed;
        position.Y += Math.sin(angle) * ((float) Render.timeDelta / 1000) * speed;
    }

    public boolean getOutOfRange() {
        if (ranged) {
            if (position.X + size < Render.camera.X ||
                    position.X > Render.camera.X + Render.zoom_width ||
                    position.Y + size < Render.camera.Y ||
                    position.Y > Render.camera.Y + Render.zoom_height) {

                return true;
            }
        } else {
            if (ttl <= 0) {
                return true;
            }
        }
        return false;
    }
}
