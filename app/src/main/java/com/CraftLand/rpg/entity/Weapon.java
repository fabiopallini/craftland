package com.craftland.rpg.entity;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import com.craftland.engine.core.Collision;
import com.craftland.engine.core.Render;
import com.craftland.engine.core.Object;
import com.craftland.engine.core.Sound;
import com.craftland.engine.gfx.Screen;
import com.craftland.engine.gfx.Texture;
import com.craftland.rpg.Resource;
import com.craftland.rpg.Tile;
import com.craftland.rpg.core.Action;
import com.craftland.engine.parser.CsvReader;
import com.craftland.rpg.core.Game;
import com.craftland.rpg.entity.Mob.Mob;
import com.craftland.rpg.ui.Inventory;
import com.craftland.rpg.ui.Item;

public class Weapon extends Object
{
    public float getDelay(){return delay;}
    public List<Bullet> bullet = new ArrayList<>();
    public float getDamage(){return damage;}
    public int getBulletSize(){
        int result = 0;
        if(texture_bullet != null) {
            result = texture_bullet.bitmapWidth;
        }
        return result;
    }
    public boolean isRanged(){
        return isRanged;
    }
    public int ammoCount;

    private String name;
    private boolean attack = false;
    private float damage;
    private float delay;
    private float ttl;
    private Texture texture_hitEffect;
    private List<Object> obj_hitEffects;
    private byte hitEffectRow;
    private byte hitEffectSize;
    private Texture texture_bullet;
    private int bulletSpeed;
    private float bulletRotation;
    private boolean isAnimated;
    private boolean isRanged;
    private Sound sound;
    private Sound sound_bullet;

    public Weapon(String name)
    {
        super();
        this.name = name;
        texture_hitEffect = new Texture(Resource.assets_Effects + "bullet_hit.png");
        texture_hitEffect.color.A = 200;
        obj_hitEffects = new ArrayList<>();
        hitEffectRow = 1;
        hitEffectSize = 16;
        isAnimated = false;
        isRanged = false;
        weaponParser(name);
    }

    public void update() {
        onInitAttack(Game.player, Game.inventory);
        // collision with objects
        for (int i = 0; i < Game.stage.objects.size(); i++) {
            if (Game.stage.objects.get(i) instanceof Mob && !Game.stage.objects.get(i).isDispose()) {
                for (int n = 0; n < bullet.size(); n++) {
                    Bullet b = bullet.get(n);
                    if (Collision.pixel(b, Game.stage.objects.get(i))) {
                        damageDealer((Mob) Game.stage.objects.get(i));
                        bulletRemove(n, true, true);
                        break;
                    }
                }
            }
        }
        // collision with terrain elements
        for (int n = 0; n < bullet.size(); n++) {
            Bullet b = bullet.get(n);
            if (Game.terrainCollision(b.centerX(), b.centerY()) &&
                    !tileCollision(b.centerX(), b.centerY(), Tile.Water) &&
                    !tileCollision(b.centerX(), b.centerY(), Tile.Water_header) &&
                    !tileCollision(b.centerX(), b.centerY(), Tile.Lava) &&
                    !tileCollision(b.centerX(), b.centerY(), Tile.Lava_header)) {
                if(name.equals(Resource.Gun_RocketLauncher)) {
                    Game.setTileBlock(b.centerX() / Tile.SIZE, b.centerY() / Tile.SIZE, Tile.Ground);
                    Render.camera.shake();
                }
                bulletRemove(n, true, true);
                break;
            }
        }
        onAttack();
    }

    private boolean tileCollision(int x, int y, byte tile)
    {
        if(Game.getTileBlock(x / Tile.SIZE, y / Tile.SIZE) == tile)
            return true;
        return false;
    }

    @Override
    public void draw(Screen screen) {
        if (attack) {
            super.draw(screen);
        }
    }

    public void draw_bullets(Screen screen) {
        for (int n = 0; n < bullet.size(); n++)
            bullet.get(n).draw(screen);

        for (int n = 0; n < obj_hitEffects.size(); n++) {
            obj_hitEffects.get(n).draw(screen);
            if (obj_hitEffects.get(n).animationOver()) {
                obj_hitEffects.remove(n);
            }
        }
    }

    private void onInitAttack(Player player, Inventory inventory)
    {
        switch (player.FACE_TO) {
            case Action.WALK_DOWN:
                rotation = 180;
                position.X = player.position.X - (width / 4) + 2;
                position.Y = player.position.Y + (height / 2) + 5;
                break;
            case Action.WALK_UP:
                rotation = 0;
                position.X = player.position.X + (width / 4);
                position.Y = player.position.Y - (height / 2);
                break;
            case Action.WALK_LEFT:
                rotation = -90;
                position.X = player.position.X - (width / 2) - 3;
                position.Y = player.position.Y + (height / 4);
                break;
            case Action.WALK_RIGHT:
                rotation = 90;
                position.X = player.position.X + (width / 2);
                position.Y = player.position.Y + (height / 4);
                break;
        }

        if(player.CURRENT_ACTION == Action.BUTTON_A && !attack)
        {
            if(!isRanged){
                sound.play();
            }
            attack = true;
            if(isAnimated) {
                switch (player.FACE_TO) {
                    case Action.WALK_DOWN:
                        animationSet(2, 2, 32, 0, false);
                        break;
                    case Action.WALK_UP:
                        animationSet(1, 2, 32, 0, false);
                        break;
                    case Action.WALK_LEFT:
                        animationSet(2, 1, 32, 0, false);
                        break;
                    case Action.WALK_RIGHT:
                        animationSet(1, 1, 32, 0, false);
                        break;
                }
            }
            bulletRotation = rotation;
            if(isAnimated){
                rotation = 0;
            }
            if(!isRanged || useAmmo(inventory)) {
                sound.play();
                Bullet b = new Bullet(texture_bullet, position.X + (width / 3), position.Y + (height / 3),
                        getBulletSize(),
                        ttl, player.FACE_TO, bulletSpeed);
                b.rotation = bulletRotation;
                bullet.add(b);
            }

        }
        if(player.CURRENT_ACTION != Action.BUTTON_A && attack)
        {
            attack = false;
        }
    }

    private void onAttack() {
        for (int n = 0; n < bullet.size(); n++) {
            bullet.get(n).update_direction();
            if(bullet.get(n).getOutOfRange() || !bullet.get(n).isActive){
                if(bullet.get(n).isActive)
                    bulletRemove(n, false, false);
                else
                    bulletRemove(n, true, true);
                n--;
            }
        }
    }

    private boolean useAmmo(Inventory inventory)
    {
        Item item = null;
        if(name.startsWith("Bow"))
            item = inventory.getItem(Resource.Ammo_Arrow);
        if(name.startsWith("Gun"))
            item = inventory.getItem(Resource.Ammo_Bullet);
        if(name.equals(Resource.Gun_RocketLauncher))
            item = inventory.getItem(Resource.Ammo_Rocket);

        if (item != null) {
            ammoCount = item.counter - 1;
            if(item.counter >= 1) {
                inventory.useItem(item.getName());
                return true;
            }
        }
        return false;
    }

    public void bulletRemove(int n, boolean animation, boolean playSound)
    {
        if(animation) {
            obj_hitEffects.add(new Object());
            int index = obj_hitEffects.size() - 1;
            obj_hitEffects.get(index).position.X = bullet.get(n).position.X - (hitEffectSize / 4);
            obj_hitEffects.get(index).position.Y = bullet.get(n).position.Y - (hitEffectSize / 4);
            obj_hitEffects.get(index).texture = texture_hitEffect;
            obj_hitEffects.get(index).animationSet(4, hitEffectRow, 16, 65, false);
            obj_hitEffects.get(index).width = hitEffectSize;
            obj_hitEffects.get(index).height = hitEffectSize;
        }
        bullet.remove(n);
        if(playSound) {
            sound_bullet.play();
        }
    }

    private void weaponParser(String name)
    {
        CsvReader csv = new CsvReader(Resource.assets_Csv+"weapons.csv");
        texture = new Texture(Resource.assets_Items+name+".png");
        hitEffectRow = 1;
        hitEffectSize = 16;
        if(name.startsWith("Sword")) {
            texture_bullet = new Texture(Resource.assets_Effects + "bullet_1.png");
            sound = Game.sfx.load("Sword.ogg");
        }
        if(name.startsWith("Gun")) {
            if(name.equals(Resource.Gun_RocketLauncher)){
                texture_bullet = new Texture(Resource.assets_Effects + "rocket.png");
                sound = Game.sfx.load("Rocket.ogg");
                hitEffectRow = 2;
                hitEffectSize = 32;
            }else{
                texture_bullet = new Texture(Resource.assets_Effects + "bullet_4.png");
                sound = Game.sfx.load("Shoot.ogg");
            }
            isRanged = true;
            isAnimated = true;
        }
        if(name.startsWith("Bow")){
            texture_bullet = new Texture(Resource.assets_Effects + "arrow.png");
            isRanged = true;
            sound = Game.sfx.load("Bow.ogg");
        }
        if(name.startsWith("Staff")){
            if(name.equals(Resource.Staff_Fire)){
                texture_bullet = new Texture(Resource.assets_Effects + "bullet_3.png");
            }else {
                texture_bullet = new Texture(Resource.assets_Effects + "bullet_2.png");
            }
            sound = Game.sfx.load("Staff.ogg");
        }
        sound_bullet = Game.sfx.load("Hit.ogg");
        width = texture.bitmapWidth;
        height = texture.bitmapHeight;
        delay = csv.select_int(name, "delay");
        bulletSpeed = csv.select_int(name, "speed");
        ttl = csv.select_int(name, "ttl");
        damage = csv.select_int(name, "damage");
    }

    private void damageDealer(Mob mob) {
        if (!isRanged()) {
            float force = 16;
            switch (Game.player.FACE_TO) {
                case Action.WALK_DOWN:
                    mob.push_down(force);
                    break;
                case Action.WALK_UP:
                    mob.push_up(force);
                    break;
                case Action.WALK_LEFT:
                    mob.push_left(force);
                    break;
                case Action.WALK_RIGHT:
                    mob.push_right(force);
                    break;
                default:
                    break;
            }
        }
        mob.getDamage(getDamage());
    }
}
