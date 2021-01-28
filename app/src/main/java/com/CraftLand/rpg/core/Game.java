package com.craftland.rpg.core;

import com.craftland.engine.core.Gui;
import com.craftland.engine.core.Render;
import com.craftland.engine.core.SFX;
import com.craftland.engine.core.Stage;
import com.craftland.engine.gfx.Screen;
import com.craftland.engine.gfx.Texture;
import com.craftland.engine.gfx.TileMap;
import com.craftland.engine.input.Button;
import com.craftland.engine.parser.CsvReader;
import com.craftland.engine.parser.TMXObject;
import com.craftland.engine.parser.TiledReader;
import com.craftland.rpg.Resource;
import com.craftland.rpg.Tile;
import com.craftland.rpg.entity.Entity;
import com.craftland.rpg.entity.Mob.BigEye;
import com.craftland.rpg.entity.Mob.Dragon;
import com.craftland.rpg.entity.Mob.Fish;
import com.craftland.rpg.entity.Mob.GiantSpider;
import com.craftland.rpg.entity.Mob.Mob;
import com.craftland.rpg.entity.Mob.MotherGlobe;
import com.craftland.rpg.entity.Mob.SpiderQueen;
import com.craftland.rpg.entity.Player;
import com.craftland.rpg.ui.Inventory;
import com.craftland.rpg.ui.Menu;
import java.util.List;

    /* TODO
        - tasto pickaxe non funziona quando ci si muove
    */

public class Game
{
    public static Stage stage;
    public static WorldMap worldMap;
    public static Gui gui;
    public static SFX sfx;
    public static Player player;
    public static Menu menu;
    public static Inventory inventory;
    public static Farm farm;
    public static DisplayDmg displayDmg;
    public static boolean gameOver;
    public static final short CollisionLeft = 1;
    public static final short CollisionRight = 2;
    public static final short CollisionUp = 3;
    public static final short CollisionDown = 4;

    public MemoryCard memoryCard;
    private float timerGameOver;
    private Button button_pause;
    private Screen screen;

    public Game(){
        screen = new Screen();
        stage = new Stage(screen);
        button_pause = new Button(Resource.assets_Gui + "pause.png", 32, 32, Render.zoom_width - 34, 2);
        gui = new Gui();
        sfx = new SFX();

        memoryCard = new MemoryCard();
        menu = new Menu();
        player = new Player("characters/character.png");
        // new game
        player.HP = 3;
        player.HP_MAX = 3;
        player.setPosInTile(12, 9);
        menu.inventory.addItem(Resource.Pickaxe, 1);
        menu.setItemB(Resource.Pickaxe);
        // load
        //memoryCard.load(player, menu);

        farm = new Farm();
        displayDmg = new DisplayDmg();
        Game.inventory = menu.inventory;

        // TESTING
        //player.setLEVEL((byte)99);
        menu.inventory.addItem(Resource.Ammo_Arrow, 99);
        menu.inventory.addItem(Resource.Ammo_Rocket, 99);
        menu.inventory.addItem(Resource.Bow_Wood, 1);
        menu.inventory.addItem(Resource.Sword_Wood, 1);
        menu.inventory.addItem(Resource.Boomerang_Wood, 1);
        menu.inventory.addItem(Resource.RocketLauncher, 1);
        menu.inventory.addItem(Resource.Water, 10);
        menu.inventory.addItem(Resource.SeedCarrot, 10);
        menu.inventory.addItem(Resource.Cherry, 10);
        menu.inventory.addItem(Resource.Poo, 10);
        menu.inventory.addItem(Resource.Bomb, 10);
        //Game.stage.addObject(Game.makeMob(Resource.NM_GiantSpider, 150, 150));

        changeMap("map_0");
    }

    public void update()
    {
        if (!Game.gameOver) {
            menu.update();
            if(!Render.pause) {
                cameraFollow(player);
                player.update();
                worldMap.update(player);
                farm.update();
                displayDmg.update();
                stage.update();
            }
            if(button_pause.isUp() && !Render.pause && player.HP > 0) {
                memoryCard.save();
                Render.activity.finish();
            }

            //change map
            if(tileCollision(player, 0, Tile.Exit)) {
                stage.clear();
                changeMap("map_1");
            }
        }
        else
        {
            //on game over
            timerGameOver += (float) Render.timeDelta;
            if(timerGameOver >= 3000){
                timerGameOver = 0;
                Game.gameOver = false;
                Render.activity.finish();
            }
        }
    }

    public void draw()
    {
        worldMap.draw();
        stage.draw();
        player.draw(screen);
        displayDmg.draw(screen);
    }

    public void draw_interface()
    {
        if(!Render.pause) {
            Render.gamepad.draw(screen);
            button_pause.draw(screen);
        }
        menu.draw(screen);
    }

    private void changeMap(String name){
        TiledReader tiledReader = new TiledReader("game_assets/tiled/"+name+".tmx", true);
        worldMap = new WorldMap(Resource.assets + "tileset.png", Tile.SIZE, tiledReader.getTiles());
        Tile.MapWidth = worldMap.getArrayWidth();
        Tile.MapHeight = worldMap.getArrayHeight();
        List<TMXObject> objects = tiledReader.getObjects();
        for(TMXObject obj : objects)
            Game.stage.addObject(Game.makeMob(obj.getName(), obj.getX(), obj.getY()));
        player.setPosInTile(12, 9);
    }

    private void cameraFollow(Player player){
        if(!Render.camera.isShaking()) {
            float x = player.position.X - Render.zoom_width / 2;
            float y = player.position.Y - Render.zoom_height / 2;
            if (x > (Tile.MapWidth * 32) - Render.zoom_width) {
                x = (Tile.MapWidth * 32) - Render.zoom_width;
            }
            if (x < 0) {
                x = 0;
            }
            if (y > (Tile.MapHeight * 32) - Render.zoom_height) {
                y = (Tile.MapHeight * 32) - Render.zoom_height;
            }
            if (y < 0) {
                y = 0;
            }
            Render.camera.X = x;
            Render.camera.Y = y;
        }
    }

    public static int getTileX()
    {
        int x = -1;
        switch (player.FACE_TO)
        {
            case Action.WALK_LEFT:
                x = player.left();
                break;
            case Action.WALK_RIGHT:
                x = player.right();
                break;
            case Action.WALK_UP:
                x = player.centerX();
                break;
            case Action.WALK_DOWN:
                x = player.centerX();
                break;
        }
        x /= worldMap.getTileSize();
        return x;
    }

    public static int getTileY()
    {
        int y = -1;
        switch (player.FACE_TO)
        {
            case Action.WALK_LEFT:
                y = player.centerY();
                break;
            case Action.WALK_RIGHT:
                y = player.centerY();
                break;
            case Action.WALK_UP:
                y = player.top();
                break;
            case Action.WALK_DOWN:
                y = player.bottom() + player.walkSize;
                break;
        }
        y /= worldMap.getTileSize();
        return y;
    }

    public static byte getTileBlock() {
        byte result;
        int x = getTileX();
        int y = getTileY();
        result = worldMap.getTileValue(x, y);
        return result;
    }

    public static byte getTileBlock(int x, int y){
        return worldMap.getTileValue(x, y);
    }

    public static byte getPlayerFootOn()
    {
        return worldMap.getTileValue(player.centerX() / Tile.SIZE, player.bottom() / Tile.SIZE);
    }

    public static void setTileBlock(byte value) {
        int x = getTileX();
        int y = getTileY();
        worldMap.setTileValue(x, y, value);
    }

    public static void setTileBlock(int x, int y, byte value) {
        worldMap.setTileValue(x, y, value);
    }

    public static byte[][] getMapArray()
    {
        return worldMap.array;
    }

    public static Mob makeMob(String name, int x, int y) {
        Mob mob = null;
        CsvReader csvMob = new CsvReader(Resource.assets_Csv + "mobs.csv");
        final int size = csvMob.select_int(name, "size");
        if (size != 0) {
            int hp = csvMob.select_int(name, "hp");
            switch (name) {
                case Resource.mob_Slime:
                    mob = new Mob(name, size, x, y, hp) {
                        @Override
                        public void animation_Move() {
                            animationSet(4, 2, size, 100, true);
                        }
                    };
                    mob.particles_blood.setColor(0, 0, 255);
                    break;
                case Resource.mob_DarkPhantom:
                    mob = new Mob(name, size, x, y, hp) {
                        @Override
                        public void animation_Move() {
                            animationSet(4, 2, size, 150, true);
                        }
                    };
                    mob.particles_blood.setColor(0, 0, 0);
                    break;
                case Resource.mob_Bomb:
                    mob = new Mob(name, size, x, y, hp) {
                        @Override
                        public void animation_Idle() {
                            animationSet(4, 1, 32, 100, true);
                        }
                    };
                    mob.particles_blood.setColor(0, 0, 0);
                    mob.deathAnimation = true;
                    mob.animationSet(4, 1, 32, 100, true);
                    break;
                case Resource.mob_Spider:
                    mob = new Mob(name, size, x, y, hp);
                    mob.particles_blood.setColor(0, 255, 0);
                    break;
                case Resource.mob_Plant:
                    mob = new Mob(name, size, x, y, hp);
                    mob.particles_blood.setColor(0, 255, 0);
                    break;
                case Resource.NM_SpiderQueen:
                    mob = new SpiderQueen(size, x, y);
                    break;
                case Resource.NM_SkeletonKing:
                    mob = new Mob(name, size, x, y, hp);
                    mob.terrainCollision = false;
                    mob.immovable = true;
                    Texture texture_bullet2 = new Texture(Resource.assets_Effects + "fireball2.png");
                    mob.bulletShootTime = 1500;
                    break;
                case Resource.NM_MotherGlobe:
                    mob = new MotherGlobe(size, x, y);
                    break;
                case Resource.NM_GiantSpider:
                    mob = new GiantSpider(size, x, y);
                    break;
                case Resource.NM_BigEye:
                    mob = new BigEye(size, x, y);
                    break;
                case Resource.NM_Dragon_Green:
                    mob = new Dragon(name, size, x, y);
                    break;
                default:
                    if (name.startsWith(Resource.mob_Fish)) {
                        mob = new Fish(name, size, x, y);
                    } else {
                        mob = new Mob(name, size, x, y, hp);
                    }
                    break;
            }

        }
        return mob;
    }

    public static boolean terrainCollision(Entity e, int collision) {
        TileMap tileMap = worldMap;
        int x = (int) e.position.X / Tile.SIZE;
        int y = (int) e.position.Y / Tile.SIZE;
        int bottom = e.bottom() / Tile.SIZE;

        if(collision == CollisionLeft && e.right() / Tile.SIZE <= 0)
            return true;
        if(collision == CollisionRight && e.right() / Tile.SIZE >= Tile.MapWidth)
            return true;
        if(collision == CollisionUp && bottom <= 0)
            return true;
        if(collision == CollisionDown && bottom >= Tile.MapHeight)
            return true;

        if (x >= 0 && x < tileMap.getArrayWidth() && y >= 0 && y < tileMap.getArrayHeight() &&
                bottom < tileMap.getArrayHeight()) {
            for (int n = 0; n < Tile._collision.length; n++) {
                switch (collision) {
                    case CollisionLeft:
                        if (tileMap.getTileValue(e.left() / tileMap.getTileSize(), e.centerY() / tileMap.getTileSize()) == Tile._collision[n]
                                || tileMap.getTileValue(e.left() / tileMap.getTileSize(), e.bottom() / tileMap.getTileSize()) == Tile._collision[n])
                            return true;
                        break;
                    case CollisionRight:
                        if (tileMap.getTileValue(e.right() / tileMap.getTileSize(), e.centerY() / tileMap.getTileSize()) == Tile._collision[n]
                                || tileMap.getTileValue(e.right() / tileMap.getTileSize(), e.bottom() / tileMap.getTileSize()) == Tile._collision[n])
                            return true;
                        break;
                    case CollisionUp:
                        if (tileMap.getTileValue(e.centerX() / tileMap.getTileSize(), e.top() / tileMap.getTileSize()) == Tile._collision[n])
                            return true;
                        break;
                    case CollisionDown:
                        if (tileMap.getTileValue(e.centerX() / tileMap.getTileSize(), (e.bottom() + e.walkSize) / tileMap.getTileSize()) == Tile._collision[n])
                            return true;
                        break;
                }
            }
        }
        return false;
    }

    public static boolean terrainCollision(int x, int y) {
        x /= Tile.SIZE;
        y /= Tile.SIZE;
        for (int n = 0; n < Tile._collision.length; n++) {
            if (Game.worldMap.getTileValue(x, y) == Tile._collision[n]) {
                return true;
            }
        }
        return false;
    }

    public static boolean tileCollision(Entity e, int collision, byte tile){
        TileMap tileMap = worldMap;
        int x = (int) e.position.X / Tile.SIZE;
        int y = (int) e.position.Y / Tile.SIZE;
        int bottom = e.bottom() / Tile.SIZE;
        if (x >= 0 && x < tileMap.getArrayWidth() && y >= 0 && y < tileMap.getArrayHeight() &&
                bottom < tileMap.getArrayHeight()) {
            switch (collision) {
                case CollisionLeft:
                    if (tileMap.getTileValue(e.left() / tileMap.getTileSize(), e.centerY() / tileMap.getTileSize()) == tile
                            || tileMap.getTileValue(e.left() / tileMap.getTileSize(), e.bottom() / tileMap.getTileSize()) == tile)
                        return true;
                    break;
                case CollisionRight:
                    if (tileMap.getTileValue(e.right() / tileMap.getTileSize(), e.centerY() / tileMap.getTileSize()) == tile
                            || tileMap.getTileValue(e.right() / tileMap.getTileSize(), e.bottom() / tileMap.getTileSize()) == tile)
                        return true;
                    break;
                case CollisionUp:
                    if (tileMap.getTileValue(e.centerX() / tileMap.getTileSize(), e.top() / tileMap.getTileSize()) == tile)
                        return true;
                    break;
                case CollisionDown:
                    if (tileMap.getTileValue(e.centerX() / tileMap.getTileSize(), (e.bottom() + e.walkSize) / tileMap.getTileSize()) == tile)
                        return true;
                    break;
                default:
                    if (tileMap.getTileValue(e.centerX() / tileMap.getTileSize(), (e.centerY() / tileMap.getTileSize())) == tile)
                        return true;
                    break;
            }
        }
        return false;
    }
}
