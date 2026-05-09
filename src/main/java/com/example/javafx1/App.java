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

    // Risorse Grafiche
    private Image background;

    // Entità di gioco
    private List<Nemico> nemici = new ArrayList<>();
    private Sprite bambino; // Supponiamo tu abbia una classe per il bambino o usa Sprite

    // Dimensioni finestra
    private final int WIDTH = 800;
    private final int HEIGHT = 800;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // 1. Caricamento Background
        // Assicurati che il file sia in src/main/resources/immagini/background.png
        try {
            background = new Image(getClass().getResourceAsStream("/immagini/background.png"));
        } catch (Exception e) {
            System.out.println("Background non trovato, uso un colore solido.");
        }

        // 2. Inizializzazione Gioco
        inizializzaPartita();

        // 3. Game Loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long nowNano) {
                long nowMilli = System.currentTimeMillis();
                double deltaTime = 1.0; // In un gioco reale calcoleresti il tempo passato

                update(deltaTime, nowMilli);
                draw(gc);
            }
        };
        timer.start();

        StackPane root = new StackPane(canvas);
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        stage.setTitle("Proteggi il Bambino - Difesa dall'Occhio");
        stage.show();
    }

    private void inizializzaPartita() {
        // Creiamo il bambino (posizione centrale a sinistra)
        // Usiamo i parametri di Sprite: x, y, dimX, dimY
        bambino = new Sprite(50, HEIGHT / 2.0 - 25, 50, 50) {
            @Override public void draw(GraphicsContext gc) { gc.setFill(Color.YELLOW); gc.fillRect(x, y, dimensionX, dimensionY); }
            @Override public void update(double dt) {}
        };
        bambino.setAlive(true);
        // Impostiamo vita iniziale dal file Sprite
        // (Nota: dovresti aggiungere dei setter o gestire i campi protected)
    }

    private void update(double deltaTime, long nowMilli) {
        // Generazione nemici casuali (esempio)
        if (Math.random() < 0.01) {
            nemici.add(new Occhio(WIDTH, Math.random() * (HEIGHT - 50), 60, 60));
        }

        // Lista di bersagli per l'attacco ad area dell'Occhio
        List<Sprite> bersagli = new ArrayList<>();
        bersagli.add(bambino);

        Iterator<Nemico> iter = nemici.iterator();
        while (iter.hasNext()) {
            Nemico n = iter.next();
            n.update(deltaTime); // Muove il nemico

            // Logica specifica per l'Occhio (Danno ad Area)
            if (n instanceof Occhio) {
                ((Occhio) n).attackArea(bersagli, nowMilli);
            }

            // Rimuovi se morto o fuori schermo
            if (!n.isAlive() || n.getX() < -100) {
                iter.hasNext();
                iter.remove();
            }
        }
    }

    private void draw(GraphicsContext gc) {
        // A. Disegna Background
        if (background != null) {
            gc.drawImage(background, 0, 0, WIDTH, HEIGHT);
        } else {
            gc.setFill(Color.DARKSLATEGRAY);
            gc.fillRect(0, 0, WIDTH, HEIGHT);
        }

        // B. Disegna Entità
        bambino.draw(gc);
        for (Nemico n : nemici) {
            n.draw(gc);
            drawHealthBar(gc, n); // Barra vita per ogni nemico
        }

        // C. Disegna UI (Barra vita del bambino)
        drawHealthBarBambino(gc);
    }

    // Metodo generico per disegnare le barre della vita sopra gli sprite
    private void drawHealthBar(GraphicsContext gc, Sprite s) {
        double barWidth = s.getDimensionX();
        double barHeight = 5;
        double x = s.getX();
        double y = s.getY() - 10; // Posizionata sopra lo sprite

        // Sfondo barra (rosso)
        gc.setFill(Color.RED);
        gc.fillRect(x, y, barWidth, barHeight);

        // Vita attuale (verde)
        // Calcolo basato su health e maxHealth definiti in Sprite
        // Esempio ipotizzando valori di default
        double lifePercent = 1.0; // Sostituisci con s.health / s.maxHealth
        gc.setFill(Color.LIME);
        gc.fillRect(x, y, barWidth * lifePercent, barHeight);
    }

    private void drawHealthBarBambino(GraphicsContext gc) {
        // Una barra più grande in alto per il protagonista
        gc.setFill(Color.BLACK);
        gc.fillRect(20, 20, 200, 20);
        gc.setFill(Color.LIME);
        gc.fillRect(22, 22, 196, 16); // Qui userai la vita reale del bambino
        gc.setStroke(Color.WHITE);
        gc.strokeRect(20, 20, 200, 20);
    }

    public static void main(String[] args) {
        launch(args);
    }
}