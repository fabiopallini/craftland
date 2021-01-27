package com.craftland.rpg.entity.Mob;

import com.craftland.engine.core.Render;
import com.craftland.rpg.Resource;
import com.craftland.rpg.core.Game;

public class GiantSpider extends Mob {

    private float timer_spawn;
    public GiantSpider(int size, int x, int y)
    {
        super(Resource.NM_GiantSpider, size, x, y, 500);
        immovable = true;
        terrainCollision = false;
        particles_blood.setColor(0,255,0);
    }

    @Override
    public void update() {
        super.update();
        if (HP > 0 && isOnCameraView()) {
            timer_spawn += (float) Render.timeDelta / 1000;
            if (timer_spawn >= 5) {
                timer_spawn = 0;
                Mob pet = Game.makeMob(Resource.mob_SpiderHigh, centerX() - 16, centerY());
                pet.terrainCollision = false;
            }
        }
    }
}
