package com.craftland.rpg;

public class Tile
{
    public static int MapHeight = 0;
    public static int MapWidth = 0;
    public final static int SIZE = 32;

    public final static byte Grass = -128;
    public final static byte Tree_GreenArea = -126;
    public final static byte GrassDry = -125;
    public final static byte Water = -124;
    public final static byte Terrain_Desert = -123;
    public final static byte Cactus = -122;
    public final static byte Tree_Snow = -121;
    public final static byte Compost_0 = -119;
    public final static byte Compost_1 = -118;
    public final static byte Compost_2 = -117;
    public final static byte Compost_3 = -116;
    public final static byte Lava = -114;
    public final static byte Lava_header = -113;
    public final static byte WhiteFlower = -112;
    public final static byte Ground = -99;
    public final static byte Tree_Canyon = -94;
    public final static byte Water_header = -93;
    public final static byte Rocks = -91;
    public final static byte Wall_1 = -90;
    public final static byte Wall_2 = -89;
    public final static byte Wall_3 = -88;
    public final static byte Wall_4 = -87;
    public final static byte Wall_5 = -86;
    public final static byte Wall_6 = -85;
    public final static byte Wall_7 = -84;
    public final static byte Wall_8 = -83;
    public final static byte Wall_9 = -82;
    public final static byte Tree_Forest = -80;
    public final static byte Pier = -76;
    public final static byte Tree_Desert = -75;
    public final static byte Door_Wood = -72;
    public final static byte Door_Iron = -71;
    public final static byte Door_Gold = -70;
    public final static byte ChestWood = -69;
    public final static byte Grid = -68;
    public final static byte TombStone = -67;
    public final static byte CampFire = -66;
    public final static byte Exit = 127;

    public final static byte _collision[] = {
            Tile.Wall_1,
            Tile.Wall_2,
            Tile.Wall_3,
            Tile.Wall_4,
            Tile.Wall_5,
            Tile.Wall_6,
            Tile.Wall_7,
            Tile.Wall_8,
            Tile.Wall_9,
            Tile.Tree_Snow,
            Tile.Tree_Forest,
            Tile.Tree_Desert,
            Tile.Tree_GreenArea,
            Tile.Tree_Canyon,
            Tile.Water,
            Tile.Cactus,
            Tile.Water_header,
            Tile.Lava,
            Tile.Lava_header,
            Tile.Rocks,
            Tile.TombStone
    };
}
