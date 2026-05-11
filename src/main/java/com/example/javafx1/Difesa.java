package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Difesa extends Sprite {

    protected int cost;
    protected Image spriteSheet;
    protected int currentFrame = 0;
    protected int totalFrames = 1;
    protected long lastFrameChange = 0;
    protected long frameDuration = 300;

    public Difesa(double x, double y, double width, double height, double health, double damage, int cost) {
        super(x, y, width, height);
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.cost = cost;
    }

    @Override
    public void update(double deltaTime) {
        animate();
    }

    protected void animate() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameChange >= frameDuration) {
            currentFrame++;
            if (currentFrame >= totalFrames) {
                currentFrame = 0;
            }
            lastFrameChange = currentTime;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (spriteSheet != null) {
            double frameWidth = spriteSheet.getWidth() / totalFrames;
            double frameHeight = spriteSheet.getHeight();
            gc.drawImage(spriteSheet, currentFrame * frameWidth, 0, frameWidth, frameHeight, x, y, dimensionX, dimensionY);
        }
    }

    public int getCost() {
        return cost;
    }
}