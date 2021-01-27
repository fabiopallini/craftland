package com.craftland.rpg.entity;

import com.craftland.engine.core.Collision;
import com.craftland.engine.core.Render;
import com.craftland.engine.core.Object;
import com.craftland.engine.core.Sound;
import com.craftland.engine.gfx.Texture;
import com.craftland.rpg.Resource;
import com.craftland.rpg.Tile;
import com.craftland.rpg.core.Action;
import com.craftland.rpg.core.Game;
import com.craftland.rpg.entity.Mob.Mob;

public class Boomerang extends Object
{
    private Sound sfx;
    private final int speed = 230;
    private final int range = 550;
    private short direction;
    private float timer_range;
    private float timer_damage;
    private float damage;
    private String resource;

    public Boomerang(String _resource)
    {
        super();
        resource = _resource;
        texture = new Texture(Resource.assets_Items + resource + ".png");
        sfx = Game.sfx.load("Hit.ogg");
        width = 32;
        height = 32;
        collisionWidth = 20;
        collisionHeight = 20;
        position.X = Game.player.position.X;
        position.Y = Game.player.position.Y;
        direction = Game.player.FACE_TO;

        switch (resource)
        {
            case Resource.Boomerang_Wood:
                damage = 5;
                break;
            case Resource.Boomerang_Iron:
                damage = 10;
                break;
            default:
                break;
        }
    }

    @Override
    public void update() {
        if(timer_damage > 0){
            timer_damage -= (float) Render.timeDelta;
            if(timer_damage < 0){
                timer_damage = 0;
            }
        }
        timer_range += (float) Render.timeDelta;
        rotation += 1400 * (float) Render.timeDelta / 1000;
        switch (direction) {
            case Action.WALK_LEFT:
                if (timer_range < range) {
                    position.X -= speed * (float) Render.timeDelta / 1000;
                } else {
                    backToPlayer();
                }
                break;
            case Action.WALK_RIGHT:
                if (timer_range < range) {
                    position.X += speed * (float) Render.timeDelta / 1000;
                } else {
                    backToPlayer();
                }
                break;
            case Action.WALK_UP:
                if (timer_range < range) {
                    position.Y -= speed * (float) Render.timeDelta / 1000;
                } else {
                    backToPlayer();
                }
                break;
            case Action.WALK_DOWN:
                if (timer_range < range) {
                    position.Y += speed * (float) Render.timeDelta / 1000;
                } else {
                    backToPlayer();
                }
                break;
            default:
                break;
        }

        if (Game.terrainCollision(centerX(), centerY()) && timer_range < 600) {
            if(Game.getTileBlock(centerX() / Tile.SIZE, centerY() / Tile.SIZE) != Tile.Water &&
                    Game.getTileBlock(centerX() / Tile.SIZE, centerY() / Tile.SIZE) != Tile.Water_header &&
                    Game.getTileBlock(centerX() / Tile.SIZE, centerY() / Tile.SIZE) != Tile.Lava &&
                    Game.getTileBlock(centerX() / Tile.SIZE, centerY() / Tile.SIZE) != Tile.Lava_header) {
                timer_range = 600;
            }
        }

        for (int n = 0; n < Game.stage.objects.size(); n++) {
            if(Game.stage.objects.get(n) instanceof Mob) {
                if (Collision.pixel(this, Game.stage.objects.get(n)) && timer_damage == 0) {
                    Mob m = (Mob) Game.stage.objects.get(n);
                    if (m.HP > 0) {
                        m.getDamage(damage);
                        timer_damage = 400;
                        sfx.play();
                    }
                    break;
                }
            }
        }

        if (Collision.pixel(this, Game.player) && timer_range >= 1000) {
            if(Game.menu.getItemB() == null) {
                Game.menu.setItemB(resource);
            }
            dispose();
        }
    }

    private void backToPlayer()
    {
        if(centerX() < Game.player.centerX())
        {
            position.X += speed * (float) Render.timeDelta / 1000;
        }
        if(centerX() > Game.player.centerX())
        {
            position.X -= speed * (float) Render.timeDelta / 1000;
        }
        if(centerY() < Game.player.centerY())
        {
            position.Y += speed * (float) Render.timeDelta / 1000;
        }
        if(centerY() > Game.player.centerY())
        {
            position.Y -= speed * (float) Render.timeDelta / 1000;
        }
    }
}
