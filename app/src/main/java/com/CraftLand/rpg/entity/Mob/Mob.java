package com.craftland.rpg.entity.Mob;

import java.lang.*;
import java.util.ArrayList;
import java.util.Random;
import com.craftland.engine.core.Collision;
import com.craftland.engine.core.Render;
import com.craftland.engine.gfx.Particles;
import com.craftland.engine.core.Vector2;
import com.craftland.engine.gfx.Screen;
import com.craftland.engine.gfx.Texture;
import com.craftland.rpg.Resource;
import com.craftland.engine.parser.CsvReader;
import com.craftland.rpg.Tile;
import com.craftland.rpg.core.Action;
import com.craftland.rpg.core.Game;
import com.craftland.rpg.entity.AIComponent;
import com.craftland.rpg.entity.Entity;
import com.craftland.rpg.entity.ItemObject;
import com.craftland.rpg.entity.Player;

public class Mob extends Entity {

    public String getName() {
        return name;
    }
    public boolean dropEnabled = true;
    public boolean deathAnimation;
    public boolean isTMX;
    public boolean terrainCollision = true;
    public boolean aiBaseActive = true;
    public AIComponent aiComponent;
    public boolean isHittable = true;
    public boolean immovable = false;
    public boolean enableRotationDirection = false;
    public int bulletShootTime = 2500;

    private String name;
    private int size;
    private boolean drop;
    private boolean aggroing;
    private float timer_death;
    private float timerDecision;
    private short aggroDirection;
    private short tempDirection;
    private Random random;

    public Mob(String _name, int size, float x, float y, int hp) {
        super("mobs/" + _name + ".png", 0);
        timer_death = 0;
        name = _name;
        position = new Vector2(x, y);
        this.size = size;
        setSize(size);
        animationSet(1, 1, size, 0, false);
        textures_blood = new ArrayList<>();
        textures_blood.add(new Texture(Resource.assets_Effects + "blood_1.png"));
        textures_blood.add(new Texture(Resource.assets_Effects + "blood_2.png"));
        particles_blood = new Particles(textures_blood);
        particles_blood.setAutoGenerate();
        particles_blood.setColor(255, 0, 0);
        isTMX = false;
        random = new Random();
        HP = hp;
        HP_MAX = hp;
    }

    @Override
    public void update() {
        if (isOnCameraView()) {
            if (HP > 0) {
                super.update();
                aiBase();
                rotationDirection();
                if (aiComponent != null) {
                    aiComponent.update();
                }
                if (CURRENT_ACTION != 0) {
                    if (PREV_ACTION != CURRENT_ACTION) {
                        animation_Move();
                        PREV_ACTION = CURRENT_ACTION;
                    }
                } else {
                    animation_Idle();
                    PREV_ACTION = 0;
                }
                hitRedColorDissolve();
                if (Collision.pixel(this, Game.player)) {
                    Game.player.damage((int)(HP_MAX * 0.05f));
                }
            } else {
                // on death
                randomDrop();
                timer_death += (float) Render.timeDelta;
                if (deathAnimation) {
                    animation_Death();
                    if (timer_death > 400) {
                        deathAnimation = false;
                    }
                }
            }
        }
        particles_blood.update(centerX(), centerY());
    }

    @Override
    public void draw(Screen screen) {
        if (HP > 0 || deathAnimation) {
            super.draw(screen);
        }
        particles_blood.draw(screen);
    }

    public void animation_Move() {
        animationSet(2, 2, size, 150, true);
    }

    public void animation_Idle() {
        animationSet(1, 1, size, 0, false);
    }

    public void animation_Death() {
        animationSet(4, 4, size, 100, false);
    }

    public boolean isAggroing() {
        return aggroing;
    }

    public boolean playerIsNear(Player player) {
        boolean result = false;
        int viewSize = 80;
        if (player.position.X + player.width >= position.X - (width + viewSize) &&
                player.position.X <= position.X + width + (width + viewSize) &&
                player.position.Y + player.height >= position.Y - (height + viewSize) &&
                player.position.Y <= position.Y + height + (height + viewSize)) {
            result = true;
        }
        return result;
    }

    public void hitRedColorDissolve() {
        if (texture.color.R != 255 || texture.color.B != 255 || texture.color.G != 255) {
            texture.color.R += 800.0f * Render.timeDelta / 1000;
            texture.color.G += 800.0f * Render.timeDelta / 1000;
            texture.color.B += 800.0f * Render.timeDelta / 1000;
            if (texture.color.R > 255) {
                texture.color.R = 255;
            }
            if (texture.color.G > 255) {
                texture.color.G = 255;
            }
            if (texture.color.B > 255) {
                texture.color.B = 255;
            }
        }
    }

    public void getDamage(float damage){
        if(HP > 0) {
            HP -= (int) damage;
            texture.color.R = 255;
            texture.color.G = 0;
            texture.color.B = 0;
            particles_BloodEffect();
            Game.displayDmg.display(this, damage);
        }
        if(HP <= 0)
            dispose();
    }

    public void randomDrop() {
        if (dropEnabled) {
            if (!drop) {
                drop = true;
                CsvReader csv = new CsvReader(Resource.assets_Csv + "drop.csv");
                String item[] = csv.selectAll(name, "drop");
                int percent[] = csv.selectAll_int(name, "percent");
                int qty[] = csv.selectAll_int(name, "qty");
                for (int n = 0; n < item.length; n++) {
                    if (item[n] != null) {
                        Random rand = new Random();
                        if (rand.nextInt(100) <= percent[n]) {
                            for (int i = 0; i < qty[n]; i++)
                                Game.stage.addObject(new ItemObject(item[n], (centerX() - 10) + (n * 4), (centerY() - 10) + (n * 4)));
                        }
                    }
                }
            }
        }
    }

    private void hunt(Player player) {
        aggroing = true;
        int m_x = centerX() / Tile.SIZE;
        int m_y = centerY() / Tile.SIZE;
        int p_x = player.centerX() / Tile.SIZE;
        int p_y = player.centerY() / Tile.SIZE;
        if (p_x < m_x || p_x > m_x) {
            if (p_x < m_x) {
                aggroDirection = Action.WALK_LEFT;
            }
            if (p_x > m_x) {
                aggroDirection = Action.WALK_RIGHT;
            }
        } else {
            if (p_y < m_y) {
                aggroDirection = Action.WALK_UP;
            }
            if (p_y > m_y) {
                aggroDirection = Action.WALK_DOWN;
            }
        }
        if (!Game.terrainCollision(this, aggroDirection) || !terrainCollision) {
            CURRENT_ACTION = aggroDirection;
        } else {
            if (aggroDirection == Action.WALK_LEFT || aggroDirection == Action.WALK_RIGHT) {
                if (tempDirection == 0) {
                    if (!Game.terrainCollision(this, Game.CollisionUp)) {
                        tempDirection = Action.WALK_UP;
                    }
                    if (!Game.terrainCollision(this, Game.CollisionDown)) {
                        tempDirection = Action.WALK_DOWN;
                    }
                } else {
                    if (!Game.terrainCollision(this, tempDirection)) {
                        CURRENT_ACTION = tempDirection;
                    } else {
                        tempDirection = 0;
                    }
                }
            }
        }
    }

    private void aiBase()
    {
        if(aiBaseActive) {
            if (playerIsNear(Game.player)) {
                hunt(Game.player);

            } else {
                aggroing = false;
                randomMove();
            }
        }
    }

    private void randomMove() {
        timerDecision += (float) Render.timeDelta;
        if (timerDecision > 4000) {
            timerDecision = 0;
            int r = random.nextInt(8);
            switch (r) {
                case 0:
                    if (!Game.terrainCollision(this, Game.CollisionLeft) || !terrainCollision) {
                        CURRENT_ACTION = Action.WALK_LEFT;
                    }
                    break;
                case 1:
                    if (!Game.terrainCollision(this, Game.CollisionRight) || !terrainCollision) {
                        CURRENT_ACTION = Action.WALK_RIGHT;
                    }
                    break;
                case 2:
                    if (!Game.terrainCollision(this, Game.CollisionUp) || !terrainCollision) {
                        CURRENT_ACTION = Action.WALK_UP;
                    }
                    break;
                case 3:
                    if (!Game.terrainCollision(this, Game.CollisionDown) || !terrainCollision) {
                        CURRENT_ACTION = Action.WALK_DOWN;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void rotationDirection()
    {
        if(enableRotationDirection) {
            switch (CURRENT_ACTION) {
                case Action.WALK_LEFT:
                    rotation = 90;
                    break;
                case Action.WALK_RIGHT:
                    rotation = -90;
                    break;
                case Action.WALK_UP:
                    rotation = -180;
                    break;
                case Action.WALK_DOWN:
                    rotation = 0;
                    break;
            }
        }
    }
}
