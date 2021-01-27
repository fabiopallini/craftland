package com.craftland.rpg.entity.Mob;

import com.craftland.engine.core.Render;
import com.craftland.rpg.Resource;
import com.craftland.rpg.core.Game;

public class BigEye extends Mob {

    private float timer;
    private boolean run;

    public BigEye(int size, int x, int y) {
        super(Resource.NM_BigEye, size, x, y, 950);
        terrainCollision = false;
        immovable = true;
    }

    @Override
    public void update() {
        super.update();
        if (HP > 0 && isOnCameraView()) {
            timer += (float) Render.timeDelta;
            if (timer >= 5000) {
                if (!run) {
                    run = true;
                    animationSet(2, 1, 64, 0, false);
                }
                if (centerX() < Game.player.centerX()) {
                    position.X += 32 * (float) Render.timeDelta / 1000;
                }
                if (centerX() > Game.player.centerX()) {
                    position.X -= 32 * (float) Render.timeDelta / 1000;
                }
                if (centerY() < Game.player.centerY()) {
                    position.Y += 32 * (float) Render.timeDelta / 1000;
                }
                if (centerY() > Game.player.centerY()) {
                    position.Y -= 32 * (float) Render.timeDelta / 1000;
                }
            }
            if (timer >= 7000) {
                timer = 0;
                run = false;
                animationSet(2, 2, 64, 150, true);
            }
        }
    }

    @Override
    public void animation_Move() {
        super.animation_Move();
        if (run) {
            animationSet(2, 1, 64, 0, false);
        }
    }
}
