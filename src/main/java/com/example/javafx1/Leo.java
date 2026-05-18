package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Leo extends Sprite {

    private Image spriteSheet;
    private int currentFrame = 0;
    private long lastFrameChange = 0;
    private final long FRAME_DURATION = 5000;

    // Costruttore: inizializza il bambino con immagine e dimensioni
    public Leo(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.health = 1000;
        this.maxHealth = 1000;

        try {
            spriteSheet = new Image(getClass().getResourceAsStream("/img/Leo.png"));
            System.out.println("Sprite Leo caricato!");
        } catch (Exception e) {
            System.out.println("Errore caricamento Leo: " + e.getMessage());
        }
    }

    // Disegna il bambino sulla canvas usando il frame corrente
    @Override
    public void draw(GraphicsContext gc) {
        if (spriteSheet != null) {
            double frameWidth = spriteSheet.getWidth() / 3;
            double frameHeight = spriteSheet.getHeight() / 4;
            gc.drawImage(spriteSheet, currentFrame * frameWidth - 30, 0, frameWidth, frameHeight, x, y, dimensionX, dimensionY);
        }
    }

    // Aggiorna il frame corrente ogni 5 secondi
    @Override
    public void update(double deltaTime) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameChange >= FRAME_DURATION) {
            currentFrame++;
            if (currentFrame >= 3) {
                currentFrame = 0;
            }
            lastFrameChange = currentTime;
        }
    }
}