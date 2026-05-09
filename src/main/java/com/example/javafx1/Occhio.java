package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.List;

public class Occhio extends Nemico {

    // Carichiamo l'immagine una sola volta per tutte le istanze di Occhio
    // Assicurati che il percorso sia corretto rispetto alla cartella delle risorse
    private static final Image SPRITE_OCCHIO = new Image(Occhio.class.getResourceAsStream("/immagini/occhio.png"));

    private double aoeRadius;

    public Occhio(double x, double y, double dimensionX, double dimensionY) {
        super(x, y, dimensionX, dimensionY, 80.0, 15.0, 2.0);
        this.aoeRadius = 130.0;
        this.attackSpeed = 2000;
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Disegniamo l'immagine dell'occhio al posto del cerchio
        // gc.drawImage(immagine, x, y, larghezza, altezza)
        if (SPRITE_OCCHIO != null && !SPRITE_OCCHIO.isError()) {
            gc.drawImage(SPRITE_OCCHIO, x, y, dimensionX, dimensionY);
        } else {
            // Fallback: se l'immagine non carica, disegna un cerchio per non rompere il gioco
            gc.setFill(Color.MAGENTA);
            gc.fillOval(x, y, dimensionX, dimensionY);
        }

        // --- FEEDBACK VISIVO DANNO AD AREA ---
        // Opzionale: disegna un cerchio pulsante quando l'attacco è quasi pronto
        disegnaIndicatoreAttacco(gc);
    }

    private void disegnaIndicatoreAttacco(GraphicsContext gc) {
        long tempoRimanente = System.currentTimeMillis() - lastAttackTime;
        if (tempoRimanente > attackSpeed - 500) { // Inizia a lampeggiare 0.5s prima dell'attacco
            gc.setStroke(Color.rgb(255, 0, 0, 0.4));
            gc.setLineWidth(3);
            gc.strokeOval(getCenterX() - aoeRadius, getCenterY() - aoeRadius, aoeRadius * 2, aoeRadius * 2);
        }
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        // Movimento sinusoidale (fluttuante)
        this.y += Math.sin(System.currentTimeMillis() / 200.0) * 1.2;
    }

    public void attackArea(List<Sprite> bersagli, long currentTime) {
        if (canAttack(currentTime)) {
            boolean hit = false;
            for (Sprite s : bersagli) {
                if (s.isAlive()) {
                    double distanza = calcolaDistanza(this, s);
                    if (distanza <= aoeRadius) {
                        s.takeDamage(this.damage);
                        hit = true;
                    }
                }
            }
            if (hit) attackPerformed(currentTime);
        }
    }

    private double calcolaDistanza(Sprite s1, Sprite s2) {
        return Math.sqrt(Math.pow(s1.getCenterX() - s2.getCenterX(), 2) +
                Math.pow(s1.getCenterY() - s2.getCenterY(), 2));
    }
}