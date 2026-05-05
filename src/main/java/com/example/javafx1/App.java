package com.example.javafx1;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(800, 800);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, 800, 800);

        gc.setFill(Color.BLUE);
        gc.fillOval(10, 10, 10, 10);
        StackPane root = new StackPane(canvas);

        List<Ball> balls = new ArrayList<>();
        createBalls(balls);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.setFill(Color.rgb(20, 20, 35));
                gc.fillRect(0, 0, 800, 800);

                for (Ball b : balls){
                    b.update();
                    b.draw(gc);

                    for (Ball b2 : balls){
                        b.collision(b2);
                    }
                }
            }
        };
        timer.start();

        stage.setScene(new Scene(root, 800, 800));
        stage.setTitle("Canvas Demo");
        stage.show();
    }

    static void createBalls(List<Ball> b) {
        for (int i = 0; i < 10; i++) {
            b.add(new Ball(Math.floor(Math.random() * 770 + 15),
                    Math.floor(Math.random() * 770 + 15),
                    Math.floor(Math.random() * 20),
                    Math.floor(Math.random() * 20),
                    Color.rgb((int) (Math.random() * 255),
                            (int) (Math.random() * 255),
                            (int) (Math.random() * 255))));
        }
    }

    static void main() {
        new App();
    }
}