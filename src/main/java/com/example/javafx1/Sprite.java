package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;

public abstract class Sprite {
    // Posizione
    protected double x;
    protected double y;

    // Dimensioni delle immagini
    protected double dimensionX; // larghezza
    protected double dimensionY; // altezza

    // Indicatore per verificare se è morto o vivo
    protected boolean alive = true;

    // VITA E DANNO (per nemici E difese)
    protected double health;        // Quanta vita ha
    protected double maxHealth;     // Vita massima (serve per barre di vita)
    protected double damage;        // Danno che infligge quando colpisce

    // ATTACCO (fire-rate: ogni quanto può attaccare)
    protected double attackSpeed;   // Intervallo in millisecondi tra gli attacchi
    protected long lastAttackTime;  // Timestamp dell'ultimo attacco

    // Costruttore
    public Sprite(double x, double y, double dimensionX, double dimensionY) {
        this.x = x;
        this.y = y;
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
    }

    public Sprite() {

    }

    // Disegna lo spiret
    public abstract void draw(GraphicsContext gc);

    public abstract void update(double deltaTime);

    // Verifica se avvengono collisioni con un altro sprite
    public boolean collidesWith(Sprite other) {
        return x < other.x + other.dimensionX
                && x + dimensionX > other.x
                && y < other.y + other.dimensionY
                && y + dimensionY > other.y;
    }

    // Quando un robo subisce danno
    public void takeDamage(double dmg) {
        health -= dmg;
        if (health <= 0) {
            alive = false;
        }
    }

    // Controlla se puoi attaccare
    public boolean canAttack(long currentTime) {
        return (currentTime - lastAttackTime) >= attackSpeed;
    }

    // Quando attacchi, aggiorna il timer
    public void attackPerformed(long currentTime) {
        lastAttackTime = currentTime;
    }

    //--------------------------------------------------------
    //-------------------GETTER E SETTER----------------------
    //--------------------------------------------------------
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDimensionX() {
        return dimensionX;
    }

    public double getDimensionY() {
        return dimensionY;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public double getCenterX() {
        return x + dimensionX / 2.0;
    }

    public double getCenterY() {
        return y + dimensionY / 2.0;
    }
}