package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.List;

public class Occhio extends Nemico {

    private static Image SPRITE_SHEET;

    static {
        try {
            InputStream stream = Occhio.class.getResourceAsStream("/img/occhio.png");
            if (stream != null) {
                SPRITE_SHEET = new Image(stream);
                System.out.println("Immagine occhio caricata!");

            } else System.out.println("Immagine NON trovata!");

        } catch (Exception e) {
            System.out.println("Errore caricamento occhio: " + e.getMessage());
        }
    }

    private double aoeRadius = 150.0;

    public Occhio(
            double x,
            double y,
            double dimensionX,
            double dimensionY
    ) {

        super(
                x,
                y,
                dimensionX,
                dimensionY,
                80.0,
                10.0,
                1.5
        );

        this.attackSpeed = 2000;
    }

    @Override
    public void draw(GraphicsContext gc) {

        if (SPRITE_SHEET != null) {

            // Calcola le dimensioni mantenendo l'aspect ratio
            // L'immagine contiene 3 occhi affiancati
            double frameWidth =
                    SPRITE_SHEET.getWidth() / 3.0;

            double frameHeight =
                    SPRITE_SHEET.getHeight();

            double aspectRatio = frameWidth / frameHeight;
            double drawWidth = dimensionX;
            double drawHeight = dimensionX / aspectRatio;  // Mantiene le proporzioni
            gc.drawImage(SPRITE_SHEET, 0, 0, frameWidth, frameHeight, x, y, drawWidth, drawHeight);
        }
    }

    @Override
    public void update(double deltaTime) {

        this.x -= 0.3;
        this.y += Math.sin(System.currentTimeMillis() * 0.005) * 0.3;
    }

    public void attackArea(
            List<Sprite> bersagli,
            long currentTime
    ) {
        if (canAttack(currentTime)) {
            boolean haColpito = false;
            for (Sprite s : bersagli) {
                if (s.isAlive()) {
                    double dist = Math.sqrt(Math.pow(getCenterX() - s.getCenterX(), 2) + Math.pow(getCenterY() - s.getCenterY(), 2));
                    if (dist <= aoeRadius) {
                        s.takeDamage(this.damage);
                        haColpito = true;
                    }
                }
            }
            if (haColpito) {
                attackPerformed(currentTime);
            }
        }
    }
}