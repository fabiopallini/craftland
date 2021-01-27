package com.craftland;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/nes.ttf");
        String version = "v"+ BuildConfig.VERSION_NAME;

        Button button_play = findViewById(R.id.buttonPlay);
        button_play.setTypeface(tf);
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        TextView tv_gameVersion = findViewById(R.id.gameVersion);
        tv_gameVersion.setTypeface(tf);
        tv_gameVersion.setText(version);
    }
}
