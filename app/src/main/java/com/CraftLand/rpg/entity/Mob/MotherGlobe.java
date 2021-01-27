package com.craftland.rpg.entity.Mob;

import java.util.ArrayList;
import java.util.List;
import com.craftland.engine.core.Render;
import com.craftland.rpg.Resource;
import com.craftland.rpg.core.Game;

public class MotherGlobe extends Mob {
    private List<Mob> pets;
    private float timer;
    private float[] angleSpeed;
    private float radius;
    private boolean expand;
    public MotherGlobe(int size, int x, int y)
    {
        super(Resource.NM_MotherGlobe, size, x, y, 1200);
        terrainCollision = false;
        immovable = true;
        pets = new ArrayList<>();
        for(int n = 0; n < 8; n++)
        {
            pets.add(Game.makeMob("NM_MotherGlobeChild", centerX(), centerY()));
            pets.get(n).aiBaseActive = false;
            pets.get(n).terrainCollision = false;
            pets.get(n).immovable = true;
        }
        angleSpeed = new float[pets.size()];
        radius = 32;
        expand = true;
    }

    @Override
    public void update() {
        super.update();
        if(HP > 0) {
            if (timer < pets.size() * 260) {
                timer += (float) Render.timeDelta;
            } else {
                if (expand) {
                    if (radius < 150) {
                        radius += 25 * (float) Render.timeDelta / 1000;
                    } else {
                        expand = false;
                    }
                } else {
                    if (radius > 90) {
                        radius -= 25 * (float) Render.timeDelta / 1000;
                    } else {
                        expand = true;
                    }
                }
            }
            for (int n = 0; n < pets.size(); n++) {
                if (timer > n * 260) {
                    angleSpeed[n] += 3 * (float) Render.timeDelta / 1000;
                    pets.get(n).position.X = (float) (centerX() + Math.cos(angleSpeed[n]) * radius) - 16;
                    pets.get(n).position.Y = (float) (centerY() + Math.sin(angleSpeed[n]) * radius) - 16;
                }
            }
        }else{
            for(int n = 0; n < pets.size(); n++){
                pets.get(n).HP = 0;
            }
            pets.clear();
        }
    }
}
