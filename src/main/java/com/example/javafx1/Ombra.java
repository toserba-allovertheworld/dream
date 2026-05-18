package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.InputStream;

public class Ombra extends Nemico {

    private static Image SPRITE_SHEET;
    private final int TOTAL_COLS = 5;
    private final int TOTAL_ROWS = 6;
    private Stato statoAttuale = Stato.IDLE;

    private long timerStato;
    private double targetX;
    private final double LARGHEZZA_CELLA = 140;
    private int currentRow = 0;

    private int frameCounter = 0;
    private final int FRAME_DELAY = 10;

    private Leo targetLeo;
    private final double stopX = 540;
    private boolean attacking = false;
    private boolean disappearing = false;
    private long attackStartTime = 0;
    private final long attackAnimationDuration = 1200;

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

    private int currentFrame = 0;

    // Costruttore: inizializza nemico Ombra con comportamento a stati
    public Ombra(double x, double y, double dimensionX, double dimensionY, Leo leo) {
        super(x, y, dimensionX, dimensionY, 80.0, 18.0, 1.5);
        this.targetLeo = leo;
        this.attackSpeed = 2000;
        this.timerStato = System.currentTimeMillis();
    }

    // Disegna il nemico Ombra dalla posizione corrente nello spritesheet
    @Override
    public void draw(GraphicsContext gc) {
        if (SPRITE_SHEET != null) {
            double frameWidth = SPRITE_SHEET.getWidth() / TOTAL_COLS;
            double frameHeight = SPRITE_SHEET.getHeight() / TOTAL_ROWS;
            double sx = currentFrame * frameWidth;
            double sy = currentRow * frameHeight;
            gc.drawImage(SPRITE_SHEET, sx, sy, frameWidth, frameHeight, x, y, dimensionX, dimensionY);
        }
    }

    // Aggiorna stato, animazione e posizione del nemico (IDLE/DASH/ATTACK)
    @Override
    public void update(double deltaTime) {
        long currentTime = System.currentTimeMillis();

        // Gestione Animazione
        frameCounter++;
        if (frameCounter >= FRAME_DELAY) {
            currentFrame = (currentFrame + 1) % (statoAttuale == Stato.IDLE ? 4 : 5);
            frameCounter = 0;
        }

        // Gestione Stati di Comportamento
        if (!attacking) {
            if (blockedByDifesa) {
                statoAttuale = Stato.IDLE;
                return;
            }
            if (statoAttuale == Stato.IDLE) {
                currentRow = 0;
                if (currentTime - timerStato >= 2500) {
                    statoAttuale = Stato.DASH;
                    targetX = this.x - LARGHEZZA_CELLA;
                    timerStato = currentTime;
                }
            } else if (statoAttuale == Stato.DASH) {
                currentRow = 1;
                this.x -= 8;
                if (this.x <= targetX || this.x <= stopX) {
                    if (this.x <= stopX) {
                        this.x = stopX;
                        attacking = true;
                        attackStartTime = currentTime;
                    } else {
                        statoAttuale = Stato.IDLE;
                        timerStato = currentTime;
                    }
                }
            }
        } else {
            long elapsed = currentTime - attackStartTime;
            if (elapsed >= 500 && !disappearing) {
                disappearing = true;
                targetLeo.takeDamage(damage);
            }
            if (elapsed >= attackAnimationDuration) {
                alive = false;
            }
        }
    }
}