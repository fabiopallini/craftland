package com.craftland.engine.core;

import android.graphics.Color;
import android.graphics.Rect;

public class Collision {

    public static boolean pixel(Object a, Object b){
        Rect bounds1 = new Rect((int)a.position.X, (int)a.position.Y, a.right(), a.bottom());
        Rect bounds2 = new Rect((int)b.position.X, (int)b.position.Y, b.right(), b.bottom());

        if( Rect.intersects(bounds1, bounds2) ){
            Rect collisionBounds = getCollisionBounds(bounds1, bounds2);
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int sprite1Pixel = getBitmapPixel(a, i, j);
                    int sprite2Pixel = getBitmapPixel(b, i, j);
                    if( isFilled(sprite1Pixel) && isFilled(sprite2Pixel)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static int getBitmapPixel(Object object, int i, int j) {
        return object.texture.bitmap.getPixel(i-(int)object.position.X, j-(int)object.position.Y);
    }

    private static Rect getCollisionBounds(Rect rect1, Rect rect2) {
        int left = (int) Math.max(rect1.left, rect2.left);
        int top = (int) Math.max(rect1.top, rect2.top);
        int right = (int) Math.min(rect1.right, rect2.right);
        int bottom = (int) Math.min(rect1.bottom, rect2.bottom);
        return new Rect(left, top, right, bottom);
    }

    private static boolean isFilled(int pixel) {
        return pixel != Color.TRANSPARENT;
    }
}
