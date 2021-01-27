package com.craftland.rpg;

import java.util.Random;
import com.craftland.rpg.core.Dig;
import com.craftland.rpg.core.Game;
import com.craftland.rpg.entity.ItemObject;
import com.craftland.rpg.entity.Mob.Bomb;
import com.craftland.rpg.entity.Boomerang;

public class UseItem
{
    private boolean init;
    private Random random;
    private String itemName;
    private Dig dig;

    public UseItem()
    {
        init = false;
        random = new Random();
    }

    public void item(String itemName) {
        this.itemName = itemName;
        Game.farm.farming(itemName);
        if(!init){
            init = true;
            dig = new Dig();
        }
        switch (itemName) {
            case Resource.Pickaxe:
                dig(getTileBlock());
                break;
            case Resource.Potion:
                Game.player.HP += 50;
                if(Game.player.HP > Game.player.HP_MAX)
                    Game.player.HP = Game.player.HP_MAX;
                useItem();
                break;
            case Resource.Bottle:
                if (getTileBlock() == Tile.Water) {
                    addItemAuto(Resource.Water);
                    useItem();
                }
                break;
            case Resource.Key:
                if(getTileBlock() == Tile.Door_Wood){
                    setTileBlock(Tile.Ground);
                    useItem();
                }
                if(getTileBlock() == Tile.ChestWood){
                    setTileBlock(Tile.Ground);
                    addItem(Resource.Potion, 3);
                    useItem();
                }
                break;
            case Resource.Skull:
                if(getTileBlock() == Tile.TombStone){
                    Game.stage.addObject(Game.makeMob(Resource.NM_SkeletonKing, Game.player.centerX() - 32, Game.player.centerY() - 128));
                    useItem();
                }
                break;
            case Resource.Chip:
                if(getTileBlock() == Tile.TombStone)
                {
                    Game.stage.addObject(Game.makeMob(Resource.NM_MotherGlobe, Game.player.centerX() - 32, Game.player.centerY() - 128));
                    useItem();
                }
                break;
            case Resource.Web:
                if(getTileBlock() == Tile.TombStone){
                    Game.stage.addObject(Game.makeMob(Resource.NM_GiantSpider, Game.player.centerX() - 32, Game.player.centerY() - 128));
                    useItem();
                }
                break;
            case Resource.Eye:
                if(getTileBlock() == Tile.TombStone){
                    Game.stage.addObject(Game.makeMob(Resource.NM_BigEye, Game.player.centerX() - 32, Game.player.centerY() - 128));
                    useItem();
                }
                break;
            case Resource.Pier:
                if(getTileBlock() == Tile.Water ||
                        getTileBlock() == Tile.Water_header){
                    setTileBlock(Tile.Pier);
                    useItem();
                }
                break;
            case Resource.CampFire:
                if(getTileBlock() == Tile.Ground)
                {
                    setTileBlock(Tile.CampFire);
                    useItem();
                }
                break;
            case Resource.Boomerang_Wood:
                Game.stage.addObject(new Boomerang(itemName));
                Game.menu.setItemB(null);
                break;
            case Resource.Boomerang_Iron:
                Game.stage.addObject(new Boomerang(itemName));
                Game.menu.setItemB(null);
                break;
            case Resource.Bomb:
                Bomb bomb = new Bomb();
                Game.stage.addObject(bomb);
                useItem();
                break;
            default:
                break;
        }
    }

    private void dig(byte id)
    {
        if(dig.isDigTile(id)) {
            dig.search(id);
            switch (id) {
                case Tile.Grass:
                    addItem(Resource.Grass);
                    switch (random.nextInt(5)) {
                        case 0:
                            addItem(Resource.SeedsTree);
                            break;
                        case 1:
                            addItem(Resource.SeedCarrot);
                            break;
                        default:
                            break;
                    }
                    break;
                case Tile.GrassDry:
                    addItem(Resource.GrassDry);
                    break;
                case Tile.Tree_Snow:
                    addItem(Resource.Wood);
                    addItem(Resource.SnowBall);
                    break;
                case Tile.Tree_Forest:
                    addItem(Resource.Wood);
                    addItem(Resource.SeedsTree);
                    break;
                case Tile.Tree_Desert:
                    addItem(Resource.Wood);
                    addItem(Resource.SeedsTree);
                    break;
                case Tile.Tree_GreenArea:
                    addItem(Resource.Wood);
                    addItem(Resource.SeedsTree);
                    switch (random.nextInt(5))
                    {
                        case 0:
                            addItem(Resource.Apple);
                            break;
                        case 1:
                            addItem(Resource.Cherry);
                            break;
                        default:
                            break;
                    }
                    break;
                case Tile.Tree_Canyon:
                    addItem(Resource.Wood);
                    addItem(Resource.SeedsTree);
                    break;
                case Tile.Water:
                    setTileBlock(Tile.Water);
                    addItemAuto(Resource.Sand);
                    break;
                case Tile.Terrain_Desert:
                    addItem(Resource.Sand);
                    break;
                case Tile.WhiteFlower:
                    addItem(Resource.WhiteFlower);
                    break;
                case Tile.Rocks:
                    setTileBlock(Tile.Ground);
                    addItem(Resource.Stone, 2);
                    break;
                case Tile.Lava:
                    setTileBlock(Tile.Lava);
                    break;
                case Tile.Grid:
                    setTileBlock(Tile.Lava);
                    if(Game.getPlayerFootOn() == Tile.Lava){
                        setTileBlock(Tile.Grid);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void setTileBlock(byte value){
        Game.setTileBlock(value);
    }

    private byte getTileBlock(){
        return Game.getTileBlock();
    }

    private void addItem(String name){
        int x = (Game.getTileX() * Tile.SIZE) + 6;
        int y = (Game.getTileY() * Tile.SIZE) + 6;
        Game.stage.addObject(new ItemObject(name, x, y));
    }

    private void addItem(String name, int qty){
        for(int n = 0; n < qty; n++) {
            int x = (Game.getTileX() * Tile.SIZE) + n*4;
            int y = (Game.getTileY() * Tile.SIZE) + n*4;
            Game.stage.addObject(new ItemObject(name, x, y));
        }
    }

    private void addItemAuto(String name){
        Game.inventory.addItem(name, 1);
    }

    private void useItem(){
        Game.inventory.useItem(itemName);
    }
}
