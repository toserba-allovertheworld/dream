package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;

public abstract class Sprite {
    // Posizione nello schermo
    protected double x;
    protected double y;

    // Dimensioni delle immagini
    protected double dimensionX;
    protected double dimensionY;

    // Indicatore per verificare se è morto o vivo
    protected boolean alive = true;

    // VITA E DANNO (per nemici E difese)
    protected double health;
    protected double maxHealth;
    protected double damage;

    // ATTACCO (fire-rate: ogni quanto può attaccare)
    protected double attackSpeed;
    protected long lastAttackTime;

    // Costruttore pieno: inizializza posizione e dimensioni
    public Sprite(double x, double y, double dimensionX, double dimensionY) {
        this.x = x;
        this.y = y;
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
    }

    // Costruttore vuoto: permette creazione senza parametri
    public Sprite() {

    }

    // Disegna lo sprite sulla canvas (implementato dalle sottoclassi)
    public abstract void draw(GraphicsContext gc);

    // Aggiorna logica dello sprite ogni frame (implementato dalle sottoclassi)
    public abstract void update(double deltaTime);

    // Verifica se questo sprite collide con un altro sprite
    public boolean collidesWith(Sprite other) {
        return x < other.x + other.dimensionX
                && x + dimensionX > other.x
                && y < other.y + other.dimensionY
                && y + dimensionY > other.y;
    }

    // Applica danno a questo sprite e lo uccide se vita <= 0
    public void takeDamage(double dmg) {
        health -= dmg;
        if (health <= 0) {
            alive = false;
        }
    }

    // Controlla se questo sprite può attaccare in base al cooldown
    public boolean canAttack(long currentTime) {
        return (currentTime - lastAttackTime) >= attackSpeed;
    }

    // Registra che è stato effettuato un attacco (aggiorna il timer)
    public void attackPerformed(long currentTime) {
        lastAttackTime = currentTime;
    }

    // Ritorna la coordinata X dello sprite
    public double getX() {
        return x;
    }

    // Ritorna la coordinata Y dello sprite
    public double getY() {
        return y;
    }

    // Ritorna la larghezza dello sprite
    public double getDimensionX() {
        return dimensionX;
    }

    // Ritorna l'altezza dello sprite
    public double getDimensionY() {
        return dimensionY;
    }

    // Ritorna se lo sprite è vivo
    public boolean isAlive() {
        return alive;
    }

    // Imposta la coordinata X dello sprite
    public void setX(double x) {
        this.x = x;
    }

    // Imposta la coordinata Y dello sprite
    public void setY(double y) {
        this.y = y;
    }

    // Imposta lo stato di vita dello sprite
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    // Ritorna il centro X dello sprite (per calcoli di distanza)
    public double getCenterX() {
        return x + dimensionX / 2.0;
    }

    // Ritorna il centro Y dello sprite (per calcoli di distanza)
    public double getCenterY() {
        return y + dimensionY / 2.0;
    }
}