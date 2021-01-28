package com.craftland.rpg.entity.Mob;

import com.craftland.engine.core.Collision;
import com.craftland.engine.core.Render;
import com.craftland.engine.core.Sound;
import com.craftland.engine.gfx.Texture;
import com.craftland.rpg.Resource;
import com.craftland.rpg.core.Game;
import com.craftland.rpg.entity.Bullet;

public class Dragon extends Mob {
    private float timer_walk;
    private float timer_shoot;
    private Texture texture_bullet;
    private boolean animationShoot;
    private int prevSpeed;

    private Sound sound_fireball;
    private Sound sound_walk;
    private int SPEED;

    public Dragon(String name, int size, int x, int y) {
        super(name, size, x, y, 1800);
        terrainCollision = false;
        immovable = true;
        texture_bullet = new Texture(Resource.assets_Effects + "fireball2.png");
        prevSpeed = 32;

        sound_fireball = Game.sfx.load("Fireball.ogg");
        sound_walk = Game.sfx.load("DragonWalk.ogg");
        sound_walk.play();
    }

    @Override
    public void update() {
        super.update();
        if(HP > 0 && isOnCameraView())
        {
            if(SPEED > 0 && CURRENT_ACTION != 0) {
                timer_walk += (float) Render.timeDelta;
                if (timer_walk >= 500) {
                    timer_walk = 0;
                    sound_walk.play();
                }
            }
            timer_shoot += (float) Render.timeDelta;
            if(timer_shoot >= 300 && animationShoot){
                animationShoot = false;
                animationSet(2,2,64,150,true);
                SPEED = prevSpeed;
            }
            if(timer_shoot >= 2500){
                timer_shoot = 0;
                shoot();
                animationSet(2,1,64,0,false);
                animationShoot = true;
                SPEED = 0;
            }
            if(Collision.pixel(this, Game.player)){
                Game.player.damage(50);
            }
        }
    }

    private void shoot() {
        for (int n = 0; n < 3; n++) {
            final int j = n;
            Bullet bullet = new Bullet(texture_bullet, centerX() - 16, centerY() - 10, 32, 0, 0, 100) {
                @Override
                public void update() {
                    super.update();
                    update_direction((Game.player.centerX() - 64) + (64 * j), Game.player.centerY());
                    if (Collision.pixel(this, Game.player)) {
                        Game.player.damage(10);
                    }
                }
            };
            bullet.texture = texture_bullet;
            bullet.collisionWidth = 10;
            bullet.collisionHeight = 10;
            bullet.animationSet(4, 1, 16, 100, true);
            Game.stage.addObject(bullet);
        }
        sound_fireball.play();
    }
}
