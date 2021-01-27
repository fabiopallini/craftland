package com.craftland.engine.gfx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import com.craftland.engine.core.Render;

public class Screen {
    private FloatBuffer vertexBuffer;
    private FloatBuffer uvBuffer;
    private float vertices[] = {
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f
    };
    private float[] uv = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f
    };

    public Screen() {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        byteBuffer = ByteBuffer.allocateDirect(uv.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        uvBuffer = byteBuffer.asFloatBuffer();
        uvBuffer.put(uv);
        uvBuffer.position(0);
    }

    public void draw(Texture texture, float x, float y) {
        draw(texture,
                x, y,
                texture.bitmapWidth,
                texture.bitmapHeight,
                0, 0,
                texture.bitmapWidth,
                texture.bitmapHeight,
                0,
                texture.color
        );
    }

    public void draw(Texture texture, float x, float y, int w, int h) {
        draw(texture,
                x, y,
                w,
                h,
                0, 0,
                texture.bitmapWidth,
                texture.bitmapHeight,
                0,
                texture.color
        );
    }

    public void draw(Texture texture, float x, float y, int w, int h, float rotation) {
        draw(texture,
                x, y,
                w,
                h,
                0, 0,
                texture.bitmapWidth,
                texture.bitmapHeight,
                rotation,
                texture.color
        );
    }

    public void draw(Texture texture, float x, float y,
                       int w, int h, float rotation,
                       Color color) {
        draw(texture,
                x, y,
                w,
                h,
                0, 0,
                texture.bitmapWidth,
                texture.bitmapHeight,
                rotation,
                color
        );
    }

    public void draw(Texture texture,
                       float posX, float posY,
                       float width, float height,
                       float x, float y,
                       float w, float h, float rotation, Color color) {

        uv[0] = x / texture.bitmapWidth;
        uv[1] = (y + h) / texture.bitmapHeight;
        uv[2] = x / texture.bitmapWidth;
        uv[3] = y / texture.bitmapHeight;
        uv[4] = (x + w) / texture.bitmapWidth;
        uv[5] = y / texture.bitmapHeight;
        uv[6] = (x + w) / texture.bitmapWidth;
        uv[7] = (y + h) / texture.bitmapHeight;

        uvBuffer.put(uv);
        uvBuffer.position(0);

        //render
        Render.gl.glLoadIdentity();
        Render.gl.glColor4f((1.0f / 255.0f) * color.R,
                (1.0f / 255.0f) * color.G,
                (1.0f / 255.0f) * color.B,
                (1.0f / 255.0f) * color.A);
        Render.gl.glTranslatef(posX + 0.5f * width,
                posY + 0.5f * height, 0);
        Render.gl.glScalef(width, height, 0.0f);
        Render.gl.glRotatef(rotation, 0, 0, 1.0f);
        Render.gl.glTranslatef(-0.5f, -0.5f, 0.0f);
        Render.gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.id[0]);
        Render.gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        Render.gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, uvBuffer.position(0));
        Render.gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        Render.gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        Render.gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, vertices.length / 3);
        Render.gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        Render.gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
}
