package com.example.javafx1;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class App extends Application {

    private Image immagineSfondo;
    private Image spriteSheetBambino;

    private Sprite bambino;

    private List<Nemico> nemici = new ArrayList<>();

    private final double WIDTH = 1920;
    private final double HEIGHT = 1080;

    @Override
    public void start(Stage stage) {

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // CARICAMENTO IMMAGINI
        try {

            immagineSfondo =
                    new Image(
                            getClass().getResourceAsStream("/img/bg.png")
                    );

            spriteSheetBambino =
                    new Image(
                            getClass().getResourceAsStream("/img/Leo.png")
                    );

            System.out.println("Immagini caricate!");

        } catch (Exception e) {

            System.out.println(
                    "Errore caricamento immagini: "
                            + e.getMessage()
            );
        }

        // BAMBINO
        bambino = new Sprite(
                40,
                HEIGHT / 2 - 80,
                120,
                160
        ) {

            @Override
            public void draw(GraphicsContext gc) {

                if (spriteSheetBambino != null) {

                    // FRAME SINGOLO
                    double frameWidth =
                            spriteSheetBambino.getWidth() / 3;

                    double frameHeight =
                            spriteSheetBambino.getHeight() / 4;

                    // SOLO FRAME ALTO SINISTRA
                    gc.drawImage(
                            spriteSheetBambino,

                            // sorgente
                            0,
                            0,
                            frameWidth,
                            frameHeight,

                            // destinazione
                            x,
                            y,
                            120,
                            160
                    );
                }
            }

            @Override
            public void update(double dt) {
            }
        };

        bambino.health = 1000;
        bambino.maxHealth = 1000;

        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {

                update();
                draw(gc);
            }
        };

        timer.start();

        stage.setScene(
                new Scene(
                        new StackPane(canvas),
                        WIDTH,
                        HEIGHT
                )
        );

        stage.setTitle(
                "Il Gioco dei Sogni - Proteggi il Bambino"
        );

        stage.show();
    }

    private void update() {

        // SPAWN CASUALE
        if (Math.random() < 0.005) {

            nemici.add(
                    new Occhio(
                            WIDTH + 50,
                            Math.random() * (HEIGHT - 150),
                            120,
                            120
                    )
            );
        }

        Iterator<Nemico> iter = nemici.iterator();

        while (iter.hasNext()) {

            Nemico n = iter.next();

            n.update(1.0);

            if (!n.isAlive() || n.getX() < -150) {

                iter.remove();
            }
        }
    }

    private void draw(GraphicsContext gc) {

        gc.clearRect(0, 0, WIDTH, HEIGHT);

        // SFONDO
        if (immagineSfondo != null) {

            gc.drawImage(
                    immagineSfondo,
                    0,
                    0,
                    WIDTH,
                    HEIGHT
            );

        } else {

            gc.setFill(Color.BLACK);

            gc.fillRect(
                    0,
                    0,
                    WIDTH,
                    HEIGHT
            );
        }

        // BAMBINO
        bambino.draw(gc);

        drawHealthBar(
                gc,
                bambino,
                Color.LIME
        );

        // NEMICI
        for (Nemico n : nemici) {

            n.draw(gc);

            drawHealthBar(
                    gc,
                    n,
                    Color.RED
            );
        }
    }

    private void drawHealthBar(
            GraphicsContext gc,
            Sprite s,
            Color color
    ) {

        double barW = s.getDimensionX();

        double currentW =
                (s.health / s.maxHealth) * barW;

        gc.setFill(Color.BLACK);

        gc.fillRect(
                s.getX(),
                s.getY() - 15,
                barW,
                8
        );

        gc.setFill(color);

        gc.fillRect(
                s.getX(),
                s.getY() - 15,
                currentW,
                8
        );
    }

    public static void main(String[] args) {

        launch(args);
    }
}