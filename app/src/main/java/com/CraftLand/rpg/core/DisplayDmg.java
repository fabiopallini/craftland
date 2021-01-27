package com.craftland.rpg.core;

import java.util.ArrayList;
import java.util.List;
import com.craftland.engine.core.Render;
import com.craftland.engine.core.Vector2;
import com.craftland.engine.gfx.Color;
import com.craftland.engine.gfx.Screen;
import com.craftland.engine.gfx.Texture;
import com.craftland.rpg.Resource;
import com.craftland.rpg.entity.Mob.Mob;

public class DisplayDmg
{
    private float timer = 0;
    private List<String> dmg = new ArrayList<>();
    private List<Vector2> pos = new ArrayList<>();
    private int size = 12;
    private Color color;
    private Texture sprite_lifeBar;
    private Vector2 position_lifeBar;
    private float hp_percent;

    public DisplayDmg()
    {
        color = new Color(255,0,0,255);
        sprite_lifeBar = new Texture(Resource.assets_Gui + "LifeBar.png");
    }

    public void update()
    {
        if(dmg.size() > 0){
            timer += (float) Render.timeDelta;
            if(timer >= 400)
            {
                timer = 0;
                dmg.remove(0);
                pos.remove(0);
            }
            for(int n = 0; n < dmg.size(); n++) {
                pos.get(n).Y -= 50 * (float) Render.timeDelta / 1000;
            }
        }
    }

    public void draw(Screen screen)
    {
        for(int n = 0; n < dmg.size(); n++)
        {
            Game.gui.font.draw(screen, dmg.get(n),
                    pos.get(n).X, pos.get(n).Y,
                    size, color);
        }
        if(dmg.size() > 0){
            if(position_lifeBar != null) {
                screen.draw(sprite_lifeBar, position_lifeBar.X, position_lifeBar.Y - 6, (int)hp_percent, 6);
            }
        }
    }

    public void display(Mob mob, float damage)
    {
        set_display("" + (int) damage, mob.position.X + mob.width / 2.5f, mob.position.Y);
        setMob(mob.position, mob.HP, mob.HP_MAX);
    }

    public void display(String str, float x, float y)
    {
        set_display(str, x, y);
    }

    private void setMob(Vector2 position, float hp, int hp_max){
        position_lifeBar = position;
        if(hp >= 1.0f) {
            hp_percent = (hp * 100.0f) / hp_max;
            hp_percent /= 3.0f;
        }else{
            hp_percent = 0;
        }
    }

    private void set_display(String str, float x, float y)
    {
        dmg.add(str);
        pos.add(new Vector2(x-(size/4), y-(size/2)));
    }
}
