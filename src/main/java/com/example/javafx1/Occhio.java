package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.InputStream;

public class Occhio extends Nemico {

    private static Image SPRITE_SHEET;

    static {
        try {
            InputStream stream = Occhio.class.getResourceAsStream("/img/occhio.png");
            if (stream != null) {
                SPRITE_SHEET = new Image(stream);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Leo targetLeo;
    private final double stopX = 510;
    private boolean attacking = false;
    private boolean disappearing = false;
    private long attackStartTime = 0;
    private final long attackAnimationDuration = 1200;
    private int currentFrame = 0;

    // Costruttore: inizializza nemico Occhio con stats specifiche
    public Occhio(double x, double y, double dimensionX, double dimensionY, Leo leo) {
        super(x, y, dimensionX, dimensionY, 80.0, 18.0, 1);
        this.targetLeo = leo;
        this.attackSpeed = 2000;
    }

    // Disegna il nemico Occhio usando il frame corrente dello spritesheet
    @Override
    public void draw(GraphicsContext gc) {
        if (SPRITE_SHEET != null) {
            double frameWidth = SPRITE_SHEET.getWidth() / 3.0;
            double frameHeight = SPRITE_SHEET.getHeight();
            double aspectRatio = frameWidth / frameHeight;
            double drawWidth = dimensionX;
            double drawHeight = dimensionX / aspectRatio;
            gc.drawImage(SPRITE_SHEET, currentFrame * frameWidth, 0, frameWidth, frameHeight, x, y, drawWidth, drawHeight);
        }
    }

    // Aggiorna posizione e stato del nemico (movimento, attacco, scomparsa)
    @Override
    public void update(double deltaTime) {
        long currentTime = System.currentTimeMillis();

        if (!attacking) {
            if (blockedByDifesa) {
                return;
            }
            if (x > stopX) {
                this.x -= 1;
                this.y += Math.sin(currentTime * 0.005) * 0.1;
            } else {
                this.x = stopX;
                attacking = true;
                attackStartTime = currentTime;
                currentFrame = 1;
            }
        } else {
            long elapsed = currentTime - attackStartTime;
            if (elapsed >= 500 && !disappearing) {
                currentFrame = 2;
                disappearing = true;
                targetLeo.takeDamage(damage);
            }
            if (elapsed >= attackAnimationDuration) {
                alive = false;
            }
        }
    }
}