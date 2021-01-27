package com.craftland.rpg.core;

import com.craftland.engine.core.Render;
import com.craftland.engine.gfx.TileMap;
import com.craftland.rpg.Tile;
import com.craftland.rpg.entity.Player;

public class WorldMap extends TileMap
{
    public WorldMap(String tileset, int tileSize, byte data[][]){
        super(tileset, tileSize, data);
    }

    private void addMeshVertices_reverse(int tile, int x, int y){
        meshGrid.addReverse(
                tiles.get(tile).X,
                tiles.get(tile).Y,
                getTileSize(), getTileSize(),
                x * getTileSize(),
                y * getTileSize(),
                getTileSize(), getTileSize());
    }

    private void addMeshVertices_forward(int tile, int x, int y){
        meshGrid.addForward(
                tiles.get(tile).X,
                tiles.get(tile).Y,
                getTileSize(), getTileSize(),
                x * getTileSize(),
                y * getTileSize(),
                getTileSize(), getTileSize());
    }

    public void update(Player player) {
        int y = player.bottom() / Tile.SIZE;
        int x = player.centerX() / Tile.SIZE;
        if (y < Tile.MapHeight && x < Tile.MapWidth &&
            y >= 0 && x >= 0) {
            meshGrid.flushMatrix();
        }
    }

    @Override
    public void draw() {
        if (array != null) {
            int posX = ((int) Render.camera.X / getTileSize()) - meshBorderSize;
            int posY = ((int) Render.camera.Y / getTileSize()) - meshBorderSize;
            int posW = (int) (Render.camera.X / getTileSize()) + (Render.zoom_width / getTileSize()) + meshBorderSize;
            int posH = (int) (Render.camera.Y / getTileSize()) + (Render.zoom_height / getTileSize()) + meshBorderSize;

            checkFlushMatrix(posX, posY, posW, posH);

            if (!meshGrid.getMatrixReady()) {
                byte i = 1;
                for (int y = posY; y < posH; y++) {
                    switch (i) {
                        case 0:
                            for (int x = posW; x >= posX; x--) {
                                if (y >= 0 && y < getArrayHeight() && x >= 0 && x < getArrayWidth()) {
                                    addMeshVertices_reverse(array[y][x] + 128, x, y);
                                }
                            }
                            i++;
                            break;
                        case 1:
                            for (int x = posX; x < posW; x++) {
                                if (y >= 0 && y < getArrayHeight() && x >= 0 && x < getArrayWidth()) {
                                    addMeshVertices_forward(array[y][x] + 128, x, y);
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
}
