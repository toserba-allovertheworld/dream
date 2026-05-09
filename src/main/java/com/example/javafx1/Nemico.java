package com.example.javafx1;

public abstract class Nemico extends Sprite {

    protected double speed;  // velocità di movimento specifica del nemico
    protected double velocityX;   // spostamento orizzontale per frame
    protected double velocityY;   // spostamento verticale per frame

    public Nemico(double x, double y, double dimensionX, double dimensionY,
                  double health, double damage, double speed) {
        super(x, y, dimensionX, dimensionY);
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.speed = speed;

        this.velocityX = -speed;
        this.velocityY = 0;
    }

    public Nemico(double x, double y, double dimensionX, double dimensionY) {
        super();
    }

    public void move() {
    }

    @Override
    public void update(double deltaTime) {
        // Da implementare
    }
}