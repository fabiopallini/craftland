package com.craftland.engine.core;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class MusicPlayer
{
	private boolean played = false;
	private MediaPlayer mp = new MediaPlayer();
	
	public MusicPlayer(String Path) {
		try {
			AssetFileDescriptor descriptor;
			descriptor = Render.context.getAssets().openFd(Path);
			mp.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void play(boolean loop) {
		if (!played) {
			if (loop) {
				mp.setLooping(loop);
				mp.setVolume(1.0f, 1.0f);
			}
			try {
				mp.prepare();
				mp.start();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			played = true;
		}
	}
	
	public void stop() {
		if (played) {
			mp.stop();
			played = false;
		}
	}
}
