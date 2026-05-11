package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Teddy extends Difesa {

    private static Image SPRITE_SHEET;

    // DIMENSIONI SPRITESHEET
    private final int ROWS = 4;
    private final int COLS = 6;

    // FRAME ATTUALE
    private int currentFrame = 0;
    private int currentRow = 0;

    // TIMER ANIMAZIONE
    private long lastFrameTime = 0;

    private final long FRAME_DURATION = 200;

    // ANIMAZIONI
    /*
        Riga 0 = idle front
        Riga 1 = back
        Riga 2 = attack
        Riga 3 = shield/idle
     */

    {
        try {
            SPRITE_SHEET = new Image(Teddy.class.getResourceAsStream("/img/teddyBear.png"));
            System.out.println("Spritesheet Peluche caricato!");
        } catch (Exception e) {
            System.out.println("Errore caricamento peluche: " + e.getMessage());
        }
    }

    public Teddy(double x, double y) {
        super(x, y, 110, 110, 250, 20, 25);
        this.attackSpeed = 1000;
    }

    @Override
    public void update(double deltaTime) {
        animate();
    }

    public void animate() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= FRAME_DURATION) {
            currentFrame++;
            if (currentFrame >= COLS) {
                currentFrame = 0;
            }
            lastFrameTime = currentTime;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {

        if (SPRITE_SHEET == null) return;
        double frameWidth = SPRITE_SHEET.getWidth() / COLS;
        double frameHeight = SPRITE_SHEET.getHeight() / ROWS;

        gc.drawImage(SPRITE_SHEET, currentFrame * frameWidth, currentRow * frameHeight, frameWidth, frameHeight, x, y, dimensionX, dimensionY);
    }

    public void attack(Nemico nemico, long currentTime) {
        if (canAttack(currentTime)) {
            nemico.takeDamage(this.damage);
            attackPerformed(currentTime);
            currentRow = 2;
        }
    }

    public void idle() {
        currentRow = 0;
    }
}