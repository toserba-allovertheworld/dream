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

    private final double WIDTH = 800;
    private final double HEIGHT = 800;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Caricamento Immagini
        try {
            immagineSfondo = new Image(getClass().getResourceAsStream("/img/bg.png"));
            spriteSheetBambino = new Image(getClass().getResourceAsStream("/img/Leo.png"));
        } catch (Exception e) {
            System.out.println("Errore caricamento risorse: " + e.getMessage());
        }

        // Inizializzazione Bambino (usiamo la classe anonima per il disegno specifico)
        bambino = new Sprite(50, HEIGHT / 2 - 45, 60, 90) {
            @Override
            public void draw(GraphicsContext gc) {
                if (spriteSheetBambino != null) {
                    double fw = spriteSheetBambino.getWidth() / 3;
                    double fh = spriteSheetBambino.getHeight() / 4;
                    gc.drawImage(spriteSheetBambino, 0, 0, fw, fh, x, y, dimensionX, dimensionY);
                }
            }
            @Override
            public void update(double dt) {} // Sta fermo
        };
        bambino.health = 1000; // Vita alta per il protagonista
        bambino.maxHealth = 1000;

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(System.currentTimeMillis());
                draw(gc);
            }
        };
        timer.start();

        stage.setScene(new Scene(new StackPane(canvas), WIDTH, HEIGHT));
        stage.setTitle("Il Gioco dei Sogni - Proteggi il Bambino");
        stage.show();
    }

    private void update(long currentTime) {
        // Spawn casuale di nemici (es. un Occhio ogni tanto)
        if (Math.random() < 0.005) {
            nemici.add(new Occhio(WIDTH, Math.random() * (HEIGHT - 100), 70, 70));
        }

        Iterator<Nemico> iter = nemici.iterator();
        while (iter.hasNext()) {
            Nemico n = iter.next();
            n.update(1.0); // Movimento

            // Se è un Occhio, esegue l'attacco ad area contro il bambino
            if (n instanceof Occhio) {
                List<Sprite> bersagli = new ArrayList<>();
                bersagli.add(bambino);
                ((Occhio) n).attackArea(bersagli, currentTime);
            }

            if (!n.isAlive() || n.getX() < 0) {
                iter.remove();
            }
        }
    }

    private void draw(GraphicsContext gc) {
        // 1. Sfondo
        if (immagineSfondo != null) {
            gc.drawImage(immagineSfondo, 0, 0, WIDTH, HEIGHT);
        } else {
            gc.setFill(Color.DARKSLATEGRAY);
            gc.fillRect(0, 0, WIDTH, HEIGHT);
        }

        // 2. Bambino
        bambino.draw(gc);
        drawHealthBar(gc, bambino, Color.LIME);

        // 3. Nemici
        for (Nemico n : nemici) {
            n.draw(gc);
            drawHealthBar(gc, n, Color.RED);
        }
    }

    private void drawHealthBar(GraphicsContext gc, Sprite s, Color color) {
        double barW = s.getDimensionX();
        double currentW = (s.health / s.maxHealth) * barW;
        gc.setFill(Color.BLACK);
        gc.fillRect(s.getX(), s.getY() - 15, barW, 8);
        gc.setFill(color);
        gc.fillRect(s.getX(), s.getY() - 15, currentW, 8);
    }

    public static void main(String[] args) { launch(args); }
}