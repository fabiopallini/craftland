package com.craftland.rpg.entity.Mob;

import com.craftland.engine.core.Render;
import com.craftland.engine.core.Object;
import com.craftland.engine.core.Sound;
import com.craftland.engine.gfx.Texture;
import com.craftland.rpg.Resource;
import com.craftland.rpg.Tile;
import com.craftland.rpg.core.Action;
import com.craftland.rpg.core.Game;
import com.craftland.rpg.entity.Player;

public class Bomb extends Object {

    private Sound sfx;
    private float speed;
    private float timer_countDown;
    private short direction;
    private float timer_explosion;
    private boolean explosion;

    public Bomb() {
        super();
        texture = new Texture(Resource.assets_Items + Resource.Bomb + ".png");
        sfx = Game.sfx.load("Fireball.ogg");
        width = 32;
        height = 32;
        collisionX = 18;
        collisionY = 18;
        collisionWidth = 50;
        collisionHeight = 50;
        direction = Game.player.FACE_TO;
        speed = 350;
        timer_countDown = 2000;
        animationSet(2, 1, 32, 100, true);
        position.X = Game.player.position.X;
        position.Y = Game.player.position.Y;
    }

    @Override
    public void update() {
        if (speed > 0) {
            switch (direction) {
                case Action.WALK_LEFT:
                    position.X -= speed * (float) Render.timeDelta / 1000;
                    break;
                case Action.WALK_RIGHT:
                    position.X += speed * (float) Render.timeDelta / 1000;
                    break;
                case Action.WALK_UP:
                    position.Y -= speed * (float) Render.timeDelta / 1000;
                    break;
                case Action.WALK_DOWN:
                    position.Y += speed * (float) Render.timeDelta / 1000;
                    break;
                default:
                    break;
            }
            speed -= 1000 * (float) Render.timeDelta / 1000;
        }
        timer_countDown -= (float) Render.timeDelta;
        if (timer_countDown <= 0 && timer_explosion == 0) {
            timer_explosion++;
            animationSet(2, 2, 32, 100, false);
            sfx.play();
            Render.camera.shake();
            modifyTerrain();
        }
        if (timer_explosion > 0) {
            timer_explosion += (float) Render.timeDelta;
            if (timer_explosion >= 200) {
                dispose();
            }
        }
        if (Game.terrainCollision(centerX(), centerY())) {
            speed = 0;
        }
        if (timer_explosion > 0 && !explosion) {
            explosion = true;
            //collision with player
            Player p = Game.player;
            int size = 32;
            if(position.X + width >= p.position.X - size && position.X <= p.position.X + p.width + size &&
                    position.Y + height >= p.position.Y - size && position.Y <= p.position.Y + p.height + size)
                    p.damage(50);
            // collision with mobs
            for (int n = 0; n < Game.stage.objects.size(); n++) {
                if(Game.stage.objects.get(n) instanceof Mob) {
                    Mob m = (Mob)Game.stage.objects.get(n);
                    if(position.X + width >= m.position.X - size && position.X <= m.position.X + m.width + size &&
                        position.Y + height >= m.position.Y - size && position.Y <= m.position.Y + m.height + size){
                        m.getDamage(50);
                    }
                }
            }
        }
    }

    private void modifyTerrain() {
        int posX = centerX() / Tile.SIZE;
        int posY = centerY() / Tile.SIZE;
        for (int y = posY - 1; y < posY + 1; y++) {
            for (int x = posX - 1; x < posX + 1; x++) {
                if (Game.getTileBlock(x, y) != Tile.Water &&
                        Game.getTileBlock(x, y) != Tile.Water_header &&
                        Game.getTileBlock(x, y) != Tile.Lava &&
                        Game.getTileBlock(x, y) != Tile.Lava_header) {
                    Game.setTileBlock(x, y, Tile.Ground);
                }
            }
        }
    }
}
