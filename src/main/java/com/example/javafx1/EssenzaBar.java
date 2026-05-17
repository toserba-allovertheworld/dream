package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EssenzaBar {
    private double essenza; // Quantità attuale di essenza
    private final double MAX_ESSENZA = 100; // Massimo ammontare di essenza
    private double regenRate = 1; // Punti di essenza rigenerati per frame (1 punto per frame)

    // Costruttore: inizializza la barra con essenza iniziale a 50
    public EssenzaBar() {
        essenza = 50; // Inizia con 50 di 100
    }

    // Rigeneraessenza ogni frame (aggiunge regenRate per frame)
    public void update(double deltaTime) {
        essenza += regenRate * deltaTime; // Aumenta essenza (regenRate * tempo_passato)
        if (essenza > MAX_ESSENZA) essenza = MAX_ESSENZA; // Limita al massimo (100)
    }

    // Consuma essenza se ne hai a sufficienza, ritorna se il consumo è avvenuto
    public boolean useEssenza(double cost) {
        if (essenza >= cost) { // Se hai abbastanza essenza
            essenza -= cost; // Sottrai il costo
            return true; // Operazione riuscita
        }
        return false; // Non avevi abbastanza, niente è stato consumato
    }

    // Disegna la barra dell'essenza e il testo nella parte superiore sinistra
    public void draw(GraphicsContext gc) {
        double barWidth = 300; // Larghezza totale della barra in pixel
        double currentWidth = (essenza / MAX_ESSENZA) * barWidth; // Larghezza barra riempita (proporzione)

        gc.setFill(Color.BLACK);
        gc.fillRect(20, 20, barWidth, 30); // Sfondo nero della barra

        gc.setFill(Color.CYAN);
        gc.fillRect(20, 20, currentWidth, 30); // Barra riempita (colore ciano)

        gc.setStroke(Color.WHITE);
        gc.strokeRect(20, 20, barWidth, 30); // Bordo bianco della barra

        gc.setFill(Color.WHITE);
        gc.fillText("Essenza: " + (int) essenza, 30, 40); // Testo con valore attuale
    }

    // Ritorna la quantità attuale di essenza
    public double getEssenza() {
        return essenza;
    }
}