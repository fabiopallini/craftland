package com.craftland.engine.gfx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.craftland.engine.core.Vector2;

public class Particles {
    private List<Texture> textures;
    private List<Particle> particles;
    private Color color;
    private int totalParticles = 0;
    private float Velocity = 0;
    private int Size = 0;
    private int TTL = 0;
    private float AngleVelocity = 0;
    private float AlphaDecrease = 0;

    private boolean startEmitter = false;
    private Vector2 pos;

    public Particles(List<Texture> _textures) {
        pos = new Vector2(0, 0);
        color = new Color(255,255,255,255);
        particles = new ArrayList<>();
        textures = _textures;
    }

    public Particles(Texture _texture) {
        pos = new Vector2(0, 0);
        color = new Color(255,255,255,255);
        particles = new ArrayList<>();
        textures = new ArrayList<>();
        textures.add(_texture);
    }

    public void setAutoGenerate()
    {
        setGenerate(5, textures.get(0).bitmapWidth, 1000, 35, 100, 150f);
    }

    public void setGenerate(int _totalParticles,
                        int _Size, int _TTL,
                        float _Velocity, float _AngleVelocity,
                        float _AlphaDecrease) {
        totalParticles = _totalParticles;
        Velocity = _Velocity;
        Size = _Size;
        TTL = _TTL;
        AngleVelocity = _AngleVelocity;
        AlphaDecrease = _AlphaDecrease;
    }

    public void setSpawn(float x, float y) {
        startEmitter = true;
        pos.X = x;
        pos.Y = y;
    }

    public void setColor(int r, int g, int b){
        color.R = r;
        color.G = g;
        color.B = b;
    }


    public void update(Vector2 position) {
        if(position != null) {
            pos.X = position.X;
            pos.Y = position.Y;
        }
        update();
    }

    public void update(float x, float y) {
        pos.X = x;
        pos.Y = y;
        update();
    }

    private void update()
    {
        if (startEmitter) {
            int total = totalParticles;

            for (int i = 0; i < total; i++) {
                particles.add(generateNewParticle());
                if (i == total - 1) {
                    startEmitter = false;
                }
            }
        }

        for (int particle = 0; particle < particles.size(); particle++) {
            particles.get(particle).update();
            if (particles.get(particle).ttl <= 0) {
                particles.remove(particle);
                particle--;
            }
        }
    }

    public void draw(Screen screen) {
        for (int index = 0; index < particles.size(); index++) {
            particles.get(index).draw(screen);
        }
    }

    private Particle generateNewParticle() {
        Random rand = new Random();
        Texture texture = textures.get(rand.nextInt(textures.size()));
        Vector2 position = new Vector2(pos.X, pos.Y);

        Vector2 velocity = new Vector2(
                Velocity * (float) (rand.nextDouble() * 2 - 1),
                Velocity * (float) (rand.nextDouble() * 2 - 1));

        int size = Size + rand.nextInt(Size * 2);
        int ttl = TTL;
        float angleVelocity = AngleVelocity;
        float alphaDecrease = AlphaDecrease + (rand.nextFloat() / 2);

        return new Particle(texture, position, velocity, size, ttl,
                angleVelocity, alphaDecrease, color);
    }
}
