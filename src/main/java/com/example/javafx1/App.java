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
    private Leo leo;

    // Entità di gioco
    private List<Nemico> nemici = new ArrayList<>();
    private Sprite bambino; // Supponiamo tu abbia una classe per il bambino o usa Sprite

    // Dimensioni finestra
    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;

    private long lastFrameTime = System.currentTimeMillis();

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // 1. Caricamento Background
        try {
            background = new Image(getClass().getResourceAsStream("/img/bg.png"));
        } catch (Exception e) {
            System.out.println("Background non trovato, uso un colore solido.");
        }

        try {
            var url = getClass().getResource("/img/Leo.png");

            System.out.println(url);

            leo = new Leo(url.toExternalForm(), 50, HEIGHT / 2.0 - 50);
        } catch (Exception e) {
            System.out.println("Leo non trovato: " + e.getMessage());
        }

        // 2. Inizializzazione Gioco
        inizializzaPartita();

        // 3. Game Loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long nowNano) {
                long currentTime = System.currentTimeMillis();
                double deltaTime = (currentTime - lastFrameTime) / 1000.0;
                lastFrameTime = currentTime;

                update(deltaTime, currentTime);
                draw(gc);
            }
        };
        timer.start();

        StackPane root = new StackPane(canvas);
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        stage.setTitle("Il Custode dei Sogni");
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
    }

    private void update(double deltaTime, long nowMilli) {
        // *** MODIFICATO *** AGGIUNTO: Aggiorna Leo (cambia posa ogni 10 secondi)
        if (leo != null) {
            leo.update(deltaTime);
        }

        // Generazione nemici casuali (esempio)
        /*if (Math.random() < 0.01) {
            nemici.add(new Occhio(WIDTH, Math.random() * (HEIGHT - 50)));
        }*/

        // Lista di bersagli
        List<Sprite> bersagli = new ArrayList<>();
        bersagli.add(bambino);

        Iterator<Nemico> iter = nemici.iterator();
        while (iter.hasNext()) {
            Nemico n = iter.next();
            n.update(deltaTime); // Muove il nemico

            // Rimuovi se morto o fuori schermo
            if (!n.isAlive() || n.getX() < -100) {
                iter.remove();
            }
        }
    }

    private void draw(GraphicsContext gc) {
        // A. Disegna Background
        if (background != null) {
            gc.drawImage(background, 0, 0, WIDTH, HEIGHT);
        } else {
            gc.setFill(Color.web("#1A1A3E"));
            gc.fillRect(0, 0, WIDTH, HEIGHT);
        }

        // B. Disegna Entità
        // *** MODIFICATO *** AGGIUNTO: Disegna Leo (con l'animazione aggiornata)
        if (leo != null) {
            leo.draw(gc);
        }

        // Disegna il bambino
        bambino.draw(gc);

        // Disegna nemici
        for (Nemico n : nemici) {
            n.draw(gc);
            drawHealthBar(gc, n);
        }

        // C. Disegna UI
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
        double lifePercent = s.health / s.maxHealth;
        gc.setFill(Color.LIME);
        gc.fillRect(x, y, barWidth * lifePercent, barHeight);
    }

    private void drawHealthBarBambino(GraphicsContext gc) {
        // Una barra più grande in alto per il protagonista
        gc.setFill(Color.BLACK);
        gc.fillRect(20, 20, 200, 20);
        gc.setFill(Color.LIME);
        gc.fillRect(22, 22, 196, 16);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(20, 20, 200, 20);
    }

    public static void main(String[] args) {
        launch(args);
    }
}