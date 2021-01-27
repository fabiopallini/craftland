package com.craftland.rpg.entity.Mob;

import java.util.Random;
import com.craftland.engine.core.Collision;
import com.craftland.engine.core.Render;
import com.craftland.rpg.Tile;
import com.craftland.rpg.core.Action;
import com.craftland.rpg.core.Game;
import com.craftland.rpg.entity.Player;

public class Fish extends Mob {

    private Random random;
    private boolean jump;
    private float timerJump;
    private int jumpRange;

    public Fish(String name, int size, int x, int y) {
        super(name, size, x, y, 10);
        isHittable = false;
        random = new Random();
        aiBaseActive = false;
        jumpRange = size + (Tile.SIZE / 2);
    }

    @Override
    public void update() {
        if (isOnCameraView()) {
            particles_blood.update(centerX(), centerY());
            if (HP > 0)
            {
                if (Collision.pixel(this, Game.player) && jump) {
                    Game.player.damage(20);
                }
                walk();
                hitRedColorDissolve();

                if (Game.player.position.X < position.X + jumpRange &&
                        Game.player.position.X + jumpRange > position.X &&
                        Game.player.position.Y < position.Y + jumpRange &&
                        Game.player.position.Y + jumpRange > position.Y &&
                        !jump) {
                    jump = true;
                } else {
                    if (CURRENT_ACTION == 0) {
                        int x = centerX() / Tile.SIZE;
                        int y = centerY() / Tile.SIZE;
                        int size = width / Tile.SIZE;
                        switch (random.nextInt(4)) {
                            case 0:
                                if (Game.getTileBlock(x - size, y) == Tile.Water) {
                                    CURRENT_ACTION = Action.WALK_LEFT;
                                }
                                break;
                            case 1:
                                if (Game.getTileBlock(x + size, y) == Tile.Water) {
                                    CURRENT_ACTION = Action.WALK_RIGHT;
                                }
                                break;
                            case 2:
                                if (Game.getTileBlock(x, y - size) == Tile.Water) {
                                    CURRENT_ACTION = Action.WALK_UP;
                                }
                                break;
                            case 3:
                                if (Game.getTileBlock(x, y + size) == Tile.Water) {
                                    CURRENT_ACTION = Action.WALK_DOWN;
                                }
                                break;
                            default:
                                break;
                        }
                        water_hunt(Game.player);
                    }
                }
                if (jump) {
                    timerJump += (float) Render.timeDelta;
                    if (timerJump <= 600) {
                        if (Game.player.position.X < position.X) {
                            position.X -= 50 * (float) Render.timeDelta / 1000;
                        }
                        if (Game.player.position.X > position.X) {
                            position.X += 50 * (float) Render.timeDelta / 1000;
                        }
                        if (Game.player.position.Y < position.Y) {
                            position.Y -= 50 * (float) Render.timeDelta / 1000;
                        }
                        if (Game.player.position.Y > position.Y) {
                            position.Y += 50 * (float) Render.timeDelta / 1000;
                        }
                    } else {
                        if (timerJump >= 3000) {
                            jump = false;
                            timerJump = 0;
                        }
                    }
                }

                //animation
                int frame = 1;
                switch (CURRENT_ACTION) {
                    case Action.WALK_LEFT:
                        frame = 2;
                        break;
                    case Action.WALK_RIGHT:
                        frame = 1;
                        break;
                    default:
                        break;
                }
                if (!jump) {
                    int x = (int) position.X / Tile.SIZE;
                    int y = (int) position.Y / Tile.SIZE;

                    if (Game.getTileBlock(x, y) == Tile.Water &&
                            Game.getTileBlock(x + 1, y) == Tile.Water &&
                            Game.getTileBlock(x, y + 1) == Tile.Water &&
                            Game.getTileBlock(x + 1, y + 1) == Tile.Water) {
                        animationSet(frame, 1, 32, 0, false);
                        isHittable = false;
                    } else {
                        animationSet(frame, 2, 32, 0, false);
                        isHittable = true;
                    }
                } else {
                    animationSet(frame, 2, 32, 0, false);
                    isHittable = true;
                }
            }
            else{
                randomDrop();
            }
        }
    }

    private void water_hunt(Player player)
    {
        if(!jump) {
            int m_x = centerX() / Tile.SIZE;
            int m_y = centerY() / Tile.SIZE;
            int p_x = player.centerX() / Tile.SIZE;
            int p_y = player.centerY() / Tile.SIZE;
            if (p_x < m_x || p_x > m_x) {
                if (p_x < m_x && Game.tileCollision(this, Game.CollisionLeft, Tile.Water)) {
                    CURRENT_ACTION = Action.WALK_LEFT;
                }
                if (p_x > m_x && Game.tileCollision(this, Game.CollisionRight, Tile.Water)) {
                    CURRENT_ACTION = Action.WALK_RIGHT;
                }
            } else {
                if (p_y < m_y && Game.tileCollision(this, Game.CollisionUp, Tile.Water)) {
                    CURRENT_ACTION = Action.WALK_UP;
                }
                if (p_y > m_y && Game.tileCollision(this, Game.CollisionDown, Tile.Water)) {
                    CURRENT_ACTION = Action.WALK_DOWN;
                }
            }
        }
    }
}
