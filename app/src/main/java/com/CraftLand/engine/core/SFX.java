package com.craftland.engine.core;

import java.io.IOException;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SFX
{
	private SoundPool pool;
	
	public SFX()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			AudioAttributes attributes = new AudioAttributes.Builder()
					.setUsage(AudioAttributes.USAGE_GAME)
					.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
					.build();
			pool = new SoundPool.Builder()
					.setMaxStreams(4)
					.setAudioAttributes(attributes)
					.build();
		} else {
			pool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		}
	}

	public Sound load(String name)
	{
		Sound s = null;
		try
		{
			s = new Sound(pool.load(Render.context.getAssets().openFd("game_assets/sfx/" + name), 1));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

    public void play(int id){
        pool.play(id, 1, 1, 1, 0, 1f);
    }
}
