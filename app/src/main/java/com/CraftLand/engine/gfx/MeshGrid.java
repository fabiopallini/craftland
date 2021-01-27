package com.craftland.engine.gfx;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;
import com.craftland.engine.core.Render;

public class MeshGrid
{
    private Bitmap bitmap;
    private int bitmapWidth;
    private int bitmapHeight;
    private int[] textureID;
    private List<Float> vboList;
    private List<Float> uvList;
    private float[] vbo;
    private float[] uv;
    private FloatBuffer vertexBuffer;
    private FloatBuffer uvBuffer;
    private boolean matrixReady;
    private int gridWidth;
    private int gridHeight;
    private int tileSize;
    private int index_x;
    private int index_y;
    private byte direction;

    public Color color;
    public boolean getMatrixReady(){
        return matrixReady;
    }

    public MeshGrid(String bitmapPath)
    {
        init(bitmapPath);
    }

    public MeshGrid(String bitmapPath, int w, int h, int tileSize)
    {
        init(bitmapPath);
        gridWidth = w;
        gridHeight = h;
        this.tileSize = tileSize;
        index_x = 0;
        index_y = 0;
        direction = 1;
    }

    public void init(String bitmapPath)
    {
        textureID = new int[1];
        if(bitmapPath != null)
        {
            if (!bitmapPath.equals("")) {
                try {
                    loadFromAsset(bitmapPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        color = new Color(255,255,255,255);
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        vboList = new ArrayList<>();
        uvList = new ArrayList<>();
        matrixReady = false;
    }

    public void add(float sx, float sy, float sw, float sh) {
        if(!matrixReady) {
            if (index_x >= gridWidth || index_x < 0) {
                switch (direction) {
                    case 0:
                        direction++;
                        index_x = 0;
                        break;
                    case 1:
                        direction--;
                        index_x--;
                        break;
                    default:
                        break;
                }
                index_y++;
            }
            switch (direction) {
                case 0:
                    addReverse(sx, sy, sw, sh,
                            index_x * tileSize,
                            index_y * tileSize,
                            tileSize, tileSize);
                    index_x--;
                    break;
                case 1:
                    addForward(sx, sy, sw, sh,
                            index_x * tileSize,
                            index_y * tileSize,
                            tileSize, tileSize);
                    index_x++;
                    break;
                default:
                    break;
            }
        }
    }

    public void addForward(float sx, float sy, float sw, float sh,
                     float x, float y, float w, float h) {
        if(!matrixReady) {
            // vertex buffer
            vboList.add(x);
            vboList.add(y);
            vboList.add(0.0f);

            vboList.add(x);
            vboList.add(y + h);
            vboList.add(0.0f);

            vboList.add(x + w);
            vboList.add(y);
            vboList.add(0.0f);

            vboList.add(x + w);
            vboList.add(y + h);
            vboList.add(0.0f);

            // uv buffer
            sw -= 0.1f;
            sh -= 0.1f;
            uvList.add(sx / bitmapWidth);
            uvList.add(sy / bitmapHeight);

            uvList.add(sx / bitmapWidth);
            uvList.add((sy / bitmapHeight) + (sh / bitmapHeight));

            uvList.add((sx / bitmapWidth) + (sw / bitmapWidth));
            uvList.add(sy / bitmapHeight);

            uvList.add((sx / bitmapWidth) + (sw / bitmapWidth));
            uvList.add((sy / bitmapHeight) + (sh / bitmapHeight));
        }
    }

    public void addReverse(float sx, float sy, float sw, float sh,
                           float x, float y, float w, float h) {
        if(!matrixReady) {
            // vertex buffer
            vboList.add(x + w);
            vboList.add(y);
            vboList.add(0.0f);

            vboList.add(x + w);
            vboList.add(y + h);
            vboList.add(0.0f);

            vboList.add(x);
            vboList.add(y);
            vboList.add(0.0f);

            vboList.add(x);
            vboList.add(y + h);
            vboList.add(0.0f);

            // uv buffer
            sw -= 0.1f;
            sh -= 0.1f;
            uvList.add((sx / bitmapWidth) + (sw / bitmapWidth));
            uvList.add(sy / bitmapHeight);

            uvList.add((sx / bitmapWidth) + (sw / bitmapWidth));
            uvList.add((sy / bitmapHeight) + (sh / bitmapHeight));

            uvList.add(sx / bitmapWidth);
            uvList.add(sy / bitmapHeight);

            uvList.add(sx / bitmapWidth);
            uvList.add((sy / bitmapHeight) + (sh / bitmapHeight));
        }
    }

    public void flushMatrix()
    {
        vboList.clear();
        uvList.clear();
        index_x = 0;
        index_y = 0;
        direction = 1;
        matrixReady = false;
    }

    public void pushMatrix()
    {
        if(!matrixReady) {
            vbo = new float[vboList.size()];
            for (int n = 0; n < vbo.length; n++) {
                vbo[n] = vboList.get(n);
            }
            uv = new float[uvList.size()];
            for (int n = 0; n < uv.length; n++) {
                uv[n] = uvList.get(n);
            }
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vbo.length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            vertexBuffer = byteBuffer.asFloatBuffer();
            vertexBuffer.put(vbo);
            vertexBuffer.position(0);
            byteBuffer = ByteBuffer.allocateDirect(uv.length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            uvBuffer = byteBuffer.asFloatBuffer();
            uvBuffer.put(uv);
            uvBuffer.position(0);
            matrixReady = true;
        }
    }

    public void draw(float posX, float posY)
    {
        if(matrixReady) {
            Render.gl.glLoadIdentity();
            Render.gl.glColor4f((1.0f / 255.0f) * color.R,
                    (1.0f / 255.0f) * color.G,
                    (1.0f / 255.0f) * color.B,
                    (1.0f / 255.0f) * color.A);
            Render.gl.glTranslatef(posX, posY, 0.0f);
            // bind the previously generated texture
            Render.gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID[0]);
            // Point to our vertex buffer
            Render.gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
            Render.gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, uvBuffer.position(0));
            Render.gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            Render.gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            Render.gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vbo.length / 3);
            Render.gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            Render.gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        }
    }

    public void draw()
    {
        if(matrixReady) {
            Render.gl.glLoadIdentity();
            Render.gl.glColor4f((1.0f / 255.0f) * color.R,
                    (1.0f / 255.0f) * color.G,
                    (1.0f / 255.0f) * color.B,
                    (1.0f / 255.0f) * color.A);
            // bind the previously generated texture
            Render.gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID[0]);
            // Point to our vertex buffer
            Render.gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
            Render.gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, uvBuffer.position(0));
            Render.gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            Render.gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            Render.gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vbo.length / 3);
            Render.gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            Render.gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        }
    }

    public void reverseArray(int[] data)
    {
        for(int i = 0; i < data.length / 2; i++)
        {
            int temp = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = temp;
        }
    }

    public void reverseArray(byte[] data)
    {
        for(int i = 0; i < data.length / 2; i++)
        {
            byte temp = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = temp;
        }
    }

    private void loadFromAsset(String path) throws IOException
    {
        AssetManager assetManager = Render.context.getAssets();
        InputStream istr = assetManager.open(path);
        bitmap = BitmapFactory.decodeStream(istr);
        bindTexture();
    }

    private void loadFromFile(String filePath)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = BitmapFactory.decodeFile(filePath, options);
        bindTexture();
    }

    private void bindTexture()
    {
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        boolean powerOfTwo = false;
        for (int n = 2; n < 2048 * 2; n *= 2) {
            if (bitmapWidth == n && bitmapHeight == n) {
                powerOfTwo = true;
                break;
            }
        }
        if(powerOfTwo)
        {
            Render.gl.glGenTextures(1, textureID, 0);
            Render.gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID[0]);
            Render.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            Render.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
        }
        else
        {
            System.out.println("ERROR->BITMAP NOT POWER OF 2 OR LARGER THAN 2048");
        }
    }
}
