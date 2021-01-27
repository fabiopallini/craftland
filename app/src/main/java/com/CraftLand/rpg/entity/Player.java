package com.craftland.rpg.entity;

import com.craftland.engine.gfx.Particles;
import com.craftland.engine.gfx.Screen;
import com.craftland.engine.gfx.Texture;
import com.craftland.engine.core.*;
import com.craftland.rpg.Resource;
import com.craftland.rpg.core.*;
import com.craftland.rpg.UseItem;
import com.craftland.rpg.ui.Item;
import com.craftland.engine.core.Render;

public class Player extends Entity {

    public Weapon weapon;
    private float timer_attack = 0;
    private boolean enemyHit = false;
    private float timer_hit = 0;
    private String equip = "";
    private Texture pickaxe_sprite;
    private float pickaxe_timer = 0;
    private int pickaxe_rot = 0;
    private Vector2 pickaxe_pos;
    private boolean pickaxe_draw = false;
    private Particles particles_Pickaxe;
    private UseItem useItem;
    private float timer_gameover = 0;
    private boolean invincible;
    private float invincibleTimer;
    private Sound sound_pickaxe;
    private Sound sound_hurt;

    private byte prevButtonA = 0;
    private float timerPrevButtonA = 0;

    public Player(String filePath)
    {
        super(filePath, 32);
        collisionWidth = 20;
        collisionHeight = 20;
        animationSet(2, 1, 32, 0, false);
        pickaxe_sprite = new Texture(Resource.assets_Items + "Pickaxe.png");
        particles_blood = new Particles(new Texture(Resource.assets_Effects + "blood_1.png"));
        particles_blood.setAutoGenerate();
        particles_blood.setColor(255, 0, 0);
        pickaxe_pos = new Vector2(0,0);
        particles_Pickaxe = new Particles(new Texture(Resource.assets_Effects + "dig.png"));
        particles_Pickaxe.setGenerate(5, 2,
                400, 50, 0, 150f);
        useItem = new UseItem();
        invincible = true;
        invincibleTimer = 0.0f;
        sound_pickaxe = Game.sfx.load("Pickaxe.ogg");
        sound_hurt = Game.sfx.load("Hurt.ogg");
    }

    public void update()
    {
        if(HP > 0)
        {
            //keyboard input
            if(Render.inputManager.dpadLeft == 1){
                FACE_TO = Action.WALK_LEFT;
                if(!Game.terrainCollision(this, Game.CollisionLeft))
                    position.X -= 64 * (float) (Render.timeDelta / 1000);
            }
            if(Render.inputManager.dpadRight == 1){
                FACE_TO = Action.WALK_RIGHT;
                if(!Game.terrainCollision(this, Game.CollisionRight))
                    position.X += 64 * (float) (Render.timeDelta / 1000);
            }
            if(Render.inputManager.dpadUp == 1){
                FACE_TO = Action.WALK_UP;
                if(!Game.terrainCollision(this, Game.CollisionUp))
                    position.Y -= 64 * (float) (Render.timeDelta / 1000);
            }
            if(Render.inputManager.dpadDown == 1){
                FACE_TO = Action.WALK_DOWN;
                if(!Game.terrainCollision(this, Game.CollisionDown))
                    position.Y += 64 * (float) (Render.timeDelta / 1000);
            }
            //touch input
            if (Render.gamepad.axeX() < 0 && Render.gamepad.axeX() < Render.gamepad.axeY())
                FACE_TO = Action.WALK_LEFT;
            if (Render.gamepad.axeX() > 0 && Render.gamepad.axeX() > Render.gamepad.axeY())
                FACE_TO = Action.WALK_RIGHT;
            if (Render.gamepad.axeY() < 0 && Render.gamepad.axeY() < Render.gamepad.axeX())
                FACE_TO = Action.WALK_UP;
            if (Render.gamepad.axeY() > 0 && Render.gamepad.axeY() > Render.gamepad.axeX())
                FACE_TO = Action.WALK_DOWN;

            if (Render.gamepad.axeX() != 0 && Render.gamepad.axeY() != 0 ||
                    Render.inputManager.dpadLeft == 1 || Render.inputManager.dpadRight == 1 ||
                    Render.inputManager.dpadUp == 1 || Render.inputManager.dpadDown == 1)
            {
                int speedX = (int) Render.gamepad.axeX() * 3;
                if (speedX > 64)
                    speedX = 64;
                if (speedX < -64)
                    speedX = -64;
                int speedY = (int) Render.gamepad.axeY() * 3;
                if (speedY > 64)
                    speedY = 64;
                if (speedY < -64)
                    speedY = -64;
                if (speedX < 0 && !Game.terrainCollision(this, Game.CollisionLeft))
                    position.X += speedX * (float) (Render.timeDelta / 1000);
                if (speedX > 0 && !Game.terrainCollision(this, Game.CollisionRight))
                    position.X += speedX * (float) (Render.timeDelta / 1000);
                if (speedY < 0 && !Game.terrainCollision(this, Game.CollisionUp))
                    position.Y += speedY * (float) (Render.timeDelta / 1000);
                if (speedY > 0 && !Game.terrainCollision(this, Game.CollisionDown))
                    position.Y += speedY * (float) (Render.timeDelta / 1000);

                // walk animation
                if(FACE_TO == Action.WALK_DOWN)
                    animationSet(4, 1, 32, animation_speed, true);
                else if(FACE_TO == Action.WALK_RIGHT)
                    animationSet(4, 2, 32, animation_speed, true);
                else if(FACE_TO == Action.WALK_LEFT)
                    animationSet(4, 3, 32, animation_speed, true);
                else if(FACE_TO == Action.WALK_UP)
                    animationSet(4, 4, 32, animation_speed, true);
                else
                    animationSet(4, 1, 32, animation_speed, true);
            }
            else {
                // stand still
                if(FACE_TO == Action.WALK_DOWN)
                    animationSet(1, 1, 32, 0, false);
                else if(FACE_TO == Action.WALK_RIGHT)
                    animationSet(1, 2, 32, 0, false);
                else if(FACE_TO == Action.WALK_LEFT)
                    animationSet(1, 3, 32, 0, false);
                else if(FACE_TO == Action.WALK_UP)
                    animationSet(1, 4, 32, 0, false);
                else
                    animationSet(1, 1, 32, 0, false);
            }
        }

        if(invincible){
            invincibleTimer += (float) Render.timeDelta;
            if(invincibleTimer > 3000){
                invincibleTimer = 0.0f;
                invincible = false;
            }
        }
        if(HP > 0) {
            input();
            A_B_Actions();
            actionAnimation();
            if (weapon != null) {
                weapon.update();
            }
        }
        hurtEffectUpdate();
        particles_blood.update(centerX() - 3, centerY());
        particles_Pickaxe.update(null);
        if(HP <= 0){
            timer_gameover += (float) Render.timeDelta;
            if(timer_gameover >= 1500) {
                Game.gameOver = true;
            }
        }
    }

    @Override
    public void draw(Screen screen)
    {
        if(HP > 0) {
            particles_Pickaxe.draw(screen);
            if (pickaxe_draw) {
                screen.draw(pickaxe_sprite, pickaxe_pos.X, pickaxe_pos.Y, 32, 32, pickaxe_rot);
            }
            if (weapon != null) {
                weapon.draw(screen);
            }
            super.draw(screen);
        }
        particles_blood.draw(screen);

        if(weapon != null){
            weapon.draw_bullets(screen);
        }
    }

    public void damage(int damage) {
        if (!invincible && !enemyHit && HP > 0) {
            enemyHit = true;
            Game.displayDmg.display("" + (int) damage, position.X + width / 2.5f, position.Y);
            texture.color.A = 50;
            HP -= (int)damage;
        }
    }

    public void setEquip(String name)
    {
        if(!name.equals("")) {
            if(equip.equals(name)){
                // take off equip
                equip = "";
                texture = new Texture(Resource.assets_Characters + "character.png");
            }
            else {
                // wear new equip
                equip = name;
                texture = new Texture(Resource.assets_Characters + equip + ".png");
            }
        }
    }

    public String getEquip()
    {
        return equip;
    }

    private void hurtEffectUpdate()
    {
        if(enemyHit)
        {
            if(timer_hit == 0){
                particles_blood.setSpawn(centerX()-3, centerY());
                texture.color.A = 150;
                sound_hurt.play();
            }
            timer_hit += (float) Render.timeDelta;
            if(timer_hit >= 2000)
            {
                timer_hit = 0;
                enemyHit = false;
                texture.color.A = 255;
            }
        }
    }

    private void input() {
        if (CURRENT_ACTION == 0) {
            if (Render.gamepad.b_isDown() && weapon != null)
                CURRENT_ACTION = Action.BUTTON_A;
            if (Render.gamepad.a_isDownOneTime())
                CURRENT_ACTION = Action.BUTTON_B;
        }
    }

    private void A_B_Actions()
    {
        //attack
        if(CURRENT_ACTION == Action.BUTTON_A)
        {
            if(weapon != null) {
                timer_attack += (float) Render.timeDelta;
                if (timer_attack >= weapon.getDelay()) {
                    CURRENT_ACTION = 0;
                    timer_attack = 0;
                }
            }
        }
        if(CURRENT_ACTION == Action.BUTTON_B)
        {
            Item i = Game.menu.getItemB();
            // pickaxe action
            if(i != null && i.getName().equals(Resource.Pickaxe)){
                if(!pickaxe_draw) {
                    pickaxe_draw = true;
                    useItem(i);
                    float x = 0.0f;
                    float y = 0.0f;
                    pickaxe_rot = 0;
                    sound_pickaxe.play();
                    switch (FACE_TO){
                        case Action.WALK_LEFT:
                            x = position.X - (width/2);
                            y = position.Y + 7;
                            pickaxe_rot = -120;
                            particles_Pickaxe.setSpawn(x+5, y+24);
                            break;
                        case Action.WALK_RIGHT:
                            x = position.X + (width/2);
                            y = position.Y + 7;
                            pickaxe_rot = 30;
                            particles_Pickaxe.setSpawn(x+24, y+24);
                            break;
                        case Action.WALK_UP:
                            x = position.X;
                            y = position.Y - 16;
                            pickaxe_rot = -60;
                            particles_Pickaxe.setSpawn(x+10, y+5);
                            break;
                        case Action.WALK_DOWN:
                            x = position.X;
                            y = position.Y + 19;
                            pickaxe_rot = 90;
                            particles_Pickaxe.setSpawn(x+10, y+24);
                            break;
                    }
                    pickaxe_pos.X = x;
                    pickaxe_pos.Y = y;
                }
                pickaxe_timer += (float) Render.timeDelta;
                if(pickaxe_timer >= 200){
                    pickaxe_timer = 0;
                    pickaxe_draw = false;
                    CURRENT_ACTION = 0;
                }
            }
            else
            {
                pickaxe_draw = false;
                if(prevButtonA == 0){
                    useItem(i);
                    prevButtonA = 1;
                }
            }
        }

        if(prevButtonA == 1) {
            timerPrevButtonA += (float) Render.timeDelta;
            if (timerPrevButtonA >= 500) {
                prevButtonA = 0;
                timerPrevButtonA = 0;
                CURRENT_ACTION = 0;
            }
        }
    }

    private void actionAnimation() {
        if (CURRENT_ACTION == Action.BUTTON_A || CURRENT_ACTION == Action.BUTTON_B)
        {
            if(FACE_TO == Action.WALK_DOWN)
                animationSet(2, 1, 32, 0, false);
            else if(FACE_TO == Action.WALK_RIGHT)
                animationSet(4, 2, 32, 0, false);
            else if(FACE_TO == Action.WALK_LEFT)
                animationSet(4, 3, 32, 0, false);
            else if (FACE_TO == Action.WALK_UP)
                animationSet(2, 4, 32, 0, false);
        }
    }

    private void useItem(Item i){
        if(i != null) {
            String name = i.getName();
            useItem.item(name);
        }
    }
}
