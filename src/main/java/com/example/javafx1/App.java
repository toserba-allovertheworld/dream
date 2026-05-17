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
    private boolean teddySelected = false; // Flag per sapere se il giocatore ha cliccato sulla carta Teddy

    private int nemiciGeneratiFinora = 0; // Contatore per non spawnare più di 50 nemici

    // Coordinate Y di spawn per ogni tipo di nemico (5 possibili posizioni verticali)
    private final double[] spawnPointsOcchio = {115, 280, 445, 610, 775};
    private final double[] spawnPointsOmbra = {150, 320, 460, 650, 790};
    private final double[] spawnPointsClown = {175, 340, 500, 660, 790};
    private final double[] spawnPointsFrammento = {160, 330, 500, 660, 790};

    private Image immagineSfondo;
    private Image orsoIcon;

    private long lastSpawnTime = 0; // Timestamp dell'ultimo nemico spawnato
    private long spawnDelay = 7500; // Millisecondi tra uno spawn e l'altro (7.5 secondi)

    private Leo bambino;
    private EssenzaBar essenzaBar;

    // Crea un nemico casuale e lo aggiunge alla lista
    public void spawnNemico() {
        switch ((int) (Math.random() * 4)) { // Genera numero casuale 0-3 per tipo nemico
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

    // Ritorna una coordinata Y casuale per il tipo di nemico specificato
    public double getSpawnY(int nemico) {
        int indicePunto = (int) (Math.random() * 4); // Sceglie indice casuale tra 0-3
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
        griglia = new Griglia(5, 9, 147, 165, 485, 150); // 5 righe, 9 colonne, cell 147x165px

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

        // Gestisce i click del mouse per posizionare difese e selezionare Teddy
        canvas.setOnMouseClicked(e -> {
            double mouseX = e.getX();
            double mouseY = e.getY();
            double boxSize = 120;
            double boxX = 20;
            double boxY = HEIGHT - boxSize - 20; // Box in basso a sinistra

            // CLICK CARTA TEDDY
            if (mouseX >= boxX &&
                    mouseX <= boxX + boxSize &&
                    mouseY >= boxY &&
                    mouseY <= boxY + boxSize) {
                if (essenzaBar.getEssenza() >= 25) { // Controlla se ha abbastanza essenza (25)
                    teddySelected = true;
                }
                return;
            }

            // TEDDY SELEZIONATO - Posizionamento su griglia
            if (teddySelected) {
                int row = griglia.getRow(mouseY);
                int col = griglia.getCol(mouseX);

                // Controlla: dentro griglia && cella vuota && può pagare (25 essenza)
                if (griglia.isInsideGrid(row, col) && !griglia.isOccupied(row, col) && essenzaBar.useEssenza(25)) {
                    double x = griglia.getCellX(col) + 10;
                    double y = griglia.getCellY(row) + 20;
                    difese.add(new Teddy(x, y));
                    griglia.occupyCell(row, col);
                    teddySelected = false; // Deseleziona dopo il piazzamento
                }
            }
        });

        // Loop di gioco: update logica e rendering
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

    // Aggiorna logica di gioco: spawn nemici, movimento, collisioni
    private void update() {

        long currentTime = System.currentTimeMillis();

        if (nemiciGeneratiFinora < 50) { // Spawn finché non raggiunge 50 nemici totali

            if (currentTime - lastSpawnTime >= spawnDelay) { // Se è passato spawnDelay ms
                spawnNemico();
                lastSpawnTime = currentTime;
                nemiciGeneratiFinora++;
            }
        }

        // Aggiorna nemici e li rimuove se morti o usciti dallo schermo
        Iterator<Nemico> iter = nemici.iterator();

        while (iter.hasNext()) {
            Nemico n = iter.next();
            n.update(1.0);

            if (!n.isAlive() || n.getX() < -150) { // Se morto oppure (||) troppo a sinistra
                iter.remove();
            }
        }

        bambino.update(1.0);
        essenzaBar.update(0.016); // ~60 FPS (1/60 ≈ 0.016)

        for (Difesa d : difese) {
            d.update(1.0);
        }
    }

    // Disegna tutti gli elementi sulla canvas
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

        // Disegna il box della carta Teddy in basso a sinistra
        double boxSize = 120;
        double boxX = 20;
        double boxY = HEIGHT - boxSize - 20;
        boolean sbloccato = essenzaBar.getEssenza() >= 50; // Carta è colorata solo se essenza >= 50

        gc.setFill(sbloccato ? Color.DARKGREEN : Color.GRAY); // Ternario: verde se sbloccato, grigio se no
        gc.fillRect(boxX, boxY, boxSize, boxSize);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(boxX, boxY, boxSize, boxSize);

        if (orsoIcon != null) {
            double imgSize = boxSize * 0.8; // Icona al 80% della dimensione del box
            double imgX = boxX + (boxSize - imgSize) / 2; // Centra icona orizzontalmente
            double imgY = boxY + (boxSize - imgSize) / 2; // Centra icona verticalmente

            if (!sbloccato) {
                gc.setGlobalAlpha(0.35); // Trasparenza 35% se bloccato (non ha abbastanza essenza)
            }

            gc.drawImage(orsoIcon, imgX, imgY, imgSize, imgSize);
            gc.setGlobalAlpha(1.0); // Ripristina opacità al 100%
        }
    }

    // Disegna barra della vita sopra uno sprite (nemico/difesa/bambino)
    private void drawHealthBar(GraphicsContext gc, Sprite s, Color color) {
        double barW = s.getDimensionX(); // Larghezza barra = larghezza sprite
        double currentW = (s.health / s.maxHealth) * barW; // Percentuale di vita rimanente
        gc.setFill(Color.BLACK);
        gc.fillRect(s.getX(), s.getY() - 15, barW, 8); // Sfondo nero
        gc.setFill(color);
        gc.fillRect(s.getX(), s.getY() - 15, currentW, 8); // Barra colorata (riempimento)
    }

    public static void main(String[] args) {
        launch(args);
    }
}