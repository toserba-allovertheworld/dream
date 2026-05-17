package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Griglia {

    // Dimensioni della griglia
    private final int rows; // Numero di righe (altezza in celle)
    private final int cols; // Numero di colonne (larghezza in celle)

    // Dimensioni delle celle
    private final double cellWidth; // Larghezza di una singola cella in pixel
    private final double cellHeight; // Altezza di una singola cella in pixel

    // Posizione iniziale della griglia nello schermo
    private final double startX; // Coordinata X dove inizia la griglia
    private final double startY; // Coordinata Y dove inizia la griglia

    // Tracciamento celle occupate
    private boolean[][] occupied; // Matrice booleana: true = cella occupata da una difesa

    // Costruttore: inizializza griglia con dimensioni e posizione
    public Griglia(int rows, int cols, double cellWidth, double cellHeight, double startX, double startY) {
        this.rows = rows;
        this.cols = cols;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.startX = startX;
        this.startY = startY;
        occupied = new boolean[rows][cols]; // Inizializza matrice (false di default)
    }

    // Converte coordinata X del mouse in indice di colonna della griglia
    public int getCol(double mouseX) {
        return (int)((mouseX - startX) / cellWidth); // Sottrae offset e divide per dimensione cella
    }

    // Converte coordinata Y del mouse in indice di riga della griglia
    public int getRow(double mouseY) {
        return (int)((mouseY - startY) / cellHeight); // Sottrae offset e divide per dimensione cella
    }

    // Controlla se le coordinate di riga/colonna sono dentro i limiti della griglia
    public boolean isInsideGrid(int row, int col) {
        // Ritorna true se: riga >= 0 E riga < max_righe E colonna >= 0 E colonna < max_colonne
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    // Controlla se una cella è già occupata da una difesa
    public boolean isOccupied(int row, int col) {
        return occupied[row][col]; // Ritorna true se cella occupata
    }

    // Marca una cella come occupata
    public void occupyCell(int row, int col) {
        occupied[row][col] = true; // Segna cella come occupata
    }

    // Marca una cella come libera
    public void freeCell(int row, int col) {
        occupied[row][col] = false; // Segna cella come libera
    }

    // Converte indice di colonna in coordinata X dello schermo
    public double getCellX(int col) {
        return startX + col * cellWidth; // Posizione X iniziale + offset per colonna
    }

    // Converte indice di riga in coordinata Y dello schermo
    public double getCellY(int row) {
        return startY + row * cellHeight; // Posizione Y iniziale + offset per riga
    }

    // Disegna la griglia sulla canvas con linee semi-trasparenti
    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.rgb(255,255,255,0.2)); // Linee bianche, 20% opacità

        // Disegna tutte le celle della griglia
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double x = getCellX(c);
                double y = getCellY(r);

                gc.strokeRect(x, y, cellWidth, cellHeight); // Disegna rettangolo senza riempimento
            }
        }
    }
}