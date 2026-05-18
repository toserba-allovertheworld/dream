package com.example.javafx1;

public abstract class Nemico extends Sprite {

    protected double speed;  // Velocità di movimento base del nemico (pixel/frame)
    protected double velocityX;   // Spostamento orizzontale per frame (generalmente negativo = sinistra)
    protected double velocityY;   // Spostamento verticale per frame (per movimenti oscillanti)

    // Costruttore: inizializza nemico con stats e velocità
    public Nemico(double x, double y, double dimensionX, double dimensionY, double health, double damage, double speed) {
        super(x, y, dimensionX, dimensionY);
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.speed = speed;
        this.velocityX = -speed; // Velocità negativa = movimento verso sinistra
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