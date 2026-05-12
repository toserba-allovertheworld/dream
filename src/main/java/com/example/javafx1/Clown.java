package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;

public class Clown extends Nemico {

    private static Image SPRITE_SHEET;

    static {

        try {

            InputStream stream =
                    Occhio.class.getResourceAsStream("/img/clown.png");

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

    private int currentImage = 0;

    public Clown(
            double x,
            double y,
            double dimensionX,
            double dimensionY,
            Leo leo
    ) {

        super(
                x,
                y,
                dimensionX,
                dimensionY,
                80.0,
                10.0,
                1
        );

        this.targetLeo = leo;

        this.attackSpeed = 2000;
    }

    @Override
    public void draw(GraphicsContext gc) {

        if (SPRITE_SHEET != null) {

            double frameWidth =
                    SPRITE_SHEET.getWidth() / 6.0;

            double frameHeight =
                    SPRITE_SHEET.getHeight() / 4.0;

            double aspectRatio =
                    frameWidth / frameHeight;

            double drawWidth =
                    dimensionX;

            double drawHeight =
                    dimensionX / aspectRatio;

            gc.drawImage(
                    SPRITE_SHEET,
                    currentFrame * frameWidth,
                    0,
                    frameWidth,
                    frameHeight,
                    x,
                    y,
                    drawWidth,
                    drawHeight
            );
        }
    }

    @Override
    public void update(double deltaTime) {

        long currentTime = System.currentTimeMillis();
        if (currentFrame == 5){
            currentFrame = 0;
        }else {
            if (currentImage == 10){
                currentFrame++;
                currentImage = 0;
            }else {
                currentImage++;
            }

        }
        if (!attacking) {

            if (x > stopX) {
                this.x -= 1;
            } else {
                this.x = stopX;
                attacking = true;
                attackStartTime = currentTime;
            }

        } else {

            long elapsed = currentTime - attackStartTime;

            if (elapsed >= 500 && !disappearing) {
                disappearing = true;
                targetLeo.takeDamage(damage);
            }

            if (elapsed >= attackAnimationDuration) {

                alive = false;
            }
        }
    }
}