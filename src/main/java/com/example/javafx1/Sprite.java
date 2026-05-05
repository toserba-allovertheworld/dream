package com.example.javafx1;
import javafx.scene.canvas.GraphicsContext;
public abstract class Sprite {

    protected double x;
    protected double y;
    protected double dimensionX; // larghezza
    protected double dimensionY; // altezza
    protected boolean alive = true;

    public Sprite(double x, double y, double dimensionX, double dimensionY) {
        this.x = x;
        this.y = y;
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
    }

    public abstract void draw(GraphicsContext gc);
    public abstract void update(double deltaTime);

    public boolean collidesWith(Sprite other) {
        return x < other.x + other.dimensionX
                && x + dimensionX > other.x
                && y < other.y + other.dimensionY
                && y + dimensionY > other.y;
    }
    public double getX()           { return x; }
    public double getY()           { return y; }
    public double getDimensionX()  { return dimensionX; }
    public double getDimensionY()  { return dimensionY; }
    public boolean isAlive()       { return alive; }

    public void setX(double x)           { this.x = x; }
    public void setY(double y)           { this.y = y; }
    public void setAlive(boolean alive)  { this.alive = alive; }

    public double getCenterX() { return x + dimensionX / 2.0; }

    public double getCenterY() { return y + dimensionY / 2.0; }
}