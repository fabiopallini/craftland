package com.craftland.rpg.ui;

import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;
import com.craftland.engine.core.Vector2;
import com.craftland.engine.gfx.Screen;
import com.craftland.engine.gfx.Texture;
import com.craftland.engine.core.Render;
import com.craftland.engine.input.Button;
import com.craftland.rpg.Resource;
import com.craftland.rpg.Tile;
import com.craftland.rpg.core.Game;
import com.craftland.rpg.entity.Weapon;

public class Menu {
    public Inventory inventory;
    private Button buttonHelp;
    private Button buttonInventory;
    private Texture texture_backPackOpen;
    private Texture texture_backPackClose;
    private Button buttonCraft;
    private Texture texture_saw;
    private Texture texture_saw90;
    private Texture texture_heart;
    private int viewType;
    private final int type_invetory = 1;
    private final int type_craft = 2;
    private List<BoxButton> boxButtons;
    private int index = 0;
    private Item itemA;
    private Item itemB;
    private Craft craft;
    private Vector2 playerPosition;
    private boolean status_dialogOpen = false;
    private Button button_arrowTop;
    private Button button_arrowDown;
    private Texture texture_background;

    public Menu() {
        texture_heart = new Texture(Resource.assets_Gui + "heart.png");
        buttonHelp = new Button(Resource.assets_Gui + "icon_tutorial.png", 5, 5);
        buttonCraft = new Button(Resource.assets_Gui + "saw.png", 5, 40);
        texture_saw = new Texture(Resource.assets_Gui + "saw.png");
        texture_saw90 = new Texture(Resource.assets_Gui + "saw90.png");
        texture_backPackOpen = new Texture(Resource.assets_Gui + "backPackOpen.png");
        texture_backPackClose = new Texture(Resource.assets_Gui + "backPackClose.png");
        buttonInventory = new Button(Resource.assets_Gui + "backPackClose.png", Render.zoom_width - 33, 50);
        texture_background = new Texture(Resource.assets_Gui + "window.png");
        viewType = 0;
        boxButtons = new ArrayList<>();
        inventory = new Inventory(null);
        craft = new Craft(inventory);
        itemA = null;
        itemB = null;
        button_arrowTop = new Button(Resource.assets_Gui + "arrowTop.png", 7, Render.zoom_height - 90);
        button_arrowDown = new Button(Resource.assets_Gui + "arrowDown.png", 7, Render.zoom_height - 40);
        playerPosition = new Vector2(0,0);
    }

    public void update() {
        playerPosition.X = Game.player.centerX() / Tile.SIZE;
        playerPosition.Y = Game.player.centerY() / Tile.SIZE;
        if(!status_dialogOpen) {
            if (Render.pause)
            {
                if (buttonInventory.isUp()) {
                    if (viewType == type_invetory)
                        closeInventory();
                }
                if (buttonCraft.isUp()) {
                    if (viewType == type_craft) {
                        closeCraft();
                        openInventory();
                    }
                    else
                        openCraft();
                }
            }
            else
            {
                if (buttonInventory.isUp())
                    openInventory();
            }
        }

        if (viewType == type_invetory) {
            for (int n = 0; n < boxButtons.size(); n++) {
                if (boxButtons.get(n).isUp() && !status_dialogOpen) {
                    if (boxButtons.get(n).getItem().getType() == Resource.WEAPON) {
                        itemA = new Item(boxButtons.get(n).getItem().getName());
                        Game.player.weapon = new Weapon(itemA.getName());
                        closeInventory();
                        break;
                    }
                    if(boxButtons.get(n).getItem().getType() == Resource.ARMOR){
                        Game.player.setEquip(boxButtons.get(n).getItem().getName());
                        closeInventory();
                        break;
                    }
                    if (boxButtons.get(n).getItem().getType() == Resource.ITEM) {
                        itemB = boxButtons.get(n).getItem();
                        closeInventory();
                        break;
                    }
                }

                if (buttonCraft.isUp())
                    openCraft();
                if(buttonHelp.isUp())
                    showCraftingBook();

                //if (boxButtons.get(n).isLongClick()) {
                    //index = n;
                    //status_dialogOpen = true;
                     /*public void onOK() {
                            if (boxButtons.size() > 0 && index < boxButtons.size() && index != -1) {
                                String name = boxButtons.get(index).getItem().getName();
                                if (itemA != null && itemA.getName().equals(name)) {
                                    itemA = null;
                                    Game.player.weapon = null;
                                }
                                inventory.removeItem_All(name);
                                if (itemB != null && itemB.getName().equals(name)) {
                                    itemB = null;
                                }
                                boxButtons.remove(index);
                                index = -1;
                                closeInventory();
                                openInventory();
                                status_dialogOpen = false;
                            }
                        }*/
                    //break;
                //}
            }
        }
        if (viewType == type_craft) {
            for (int n = 0; n < boxButtons.size(); n++) {
                if (boxButtons.get(n).isUp() && !craft.pots.isEmpty()) {
                    String itemToCraft = boxButtons.get(n).getItem().getName();
                    if(craft.build(itemToCraft, inventory)) {
                        closeCraft();
                        openCraft();
                    }else{
                        closeCraft();
                    }
                }
            }
        }

        if(viewType == type_invetory || viewType == type_craft){
            if(boxButtons.size() > 0) {
                if (button_arrowTop.isDown()) {
                    if (boxButtons.get(0).getPosY() < 40) {
                        for (int n = 0; n < boxButtons.size(); n++) {
                            boxButtons.get(n).movePosY(+250);
                        }
                    }
                }
                if (button_arrowDown.isDown()) {
                    if (boxButtons.get(boxButtons.size() - 1).getPosY() > Render.zoom_height - 100) {
                        for (int n = 0; n < boxButtons.size(); n++) {
                            boxButtons.get(n).movePosY(-250);
                        }
                    }
                }
            }
        }
    }

    public void draw(Screen screen) {
        for(float n = 0; n < Game.player.HP; n++)
            screen.draw(texture_heart, ((texture_heart.bitmapWidth / 1.5f) * n), 0);

        if (viewType == type_invetory || viewType == type_craft)
            screen.draw(texture_background, 0, 0);

        buttonInventory.draw(screen);

        if (viewType != type_invetory && viewType != type_craft) {
            if (Game.player.weapon != null) {
                if (Game.player.weapon.isRanged() && Game.player.weapon.ammoCount > 0) {
                    Game.gui.draw_string(screen, "" + (Game.player.weapon.ammoCount), Render.zoom_width - 105, Render.zoom_height - 66);
                }
            }
            if (itemA != null)
                itemA.draw(screen, Render.zoom_width - 100, Render.zoom_height - 57);
            if (itemB != null) {
                itemB.draw(screen, Render.zoom_width - 47, Render.zoom_height - 57);
                if (itemB.counter > 1)
                    Game.gui.draw_string(screen, "" + itemB.counter, Render.zoom_width - 52, Render.zoom_height - 66);
            }
        }

        if(viewType == type_invetory || viewType == type_craft) {
            buttonHelp.draw(screen);
            buttonCraft.draw(screen);
        }

        if (viewType == type_invetory)
            menuDraw(screen);

        if (viewType == type_craft) {
            menuDraw(screen);
            if (craft.pots.isEmpty())
                Game.gui.draw_string(screen, "nothing to craft", 60, 20, 12);
        }
    }

    public Item getItemA() {
        return itemA;
    }

    public Item getItemB() {
        return itemB;
    }

    public void setItemA(String itemName) {
        if(itemName != null) {
            itemA = inventory.getItem(itemName);
        }else{
            itemA = null;
        }
    }

    public void setItemB(String itemName) {
        if(itemName != null) {
            itemB = inventory.getItem(itemName);
        }else{
            itemB = null;
        }
    }

    private void menuFiller(List<Item> items) {
        int L = 55;
        int R = 230;
        int x = L;
        int y = 5;
        int space = 50;
        for (int n = 0; n < items.size(); n++) {
            boxButtons.add(new BoxButton(x, y, items.get(n)));
            x += space;
            if (x > R) {
                x = L;
                y += space;
            }
        }
    }

    private void menuDraw(Screen screen) {
        if(!status_dialogOpen) {
            button_arrowTop.draw(screen);
            button_arrowDown.draw(screen);
            for (int n = 0; n < boxButtons.size(); n++) {
                boxButtons.get(n).draw(screen);
            }
        }
    }

    private void openInventory() {
        Render.pause = true;
        buttonInventory.texture = texture_backPackOpen;
        clean();
        viewType = type_invetory;
        menuFiller(inventory.items);
    }

    private void openCraft() {
        buttonCraft.texture = texture_saw90;
        clean();
        viewType = type_craft;
        craft.generateCraftingPots();
        menuFiller(craft.pots);
    }

    private void closeInventory() {
        Render.pause = false;
        buttonInventory.texture = texture_backPackClose;
        viewType = 0;
        clean();
    }

    private void closeCraft() {
        buttonCraft.texture = texture_saw;
        viewType = type_invetory;
        clean();
    }

    private void clean() {
        if (!boxButtons.isEmpty()) {
            boxButtons.clear();
        }
        craft.clean();
    }

    private void showCraftingBook() {
        Render.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Render.pause = true;
                WebView webView = new WebView(Render.activity);
                webView.loadUrl("file:///android_asset/craftingbook/book.html");
                final android.widget.Button button = new android.widget.Button(Render.context);
                button.setText("X");
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                button.setLayoutParams(params);

                RelativeLayout layout = new RelativeLayout(Render.context);
                layout.addView(webView);
                layout.addView(button);

                AlertDialog.Builder builder = new AlertDialog.Builder(Render.activity);
                builder.setView(layout);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        ViewGroup viewGroup = (ViewGroup) button.getParent();
                        if (viewGroup != null) {
                            viewGroup.removeView(button);
                        }
                        Render.pause = false;
                    }
                });
                dialog.show();
            }
        });
    }
}
