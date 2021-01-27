package com.craftland.rpg.entity.Mob;

import java.util.Random;
import com.craftland.engine.core.Render;
import com.craftland.rpg.Resource;
import com.craftland.rpg.core.Game;
import com.craftland.rpg.entity.AIComponent;

public class SpiderQueen extends Mob
{
    private float childTimer = 0;
    private boolean deathAction = false;
    private Random random;

    public SpiderQueen(int size, int x, int y) {
        super(Resource.NM_SpiderQueen, size, x, y, 1400);
        particles_blood.setColor(95, 2, 148);
        terrainCollision = false;
        immovable = true;
        AIComponent ai = new AIComponent(){
            @Override
            public void update() {
                childTimer += (float) Render.timeDelta;
                if(childTimer >= 3000 && isAggroing()) {
                    childTimer = 0;
                    Game.stage.addObject(Game.makeMob(Resource.mob_Spider, centerX() - 16, bottom() - 32));
                }
            }
        };
        aiComponent = ai;
    }

    @Override
    public void update() {
        super.update();
        if(HP <= 0 && !deathAction){
            deathAction = true;
            random = new Random();
            for(int n = 0; n < 10; n++) {
                Game.stage.addObject(Game.makeMob(Resource.mob_Spider,
                        (int) (position.X + random.nextInt(width)),
                        (int) (position.Y + random.nextInt(height))));
            }
        }
    }
}
