package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EssenzaBar {
    private double essenza;
    private final double MAX_ESSENZA = 100;
    private double regenRate = 1;

    // Costruttore: inizializza la barra con essenza iniziale a 50
    public EssenzaBar() {
        essenza = 50;
    }

    // Rigenera essenza ogni frame
    public void update(double deltaTime) {
        essenza += regenRate * deltaTime;
        if (essenza > MAX_ESSENZA) essenza = MAX_ESSENZA;
    }

    // Consuma essenza se ne hai a sufficienza, ritorna se il consumo è avvenuto
    public boolean useEssenza(double cost) {
        if (essenza >= cost) {
            essenza -= cost;
            return true;
        }
        return false;
    }

    // Disegna la barra dell'essenza e il testo nella parte superiore sinistra
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

    // Ritorna la quantità attuale di essenza
    public double getEssenza() {
        return essenza;
    }
}