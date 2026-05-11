package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EssenzaBar {
    private double essenza;
    private final double MAX_ESSENZA = 100;
    private double regenRate = 1;

    public EssenzaBar() {
        essenza = 50;
    }

    public void update(double deltaTime) {
        essenza += regenRate * deltaTime;
        if (essenza > MAX_ESSENZA) essenza = MAX_ESSENZA;
    }

    // consuma essenza
    public boolean useEssenza(double cost) {
        if (essenza >= cost) {
            essenza -= cost;
            return true;
        }
        return false;
    }

    public void draw(GraphicsContext gc) {
        double barWidth = 300;
        double currentWidth = (essenza / MAX_ESSENZA) * barWidth;
        gc.setFill(Color.BLACK);
        gc.fillRect(20, 20, barWidth, 30);
        gc.setFill(Color.CYAN);
        gc.fillRect(20, 20, currentWidth, 30);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(20, 20, barWidth, 30);
        gc.setFill(Color.WHITE);
        gc.fillText("Essenza: " + (int) essenza, 30, 40);
    }

    public double getEssenza() {
        return essenza;
    }
}