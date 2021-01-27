package com.craftland.engine.gfx;

import java.util.Random;
import com.craftland.engine.core.Render;
import com.craftland.engine.core.Vector2;

class Particle
{
    private Texture texture;
    private Vector2 position = new Vector2(0,0);
    private Vector2 velocity;
    private int size;
    private float angleVelocity;
    private float angle = 0.0f;
    private float alphaDecrease = 0;
    private Color color;
    private Random rand = new Random();

    public int ttl;

    public Particle(Texture texture, Vector2 pos, Vector2 velocity,
                    int size, int ttl, float angleVelocity, float alphaDecrease, Color color)
    {
        this.texture = texture;
        position = new Vector2(pos.X, pos.Y);
        this.velocity = velocity;
        this.size = size;
        this.ttl = ttl;
        this.angleVelocity = angleVelocity + rand.nextInt(90);
        this.alphaDecrease = alphaDecrease;
        this.color = new Color(color.R, color.G, color.B, color.A);
    }

    public void update() {
        ttl -= 1000 * (float) Render.timeDelta / 1000;
        color.A -= alphaDecrease * (float) (Render.timeDelta / 1000);

        angle += angleVelocity * (float) Render.timeDelta / 1000;

        position.X += velocity.X * (float) Render.timeDelta / 1000;
        position.Y += velocity.Y * (float) Render.timeDelta / 1000;
    }

    public void draw(Screen screen)
    {
        screen.draw(texture,
                position.X, position.Y,
                size, size, angle, color);
    }
}