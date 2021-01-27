package com.craftland.engine.gfx;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;
import com.craftland.engine.core.Render;

public class Texture
{
    public Bitmap bitmap;
    public Color color;
    public int bitmapWidth;
    public int bitmapHeight;
    public int[] id = new int[1];

    public Texture(String path) {
        if (path != null) {
            if (!path.equals("")) {
                try {
                    loadFromAsset(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        color = new Color(255,255,255,255);
    }

    public void delete()
    {
        if(bitmap != null){
            bitmap.recycle();
            bitmap = null;
        }
        Render.gl.glDeleteTextures(1, id, 0);
    }

    public void loadFromAsset(String path) throws IOException
    {
        AssetManager assetManager = Render.context.getAssets();
        InputStream istr = assetManager.open(path);
        bitmap = BitmapFactory.decodeStream(istr);
        bindTexture();
    }

    public void loadFromFile(String filePath)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = BitmapFactory.decodeFile(filePath, options);
        bindTexture();
    }

    private void bindTexture() {
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        boolean powerOfTwo = false;
        for (int n = 2; n < 2048 * 2; n *= 2) {
            if (bitmapWidth == n && bitmapHeight == n) {
                powerOfTwo = true;
                break;
            }
        }
        if (powerOfTwo) {
            Render.gl.glGenTextures(1, id, 0);
            Render.gl.glBindTexture(GL10.GL_TEXTURE_2D, id[0]);
            Render.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            Render.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
            Render.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
            Render.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE );
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            //bitmap.recycle();
        } else {
            System.out.println("ERROR->BITMAP NOT POWER OF 2 OR LARGER THAN 2048");
        }
    }

    public void setPixel(Texture _texture, float posX, float posY, int width, int height)
    {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                bitmap.setPixel(x, y, _texture.bitmap.getPixel((int)posX+x, (int)posY+y));
                //bitmap.setPixel(x, y, android.graphics.GColor.rgb(255, 0, 0));
            }
        }
        bindTexture();
    }

    public int[] getPixel(int x, int y)
    {
        int[] color = new int[3];
        if(x >= 0 && x < bitmapWidth && y >= 0 && y < bitmapHeight) {
            int pixel = bitmap.getPixel(x, y);
            color[0] = android.graphics.Color.red(pixel);
            color[1] = android.graphics.Color.green(pixel);
            color[2] = android.graphics.Color.blue(pixel);
        }
        else
        {
            color[0] = 0;
            color[1] = 0;
            color[2] = 0;
        }
        return color;
    }
}
