package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;

public class Occhio extends Nemico {

    private static Image SPRITE_SHEET; // Spritesheet statico (3 frame orizzontali)

    static {
        try {
            InputStream stream = Occhio.class.getResourceAsStream("/img/occhio.png");
            if (stream != null) {
                SPRITE_SHEET = new Image(stream);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Leo targetLeo; // Riferimento al bambino che il nemico attacca
    private final double stopX = 510; // Coordinata X dove il nemico si ferma e attacca
    private boolean attacking = false; // Flag: se true, il nemico sta eseguendo l'animazione di attacco
    private boolean disappearing = false; // Flag: se true, il nemico sta scomparendo dopo aver fatto danno
    private long attackStartTime = 0; // Timestamp inizio dell'attacco (per timer durata attacco)
    private final long attackAnimationDuration = 1200; // Millisecondi totali dell'animazione di attacco
    private int currentFrame = 0; // Frame corrente (0, 1, o 2)

    // Costruttore: inizializza nemico Occhio con stats specifiche
    public Occhio(double x, double y, double dimensionX, double dimensionY, Leo leo) {

        super(x, y, dimensionX, dimensionY, 80.0, 10.0, 1); // Health=80, Damage=10, Speed=1

        this.targetLeo = leo;
        this.attackSpeed = 2000; // Cooldown attacco (non usato per questo nemico)
    }

    // Disegna il nemico Occhio usando il frame corrente dello spritesheet
    @Override
    public void draw(GraphicsContext gc) {

        if (SPRITE_SHEET != null) {
            double frameWidth = SPRITE_SHEET.getWidth() / 3.0; // 3 frame orizzontali
            double frameHeight = SPRITE_SHEET.getHeight(); // Una sola riga
            double aspectRatio = frameWidth / frameHeight;
            double drawWidth = dimensionX;
            double drawHeight = dimensionX / aspectRatio; // Mantiene proporzioni
            // Disegna il frame corrente mantenendo l'aspect ratio
            gc.drawImage(SPRITE_SHEET, currentFrame * frameWidth, 0, frameWidth, frameHeight, x, y, drawWidth, drawHeight);
        }
    }

    // Aggiorna posizione e stato del nemico (movimento, attacco, scomparsa)
    @Override
    public void update(double deltaTime) {
        long currentTime = System.currentTimeMillis();

        if (!attacking) {
            if (x > stopX) {
                this.x -= 1; // Movimento costante verso sinistra
                this.y += Math.sin(currentTime * 0.005) * 0.1; // Oscillazione sinusoidale verticale (movimento ondulato)
            } else {
                this.x = stopX; // Fissa a posizione di attacco
                attacking = true; // Inizia la fase di attacco
                attackStartTime = currentTime;
                currentFrame = 1; // Cambia frame a quello intermedio
            }
        } else {
            long elapsed = currentTime - attackStartTime; // Tempo passato da inizio attacco
            if (elapsed >= 500 && !disappearing) { // Se 500ms passati
                currentFrame = 2; // Cambia a frame finale
                disappearing = true; // Marchia come in scomparsa
                targetLeo.takeDamage(damage); // Applica danno al bambino
            }

            if (elapsed >= attackAnimationDuration) { // Se attacco finito (1200ms)
                alive = false; // Uccidi il nemico
            }
        }
    }
}