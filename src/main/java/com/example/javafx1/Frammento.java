package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;

public class Frammento extends Nemico {

    private static Image SPRITE_SHEET; // Spritesheet 3x1 (3 frame orizzontali, una sola riga)

    static {
        try {
            InputStream stream = Frammento.class.getResourceAsStream("/img/frammento.png");
            if (stream != null) {
                SPRITE_SHEET = new Image(stream);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Leo targetLeo; // Riferimento al bambino che il nemico attacca
    private final double stopX = 510; // Coordinata X dove il nemico si ferma e attacca
    private boolean attacking = false; // Flag: se true, sta eseguendo l'attacco
    private boolean disappearing = false; // Flag: se true, sta scomparendo dopo aver fatto danno
    private long attackStartTime = 0; // Timestamp inizio dell'attacco
    private final long attackAnimationDuration = 1200; // Durata totale animazione attacco
    private int currentFrame = 0; // Frame corrente (colonna nello spritesheet, 0-2)
    private int currentImage = 0; // Counter per rallentare l'animazione (frame skip)

    // Costruttore: inizializza nemico Frammento con stats specifiche
    public Frammento(double x, double y, double dimensionX, double dimensionY, Leo leo) {
        super(x, y, dimensionX, dimensionY, 80.0, 10.0, 1); // Health=80, Damage=10, Speed=1
        this.targetLeo = leo;
        this.attackSpeed = 2000; // Cooldown attacco (non usato)
    }

    // Disegna il nemico Frammento usando il frame corrente dello spritesheet
    @Override
    public void draw(GraphicsContext gc) {

        if (SPRITE_SHEET != null) {
            double frameWidth = SPRITE_SHEET.getWidth() / 3.0; // 3 frame orizzontali
            double frameHeight = SPRITE_SHEET.getHeight(); // Una sola riga
            double aspectRatio = frameWidth / frameHeight;
            double drawWidth = dimensionX;
            double drawHeight = dimensionX / aspectRatio; // Mantiene proporzioni
            // Disegna il frame corrente
            gc.drawImage(SPRITE_SHEET, currentFrame * frameWidth, 0, frameWidth, frameHeight, x, y, drawWidth, drawHeight);
        }
    }

    // Aggiorna animazione, posizione e stato del nemico
    @Override
    public void update(double deltaTime) {

        long currentTime = System.currentTimeMillis();

        // Animazione: cambia frame ogni 30 update (rallenta molto l'animazione)
        if (currentFrame == 3){ // Se avanzato oltre l'ultimo frame (0-2 = 3 frame totali)
            currentFrame = 0; // Ricomincia dal primo
        }else {
            if (currentImage == 30){ // Ogni 30 update (rallentamento maggiore rispetto a Clown)
                currentFrame++; // Avanza frame
                currentImage = 0; // Reset counter
            }else {
                currentImage++; // Incrementa counter
            }

        }

        if (!attacking) {
            if (blockedByDifesa) {
                return;
            }
            if (x > stopX) {
                this.x -= 0.3; // Movimento lento verso sinistra (più lento di Clown)
            } else {
                this.x = stopX; // Fissa a posizione di attacco
                attacking = true; // Inizia la fase di attacco
                attackStartTime = currentTime;
            }

        } else {

            long elapsed = currentTime - attackStartTime; // Tempo passato da inizio attacco

            if (elapsed >= 500 && !disappearing) { // Se 500ms passati
                disappearing = true; // Marchia come in scomparsa
                targetLeo.takeDamage(damage); // Applica danno al bambino
            }

            if (elapsed >= attackAnimationDuration) { // Se attacco finito (1200ms)

                alive = false; // Uccidi il nemico
            }
        }
    }
}