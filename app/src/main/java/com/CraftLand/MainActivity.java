package com.craftland;

import android.os.Bundle;
import com.craftland.engine.core.Render;
import com.craftland.rpg.core.Game;

public class MainActivity extends Render {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void load() {
        game = new Game();
    }

    @Override
    public void update() {
        game.update();
    }

    @Override
    public void draw() {
        game.draw();
    }

    @Override
    public void draw_interface() {
        game.draw_interface();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(game.memoryCard != null)
            game.memoryCard.save();
        if(glView != null)
            glView.onPause();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(glView != null)
            glView.onResume();
    }

    @Override
    public void onBackPressed() {
        //nothing to do
    }
}
