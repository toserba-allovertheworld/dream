package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Difesa extends Sprite {

    protected int cost; // Costo in essenza per piazzare questa difesa
    protected Image spriteSheet; // Immagine contenente i frame dell'animazione
    protected int currentFrame = 0; // Frame corrente dell'animazione
    protected int totalFrames = 1; // Numero totale di frame disponibili
    protected long lastFrameChange = 0; // Timestamp ultimo cambio frame
    protected long frameDuration = 300; // Millisecondi tra un frame e l'altro

    // Costruttore: inizializza difesa con stats e costo
    public Difesa(double x, double y, double width, double height, double health, double damage, int cost) {
        super(x, y, width, height);
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.cost = cost;
    }

    // Aggiorna lo sprite: gestisce animazioni
    @Override
    public void update(double deltaTime) {
        animate(); // Esegui animazione del frame
    }

    // Cambia il frame corrente se è passato abbastanza tempo
    protected void animate() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameChange >= frameDuration) { // Se tempo >= durata frame
            currentFrame++; // Avanza al frame successivo
            if (currentFrame >= totalFrames) { // Se fuori range
                currentFrame = 0; // Ricomincia dal primo frame (loop)
            }
            lastFrameChange = currentTime; // Salva il timestamp
        }
    }

    // Disegna lo sprite sulla canvas usando lo spritesheet
    @Override
    public void draw(GraphicsContext gc) {
        if (spriteSheet != null) {
            double frameWidth = spriteSheet.getWidth() / totalFrames; // Larghezza singolo frame
            double frameHeight = spriteSheet.getHeight(); // Altezza (assunto una sola riga)
            // Disegna il frame corrente dello spritesheet
            gc.drawImage(spriteSheet, currentFrame * frameWidth, 0, frameWidth, frameHeight, x, y, dimensionX, dimensionY);
        }
    }

    // Ritorna il costo della difesa in essenza
    public int getCost() {
        return cost;
    }
}