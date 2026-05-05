package com.example.javafx1;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    double x, y;
    double vx, vy;
    double radius;
    Color color;

    public Ball(double x, double y, double vx, double vy, Color color) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = 15;
        this.color = color;
    }

    public void update() {
        if(x >= 800 - radius) vx = -vx;
        if(x <= 0.0 + radius) vx = -vx;

        if(y >= 800 - radius) vy = -vy;
        if(y <= 0.0 + radius) vy = -vy;

        x += vx;
        y += vy;
    }

    public void collision(Ball b){
        if(Math.sqrt(Math.pow(x - b.getX(), 2) + Math.pow(y - b.getY(), 2)) <= 32){
            if(x <= b.getX()){
                vx = -vx;
                b.setVx(-vx);
            } else if(y <= b.getY()) {
                vy = -vy;
                b.setVy(-vy);
            } /*else if(x <= b.getX() && y <= b.getY()){
                vx = -vx;
                b.setVx(-vx);
                vy = -vy;
                b.setVy(-vy);
            }*/
        }
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getRadius() {
        return radius;
    }
}