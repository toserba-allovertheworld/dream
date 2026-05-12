package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;

public class Ombra extends Nemico{


    private static Image SPRITE_SHEET;
    private final int TOTAL_COLS = 5; // Numero di sprite per riga
    private final int TOTAL_ROWS = 6; // Numero di righe totali

    static {

        try {
            InputStream stream = Ombra.class.getResourceAsStream("/img/ombra.png");
            if (stream != null) {
                SPRITE_SHEET = new Image(stream);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Leo targetLeo;
    private final double stopX = 540;
    private boolean attacking = false;
    private boolean disappearing = false;
    private long attackStartTime = 0;
    private final long attackAnimationDuration = 1200;

    private int currentFrame = 0;

    public Ombra(double x, double y, double dimensionX, double dimensionY, Leo leo) {
        super(x, y, dimensionX, dimensionY, 80.0, 10.0, 1.5);
        this.targetLeo = leo;
        this.attackSpeed = 2000;
    }

    @Override
    public void draw(GraphicsContext gc) {

        if (SPRITE_SHEET != null) {
            // Calcoliamo quanto è grande un singolo quadratino (frame)
            double frameWidth = SPRITE_SHEET.getWidth() / TOTAL_COLS;
            double frameHeight = SPRITE_SHEET.getHeight() / TOTAL_ROWS;

            // Scegliamo quale riga e colonna vogliamo usare
            // Esempio: riga 0 per il movimento, riga 1 per l'attacco
            int row = 0;
            int col = currentFrame;

            // Coordinate X e Y di partenza nell'immagine sorgente
            double sx = col * frameWidth;
            double sy = row * frameHeight;

            // Disegniamo solo quel pezzetto
            gc.drawImage(
                    SPRITE_SHEET,
                    sx, sy, frameWidth, frameHeight, // Area dell'immagine da ritagliare
                    x, y, dimensionX, dimensionY    // Dove e quanto grande disegnarlo a schermo
            );
        }
    }

    @Override
    public void update(double deltaTime) {
        long currentTime = System.currentTimeMillis();
        if (!attacking) {
            if (x > stopX) {
                this.x -= 0.3;
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
