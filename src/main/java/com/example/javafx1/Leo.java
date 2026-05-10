package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class Leo {

    // Spritesheet
    private Image spritesheet;
    private double x;
    private double y;

    // *** AGGIUNTO *** Scala di Leo (2.0 = doppia dimensione, 0.5 = metà)
    private double scala = 3.0;

    // Animazione
    private int posaAttuale = 0;        // 0-11 (12 pose totali)
    private double tempoAccumulato = 0;
    private static final double INTERVALLO = 10.0;  // Cambia posa ogni 10 secondi

    // Dimensioni frame
    private static final int FRAME_WIDTH = 32;
    private static final int FRAME_HEIGHT = 32;
    private static final int COLS = 3;  // 3 colonne
    private static final int ROWS = 4;  // 4 righe

    /**
     * Crea Leo
     * @param pathSpritesheet Il percorso al file dello spritesheet
     * @param x Posizione X
     * @param y Posizione Y
     */
    public Leo(String pathSpritesheet, double x, double y) {
        try {
            this.spritesheet = new Image(pathSpritesheet);
            this.x = x;
            this.y = y;
        } catch (Exception e) {
            System.err.println("Errore caricamento Leo: " + e.getMessage());
        }
    }

    // *** AGGIUNTO *** Costruttore con scala personalizzata
    public Leo(String pathSpritesheet, double x, double y, double scala) {
        this(pathSpritesheet, x, y);
        this.scala = scala;
    }

    /**
     * Aggiorna l'animazione di Leo
     */
    public void update(double deltaTime) {
        tempoAccumulato += deltaTime;

        if (tempoAccumulato >= INTERVALLO) {
            tempoAccumulato = 0;
            posaAttuale = (posaAttuale + 1) % (ROWS * COLS);
        }
    }

    /**
     * Disegna Leo sullo schermo
     */
    public void draw(GraphicsContext gc) {
        if (spritesheet == null) return;

        // Calcola riga e colonna
        int riga = posaAttuale / COLS;
        int colonna = posaAttuale % COLS;

        // Posizione del frame nello spritesheet
        int spriteX = colonna * FRAME_WIDTH;
        int spriteY = riga * FRAME_HEIGHT;

        // Estrai il frame
        WritableImage frame = estraiFrame(spriteX, spriteY);

        // *** MODIFICATO *** Disegna con scala (dimensioni originali * scala)
        double drawWidth = FRAME_WIDTH * scala;
        double drawHeight = FRAME_HEIGHT * scala;
        gc.drawImage(frame, x, y, drawWidth, drawHeight);
    }

    /**
     * Estrae un frame dallo spritesheet
     */
    private WritableImage estraiFrame(int startX, int startY) {
        WritableImage frame = new WritableImage(FRAME_WIDTH, FRAME_HEIGHT);
        PixelReader reader = spritesheet.getPixelReader();

        for (int y = 0; y < FRAME_HEIGHT; y++) {
            for (int x = 0; x < FRAME_WIDTH; x++) {
                int argb = reader.getArgb(startX + x, startY + y);
                frame.getPixelWriter().setArgb(x, y, argb);
            }
        }

        return frame;
    }

    public void setScala(double scala) {
        this.scala = scala;
    }

    public double getScala() {
        return scala;
    }
}