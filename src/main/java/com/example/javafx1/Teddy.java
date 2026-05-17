package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Teddy extends Difesa {

    private static Image SPRITE_SHEET; // Spritesheet 6x4 (6 colonne, 4 righe di animazioni)

    // DIMENSIONI SPRITESHEET
    private final int ROWS = 4;
    private final int COLS = 6;

    // FRAME ATTUALE
    private int currentFrame = 0; // Colonna corrente nello spritesheet
    private int currentRow = 0; // Riga corrente nello spritesheet

    // TIMER ANIMAZIONE
    private long lastFrameTime = 0; // Timestamp ultimo cambio frame

    private final long FRAME_DURATION = 200; // Millisecondi tra un frame e l'altro

    // ANIMAZIONI
    /*
        Riga 0 = idle front (posizione di riposo, guardando avanti)
        Riga 1 = back (tornando a riposo)
        Riga 2 = attack (animazione di attacco)
        Riga 3 = shield/idle (posizione difensiva)
     */

    {
        // Inizializzatore di istanza: carica lo spritesheet
        try {
            SPRITE_SHEET = new Image(Teddy.class.getResourceAsStream("/img/teddyBear.png"));
            System.out.println("Spritesheet Peluche caricato!");
        } catch (Exception e) {
            System.out.println("Errore caricamento peluche: " + e.getMessage());
        }
    }

    // Costruttore: inizializza difesa Teddy con stats specifiche
    public Teddy(double x, double y) {
        super(x, y, 110, 110, 250, 20, 25); // Width=110, Height=110, Health=250, Damage=20, Cost=25
        this.attackSpeed = 1000; // Cooldown attacco (ogni 1 secondo)
    }

    // Aggiorna il frame dell'animazione corrente
    @Override
    public void update(double deltaTime) {
        animate(); // Esegui animazione
    }

    // Cambia il frame corrente se è passato abbastanza tempo
    public void animate() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= FRAME_DURATION) { // Se è passato il tempo di durata frame
            currentFrame++; // Avanza al frame successivo
            if (currentFrame >= COLS) { // Se oltre il numero di colonne (6)
                currentFrame = 0; // Ricomincia dal primo frame (loop)
            }
            lastFrameTime = currentTime; // Salva il nuovo timestamp
        }
    }

    // Disegna il Teddy sulla canvas usando il frame corrente dello spritesheet
    @Override
    public void draw(GraphicsContext gc) {

        if (SPRITE_SHEET == null) return; // Se spritesheet non caricato, esci

        double frameWidth = SPRITE_SHEET.getWidth() / COLS; // Larghezza singolo frame
        double frameHeight = SPRITE_SHEET.getHeight() / ROWS; // Altezza singolo frame

        // Disegna il frame della riga corrente e colonna corrente
        gc.drawImage(SPRITE_SHEET, currentFrame * frameWidth, currentRow * frameHeight, frameWidth, frameHeight, x, y, dimensionX, dimensionY);
    }

    // Attacca un nemico se può (cooldown rispettato) e cambia animazione
    public void attack(Nemico nemico, long currentTime) {
        if (canAttack(currentTime)) { // Se il cooldown è scaduto
            nemico.takeDamage(this.damage); // Applica danno al nemico
            attackPerformed(currentTime); // Aggiorna il timestamp di attacco
            currentRow = 2; // Cambia riga a quella di attacco (riga 2)
        }
    }

    // Ripristina lo Teddy allo stato di idle (riposo)
    public void idle() {
        currentRow = 0; // Cambia riga a quella di idle (riga 0)
    }
}