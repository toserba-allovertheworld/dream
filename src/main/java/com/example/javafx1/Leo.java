package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Leo extends Sprite {

    private Image spriteSheet; // Spritesheet contenente le 12 frame (3x4)

    // Frame corrente e timing
    private int currentFrame = 0; // Quale frame (0, 1, o 2) sta per essere disegnato

    private long lastFrameChange = 0; // Timestamp dell'ultimo cambio di frame

    // Ogni quanto cambia frame
    private final long FRAME_DURATION = 5000; // 5 secondi tra un frame e l'altro

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
            double frameWidth = spriteSheet.getWidth() / 3; // 3 colonne di frame
            double frameHeight = spriteSheet.getHeight() / 4; // 4 righe di frame
            // Disegna il frame in posizione (row=0, col=currentFrame) con offset di -30 pixel
            gc.drawImage(spriteSheet, currentFrame * frameWidth - 30, 0, frameWidth, frameHeight, x, y, dimensionX, dimensionY);
        }
    }

    // Aggiorna il frame corrente ogni 5 secondi
    @Override
    public void update(double deltaTime) {

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameChange >= FRAME_DURATION) { // Se sono passati 5 secondi
            currentFrame++; // Avanza al frame successivo
            if (currentFrame >= 3) { // Se fuori range (0, 1, 2)
                currentFrame = 0; // Ricomincia dal primo frame (loop)
            }
            lastFrameChange = currentTime; // Salva il nuovo timestamp
        }
    }
}