package com.example.javafx1;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player {
    double x, y;
    double width;
    Color color;

    public Player() {
        this.x = x;
        this.y = y;
        this.width = 15;
        this.color = Color.GRAY;
    }

    public void update() {
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        //gc.fill(x - width, y - width, width * 2, width * 2);
    }
}
