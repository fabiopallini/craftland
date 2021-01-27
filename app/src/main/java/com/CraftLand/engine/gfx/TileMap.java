package com.craftland.engine.gfx;

import java.util.ArrayList;
import java.util.List;
import com.craftland.engine.core.Render;
import com.craftland.engine.core.Vector2;

public class TileMap
{
    private int arrayWidth;
    private int arrayHeight;
    private int tileSize;
    private float prevX;
    private float prevY;
    private float prevW;
    private float prevH;

    public final int meshBorderSize = 10;
    public byte array[][];
    public MeshGrid meshGrid;
    public List<Vector2>tiles;
    public int getTileSize(){return tileSize;}
    public int getArrayWidth(){
        return arrayWidth;
    }
    public int getArrayHeight(){return arrayHeight;}

    public TileMap(String tileset, int tileSize, byte data[][])
    {
        setArray(data);
        tiles = new ArrayList<>();
        meshGrid = new MeshGrid(tileset);
        Texture textureTileset = new Texture(tileset);
        this.tileSize = tileSize;
        int tilesetHeight = textureTileset.bitmapHeight / tileSize;
        int tilesetWidth = textureTileset.bitmapWidth / tileSize;
        for(int y = 0; y < tilesetHeight; y++)
        {
            for(int x = 0; x < tilesetWidth; x++)
            {
                tiles.add(new Vector2(x*tileSize, y*tileSize));
            }
        }
        prevX = 0;
        prevY = 0;
        prevW = 0;
        prevH = 0;
    }

    public void draw() {
        if (array != null) {
            int posX = ((int) Render.camera.X / tileSize) - meshBorderSize;
            int posY = ((int) Render.camera.Y / tileSize) - meshBorderSize;
            int posW = (int) (Render.camera.X / tileSize) + (Render.zoom_width / tileSize) + meshBorderSize;
            int posH = (int) (Render.camera.Y / tileSize) + (Render.zoom_height / tileSize) + meshBorderSize;

            checkFlushMatrix(posX, posY, posW, posH);

            if (!meshGrid.getMatrixReady()) {
                byte i = 1;
                for (int y = posY; y < posH; y++) {
                    switch (i) {
                        case 0:
                            for (int x = posW; x >= posX; x--) {
                                if (y >= 0 && y < arrayHeight && x >= 0 && x < arrayWidth) {
                                    meshGrid.addReverse(tiles.get(array[y][x] + 128).X,
                                            tiles.get(array[y][x] + 128).Y, tileSize, tileSize,
                                            x * tileSize, y * tileSize, tileSize, tileSize);
                                }
                            }
                            i++;
                            break;
                        case 1:
                            for (int x = posX; x < posW; x++) {
                                if (y >= 0 && y < arrayHeight && x >= 0 && x < arrayWidth) {
                                    meshGrid.addForward(tiles.get(array[y][x] + 128).X,
                                            tiles.get(array[y][x] + 128).Y, tileSize, tileSize,
                                            x * tileSize, y * tileSize, tileSize, tileSize);
                                }
                            }
                            i--;
                            break;
                        default:
                            break;
                    }
                }
            }
            meshGrid.pushMatrix();
            meshGrid.draw();
        }
    }

    public byte getTileValue(int x, int y) {
        byte result = 0;
        if (y >= 0 && y < arrayHeight &&
                x >= 0 && x < arrayWidth) {
            result = array[y][x];
        }
        return result;
    }

    public void setTileValue(int x, int y, byte value) {
        if (y >= 0 && y < arrayHeight &&
                x >= 0 && x < arrayWidth) {
            array[y][x] = value;
            meshGrid.flushMatrix();
        }
    }

    public void setArray(byte data[][]){
        arrayHeight = data.length;
        arrayWidth = data[0].length;
        array = new byte[arrayHeight][arrayWidth];
        for(int y = 0; y < arrayHeight; y++)
        {
            for(int x = 0; x < arrayWidth; x++)
            {
                array[y][x] = data[y][x];
            }
        }
    }

    public void checkFlushMatrix(int x, int y, int w, int h) {
        if(x <= prevX || y <= prevY ||
                w >= prevW || h >= prevH){
            prevX = x - (meshBorderSize / 2);
            prevY = y - (meshBorderSize / 2);
            prevW = w + (meshBorderSize / 2);
            prevH = h + (meshBorderSize / 2);
            meshGrid.flushMatrix();
        }
    }
}
