package com.craftland.rpg.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.RandomAccessFile;
import com.craftland.engine.core.Render;
import com.craftland.rpg.Tile;
import com.craftland.rpg.entity.Player;
import com.craftland.rpg.entity.Weapon;
import com.craftland.rpg.ui.Menu;

public class MemoryCard
{
    private File file;
    private FileOutputStream fos;
    private RandomAccessFile raf;
    private byte[] buffer;
    private byte[][] map;
    private String data;
    private BufferedReader bufferedReader;
    private String lines[];

    public MemoryCard()
    {
    }

    public void save()
    {
        if(Game.player.HP > 0)
            saveCharacter();
    }

    public void load(Player p, Menu m){
        load_Character(p, m);
    }

    public byte[][] loadMap() {
        file = new File(Render.context.getFilesDir(), "map.dat");
        map = new byte[Tile.MapHeight][Tile.MapWidth];
        long offset = 0;
        try {
            raf = new RandomAccessFile(file, "r");
            for (int y = 0; y < Tile.MapHeight; y++) {
                raf.seek(offset);
                offset += Tile.MapWidth;
                buffer = new byte[Tile.MapWidth];
                raf.read(buffer, 0, buffer.length);
                map[y] = buffer;
            }
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private void load_Character(Player player, Menu menu) {
        // load inventory
        file = new File(Render.context.getFilesDir(), "inventory.dat");
        data = "";
        if (file.exists()) {
            try {
                String line;
                bufferedReader = new BufferedReader(new FileReader(file));
                while ((line = bufferedReader.readLine()) != null) {
                    data += line;
                }
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lines = data.split(";");
                for (int i = 0; i < lines.length; i++) {
                    String[] str = lines[i].split("&");
                    if (str.length >= 2) {
                        menu.inventory.addItem(str[0], Integer.parseInt(str[1]));
                    }
                }
            }
        }

        // load character data
        file = new File(Render.context.getFilesDir(), "character.dat");
        data = "";
        if (file.exists()) {
            try {
                String line;
                bufferedReader = new BufferedReader(new FileReader(file));
                while ((line = bufferedReader.readLine()) != null) {
                    data += line;
                }
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lines = data.split(";");
                int n = 0;
                player.HP = Integer.parseInt(lines[n++]);
                player.setEquip(lines[n++]);
                player.setPosInTile(Integer.parseInt(lines[n++]), Integer.parseInt(lines[n++]));
                if (!lines[n].equals("0")) {
                    player.weapon = new Weapon(lines[n]);
                    menu.setItemA(lines[n]);
                }
                n++;
                if (!lines[n].equals("0")) {
                    menu.setItemB(lines[n]);
                }
            }
        }
    }

    private void saveMap() {
        file = new File(Render.context.getFilesDir(), "map.dat");
        file.delete();
        map = Game.getMapArray();
        try {
            fos = new FileOutputStream(file, true);
            for (int y = 0; y < Tile.MapHeight; y++) {
                buffer = map[y];
                fos.write(buffer);
            }
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveCharacter()
    {
        // save intenvory
        file = new File(Render.context.getFilesDir(), "inventory.dat");
        file.delete();
        data = "";
        for(int n = 0; n < Game.inventory.getSize(); n++){
            data += Game.inventory.items.get(n).getName() +
                    "&" + Game.inventory.items.get(n).counter + ";";
        }
        try {
            fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        // save character data
        file = new File(Render.context.getFilesDir(), "character.dat");
        file.delete();
        data = "";
        data += Game.player.HP + ";";
        data += Game.player.getEquip() + ";";
        data += (Game.player.centerX() / Tile.SIZE) + ";";
        data += (Game.player.centerY() / Tile.SIZE) + ";";
        String itemA = "0;";
        String itemB = "0;";
        if(Game.menu.getItemA() != null){
            itemA = Game.menu.getItemA().getName() + ";";
        }
        if(Game.menu.getItemB() != null){
            itemB = Game.menu.getItemB().getName() + ";";
        }
        data += itemA;
        data += itemB;

        try {
            fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
