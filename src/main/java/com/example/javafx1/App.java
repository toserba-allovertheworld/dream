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

    private final double WIDTH = 1920;
    private final double HEIGHT = 1080;

    private List<Difesa> difese = new ArrayList<>();
    private List<Nemico> nemici = new ArrayList<>();

    private Griglia griglia;
    private boolean teddySelected = false;

    private int nemiciGeneratiFinora = 0;

    private final double[] spawnPointsOcchio = {115, 280, 445, 610, 775};
    private final double[] spawnPointsOmbra = {150, 320, 460, 650, 790};
    private final double[] spawnPointsClown = {175, 340, 500, 660, 790};
    private final double[] spawnPointsFrammento = {160, 330, 500, 660, 790};

    private Image immagineSfondo;
    private Image orsoIcon;

    private long lastSpawnTime = 0;
    private long spawnDelay = 7500;

    private Leo bambino;
    private EssenzaBar essenzaBar;

    public void spawnNemico() {
        switch ((int) (Math.random() * 4)) {
            case 0:
                nemici.add(new Occhio(1920, getSpawnY(0), 120, 120, bambino));
                break;
            case 1:
                nemici.add(new Ombra(1920 - 250, getSpawnY(1), 120, 120, bambino));
                break;
            case 2:
                nemici.add(new Clown(1920, getSpawnY(2), 120, 120, bambino));
                break;
            case 3:
                nemici.add(new Frammento(1920, getSpawnY(3), 120, 120, bambino));
                break;
        }
    }

    public double getSpawnY(int nemico) {
        int indicePunto = (int) (Math.random() * 4);
        switch (nemico) {
            case 0:
                return spawnPointsOcchio[indicePunto];
            case 1:
                return spawnPointsOmbra[indicePunto];
            case 2:
                return spawnPointsClown[indicePunto];
            case 3:
                return spawnPointsFrammento[indicePunto];
            default:
                return -1;
        }
    }

    @Override
    public void start(Stage stage) {

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        griglia = new Griglia(5, 9, 147, 165, 485, 150);

        try {
            immagineSfondo = new Image(getClass().getResourceAsStream("/img/bg.png"));
            orsoIcon = new Image(getClass().getResourceAsStream("/img/Teddy.png"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        bambino = new Leo(150, 150, 240, 320);
        bambino.health = 1000;
        bambino.maxHealth = 1000;
        essenzaBar = new EssenzaBar();

        canvas.setOnMouseClicked(e -> {
            double mouseX = e.getX();
            double mouseY = e.getY();
            double boxSize = 120;
            double boxX = 20;
            double boxY = HEIGHT - boxSize - 20;

            // CLICK CARTA TEDDY
            if (mouseX >= boxX &&
                    mouseX <= boxX + boxSize &&
                    mouseY >= boxY &&
                    mouseY <= boxY + boxSize) {
                if (essenzaBar.getEssenza() >= 25) {
                    teddySelected = true;
                }
                return;
            }

            // TEDDY SELEZIONATO
            if (teddySelected) {
                int row = griglia.getRow(mouseY);
                int col = griglia.getCol(mouseX);

                if (griglia.isInsideGrid(row, col) && !griglia.isOccupied(row, col) && essenzaBar.useEssenza(25)) {
                    double x = griglia.getCellX(col) + 10;
                    double y = griglia.getCellY(row) + 20;
                    difese.add(new Teddy(x, y));
                    griglia.occupyCell(row, col);
                    teddySelected = false;
                }
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw(gc);
            }
        };

        timer.start();

        stage.setScene(new Scene(new StackPane(canvas), WIDTH, HEIGHT));
        stage.setTitle("Il Gioco dei Sogni - Proteggi il Bambino");
        stage.show();
    }

    private void update() {

        long currentTime = System.currentTimeMillis();

        if (nemiciGeneratiFinora < 50) {

            if (currentTime - lastSpawnTime >= spawnDelay) {
                spawnNemico();
                lastSpawnTime = currentTime;
                nemiciGeneratiFinora++;
            }
        }

        Iterator<Nemico> iter = nemici.iterator();

        while (iter.hasNext()) {
            Nemico n = iter.next();
            n.update(1.0);

            if (!n.isAlive() || n.getX() < -150) {
                iter.remove();
            }
        }

        bambino.update(1.0);

        essenzaBar.update(0.016);

        for (Difesa d : difese) {
            d.update(1.0);
        }
    }

    private void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        if (immagineSfondo != null) {
            gc.drawImage(immagineSfondo, 0, 0, WIDTH, HEIGHT);
        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, WIDTH, HEIGHT);
        }

        bambino.draw(gc);
        griglia.draw(gc);

        drawHealthBar(gc, bambino, Color.LIME);

        for (Nemico n : nemici) {
            n.draw(gc);
            //drawHealthBar(gc, n, Color.RED); // <-- Secondo me è brutta da vedere e mi fa impazzire vedere che i nemici non vengono spawnati perfettamente
        }

        for (Difesa d : difese) {
            d.draw(gc);
            drawHealthBar(gc, d, Color.CYAN);
        }

        essenzaBar.draw(gc);

        double boxSize = 120;
        double boxX = 20;
        double boxY = HEIGHT - boxSize - 20;

        boolean sbloccato = essenzaBar.getEssenza() >= 50;

        gc.setFill(sbloccato ? Color.DARKGREEN : Color.GRAY);
        gc.fillRect(boxX, boxY, boxSize, boxSize);

        gc.setStroke(Color.BLACK);
        gc.strokeRect(boxX, boxY, boxSize, boxSize);

        if (orsoIcon != null) {

            double imgSize = boxSize * 0.8;

            double imgX = boxX + (boxSize - imgSize) / 2;
            double imgY = boxY + (boxSize - imgSize) / 2;

            if (!sbloccato) {
                gc.setGlobalAlpha(0.35);
            }

            gc.drawImage(orsoIcon, imgX, imgY, imgSize, imgSize);

            gc.setGlobalAlpha(1.0);
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

    public static void main(String[] args) {
        launch(args);
    }
}