package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.InputStream;

public class Ombra extends Nemico {

    private static Image SPRITE_SHEET; // Spritesheet 5x6 (5 colonne, 6 righe di frame)
    private final int TOTAL_COLS = 5; // Numero di colonne nello spritesheet
    private final int TOTAL_ROWS = 6; // Numero di righe nello spritesheet
    private Stato statoAttuale = Stato.IDLE; // Stato comportamentale: IDLE o DASH

    private long timerStato; // Timer per gestire cambio stato (inizializzato nel costruttore)
    private double targetX; // Coordinata X target durante il DASH
    private final double LARGHEZZA_CELLA = 140; // Larghezza in pixel di una "cella" per il DASH
    private int currentRow = 0; // Riga corrente dello spritesheet (0=IDLE, 1=DASH)

    private int frameCounter = 0; // Counter per rallentare l'animazione
    private final int FRAME_DELAY = 10; // Ogni quanti update cambia frame

    private Leo targetLeo; // Riferimento al bambino che il nemico attacca
    private final double stopX = 540; // Coordinata X dove il nemico si ferma per attaccare
    private boolean attacking = false; // Flag: se true, sta eseguendo l'attacco
    private boolean disappearing = false; // Flag: se true, sta scomparendo dopo aver fatto danno
    private long attackStartTime = 0; // Timestamp inizio dell'attacco
    private final long attackAnimationDuration = 1200; // Durata totale animazione attacco

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

    private int currentFrame = 0; // Frame corrente (colonna nello spritesheet)

    // Costruttore: inizializza nemico Ombra con comportamento a stati
    public Ombra(double x, double y, double dimensionX, double dimensionY, Leo leo) {
        super(x, y, dimensionX, dimensionY, 80.0, 10.0, 1.5); // Health=80, Damage=10, Speed=1.5
        this.targetLeo = leo;
        this.attackSpeed = 2000; // Cooldown attacco (non usato)
        this.timerStato = System.currentTimeMillis(); // Inizializza timer
    }

    // Disegna il nemico Ombra dalla posizione corrente nello spritesheet
    @Override
    public void draw(GraphicsContext gc) {
        if (SPRITE_SHEET != null) {
            double frameWidth = SPRITE_SHEET.getWidth() / TOTAL_COLS; // Larghezza singolo frame
            double frameHeight = SPRITE_SHEET.getHeight() / TOTAL_ROWS; // Altezza singolo frame

            double sx = currentFrame * frameWidth; // Coordinata X nello spritesheet
            double sy = currentRow * frameHeight; // Coordinata Y nello spritesheet

            gc.drawImage(SPRITE_SHEET, sx, sy, frameWidth, frameHeight, x, y, dimensionX, dimensionY);
        }
    }

    // Aggiorna stato, animazione e posizione del nemico (IDLE/DASH/ATTACK)
    @Override
    public void update(double deltaTime) {
        long currentTime = System.currentTimeMillis();

        // Animazione: cambia frame ogni FRAME_DELAY update
        frameCounter++; // Incrementa counter
        if (frameCounter >= FRAME_DELAY) {
            // Se IDLE: 4 frame, se DASH: 5 frame (numero di colonne per quello stato)
            currentFrame = (currentFrame + 1) % (statoAttuale == Stato.IDLE ? 4 : 5);
            frameCounter = 0; // Reset counter
        }

        if (!attacking) {
            if (statoAttuale == Stato.IDLE) {
                currentRow = 0; // Riga 0 = animazione idle

                if (currentTime - timerStato >= 2500) { // Se 2.5 secondi passati nello IDLE
                    statoAttuale = Stato.DASH; // Cambia stato a DASH
                    targetX = this.x - LARGHEZZA_CELLA; // Calcola target (una cella a sinistra)
                    timerStato = currentTime; // Reset timer
                }
            } else if (statoAttuale == Stato.DASH) {
                currentRow = 1; // Riga 1 = animazione dash
                this.x -= 8; // Movimento veloce verso sinistra

                // Se raggiunge il target oppure (||) raggiunge posizione di attacco
                if (this.x <= targetX || this.x <= stopX) {
                    if (this.x <= stopX) { // Se raggiunto punto di attacco
                        this.x = stopX; // Fissa posizione
                        attacking = true; // Inizia attacco
                        attackStartTime = currentTime;
                    } else {
                        statoAttuale = Stato.IDLE; // Ritorna a IDLE
                        timerStato = currentTime; // Reset timer
                    }
                }
            }
        } else {
            long elapsed = currentTime - attackStartTime; // Tempo passato da inizio attacco

            if (elapsed >= 500 && !disappearing) { // Se 500ms passati
                disappearing = true; // Marchia come in scomparsa
                targetLeo.takeDamage(damage); // Applica danno
            }

            if (elapsed >= attackAnimationDuration) { // Se attacco finito (1200ms)
                alive = false; // Uccidi il nemico
            }
        }
    }
}