package com.example.javafx1;

public abstract class Nemico extends Sprite {

    protected double speed;
    protected double velocityX;
    protected double velocityY;

    // Costruttore: inizializza nemico con stats e velocità
    public Nemico(double x, double y, double dimensionX, double dimensionY, double health, double damage, double speed) {
        super(x, y, dimensionX, dimensionY);
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.speed = speed;
        this.velocityX = -speed;
        this.velocityY = 0;
    }


    @Override
    public void update(double deltaTime) {
    }


    public void attackDifesa(Difesa difesa, long currentTime) {
        if (canAttack(currentTime)) {
            difesa.takeDamage(this.damage);
            attackPerformed(currentTime);
        }
    }

    public boolean isBlockedByDifesa() {
        return blockedByDifesa;
    }

    public void setBlockedByDifesa(boolean blocked) {
        this.blockedByDifesa = blocked;
    }

    protected boolean blockedByDifesa = false;
}