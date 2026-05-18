package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Griglia {

    private final int rows;
    private final int cols;
    private final double cellWidth;
    private final double cellHeight;
    private final double startX;
    private final double startY;
    private boolean[][] occupied;

    // Costruttore: inizializza griglia con dimensioni e posizione
    public Griglia(int rows, int cols, double cellWidth, double cellHeight, double startX, double startY) {
        this.rows = rows;
        this.cols = cols;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.startX = startX;
        this.startY = startY;
        occupied = new boolean[rows][cols];
    }

    // Converte coordinata X del mouse in indice di colonna della griglia
    public int getCol(double mouseX) {
        return (int)((mouseX - startX) / cellWidth);
    }

    // Converte coordinata Y del mouse in indice di riga della griglia
    public int getRow(double mouseY) {
        return (int)((mouseY - startY) / cellHeight);
    }

    // Controlla se le coordinate di riga/colonna sono dentro i limiti della griglia
    public boolean isInsideGrid(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    // Controlla se una cella è già occupata da una difesa
    public boolean isOccupied(int row, int col) {
        return occupied[row][col];
    }

    // Marca una cella come occupata
    public void occupyCell(int row, int col) {
        occupied[row][col] = true;
    }

    // Marca una cella come libera
    public void freeCell(int row, int col) {
        occupied[row][col] = false;
    }

    // Converte indice di colonna in coordinata X dello schermo
    public double getCellX(int col) {
        return startX + col * cellWidth;
    }

    // Converte indice di riga in coordinata Y dello schermo
    public double getCellY(int row) {
        return startY + row * cellHeight;
    }

    // Disegna la griglia sulla canvas con linee semi-trasparenti
    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.rgb(255,255,255,0.2));

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double x = getCellX(c);
                double y = getCellY(r);
                gc.strokeRect(x, y, cellWidth, cellHeight);
            }
        }
    }
}