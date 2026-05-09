package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.List;

public class Occhio extends Nemico {

    private static final Image SPRITE_SHEET = new Image(Occhio.class.getResourceAsStream("/immagini/image_9291ff.jpg"));
    private double aoeRadius = 150.0; // Raggio d'azione dell'attacco

    public Occhio(double x, double y, double dimensionX, double dimensionY) {
        // Parametri: x, y, dimX, dimY, vita, danno, velocità
        super(x, y, dimensionX, dimensionY, 80.0, 10.0, 1.5);
        this.attackSpeed = 2000; // Può attaccare ogni 2 secondi
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (SPRITE_SHEET != null && !SPRITE_SHEET.isError()) {
            double frameWidth = SPRITE_SHEET.getWidth() / 3;
            double frameHeight = SPRITE_SHEET.getHeight();
            // Disegna solo la prima posa (Frame 1)
            gc.drawImage(SPRITE_SHEET, 0, 0, frameWidth, frameHeight, x, y, dimensionX, dimensionY);
        }
    }

    @Override
    public void update(double deltaTime) {
        this.x += velocityX; // Si muove verso sinistra
        this.y += Math.sin(System.currentTimeMillis() * 0.005) * 0.8; // Oscillazione
    }

    // Metodo per gestire l'attacco ad area
    public void attackArea(List<Sprite> bersagli, long currentTime) {
        if (canAttack(currentTime)) { // Verifica il cooldown dell'attacco
            boolean haColpito = false;
            for (Sprite s : bersagli) {
                if (s.isAlive()) {
                    // Calcolo distanza tra l'occhio e il bersaglio
                    double dist = Math.sqrt(Math.pow(getCenterX() - s.getCenterX(), 2) +
                            Math.pow(getCenterY() - s.getCenterY(), 2));
                    if (dist <= aoeRadius) {
                        s.takeDamage(this.damage); // Infligge danno
                        haColpito = true;
                    }
                }
            }
            if (haColpito) {
                attackPerformed(currentTime); // Reset del timer di attacco
            }
        }
    }
}