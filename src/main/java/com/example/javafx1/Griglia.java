package com.example.javafx1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Griglia {

    // numero righe e colonne
    private final int rows;
    private final int cols;

    // dimensione celle
    private final double cellWidth;
    private final double cellHeight;

    // posizione iniziale della griglia
    private final double startX;
    private final double startY;

    // celle occupate
    private boolean[][] occupied;

    public Griglia(int rows, int cols, double cellWidth, double cellHeight, double startX, double startY) {
        this.rows = rows;
        this.cols = cols;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.startX = startX;
        this.startY = startY;
        occupied = new boolean[rows][cols];
    }

    public int getCol(double mouseX) {
        return (int)((mouseX - startX) / cellWidth);
    }

    public int getRow(double mouseY) {
        return (int)((mouseY - startY) / cellHeight);
    }

    public boolean isInsideGrid(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public boolean isOccupied(int row, int col) {
        return occupied[row][col];
    }

    public void occupyCell(int row, int col) {
        occupied[row][col] = true;
    }

    public void freeCell(int row, int col) {
        occupied[row][col] = false;
    }

    public double getCellX(int col) {
        return startX + col * cellWidth;
    }

    public double getCellY(int row) {
        return startY + row * cellHeight;
    }

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