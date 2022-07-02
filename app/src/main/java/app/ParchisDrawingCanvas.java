package app;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import parchis.Casilla;
import parchis.Ficha;
import parchis.Jugador;
import parchis.Casilla.TipoCasilla;
import parchis.Tablero;

public class ParchisDrawingCanvas extends Canvas {

    private final double WIDTH = 800;

    private final double HEIGHT = 800;

    private Tablero tablero;

    private GraphicsContext gc;

    private Image background;

    private ArrayList<Coordenada> coords;

    private ArrayList<Coordenada> carceles;

    private ArrayList<Coordenada> entradas;

    public ParchisDrawingCanvas() {
        super();

        gc = getGraphicsContext2D();

        background = new Image(getClass().getResourceAsStream("parchis6.png"));

        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());

        loadCoords();
        loadCarceles();
        loadEntradas();

    }

    public void draw() {

        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(background, 0, 0, getWidth(), getHeight());

    }

    void drawFichas(Casilla c) {

        int numFichas = c.getFichas().size();
        Coordenada coord;
        Jugador jugador;
        int idJugador;
        Color color;

        switch (c.getTipoCasilla()) {
            case CARCEL:

                jugador = c.getJugadorPadre();
                idJugador = jugador.getIdJugador();

                coord = carceles.get(idJugador);
                color = c.getColor();

                drawFicha(coord, color);
                break;

            default:
                break;
        }

        // switch (numFichas) {
        // case 1:

        // if(c.getTipoCasilla() == TipoCasilla.CARCEL){

        // jugador = c.getJugadorPadre();
        // idJugador = jugador.getIdJugador();

        // coord = carceles.get(idJugador);
        // color = c.getColor();

        // drawFicha(coord, color);
        // }

        // break;

        // case 2:

        // break;

        // default:
        // break;
        // }

    }

    void drawFicha(Coordenada coord, Color color) {

        gc.setFill(color);
        gc.fillOval(transformX(coord.x - 5), transformY(coord.y - 5), transformX(10), transformY(10));

        gc.setStroke(Color.WHITE);
        gc.strokeOval(transformX(coord.x - 5), transformY(coord.y - 5), transformX(10), transformY(10));

    }

    double transformX(double x) {
        return (x * getWidth()) / WIDTH;
    }

    double transformY(double y) {
        return (y * getHeight()) / HEIGHT;
    }

    public void nuevoTablero() {

        draw();

    }

    void loadCoords() {

        coords = new ArrayList<>();

        double x, y;
        String line, xstr, ystr;
        Coordenada coord;

        try {

            String path = getClass().getResource("coords.txt").getFile();
            File file = new File(path);
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {

                line = sc.nextLine();
                xstr = line.split(",")[0];
                ystr = line.split(",")[1];

                x = Double.parseDouble(xstr);
                y = Double.parseDouble(ystr);

                coord = new Coordenada(x, y);
                coords.add(coord);
            }

            sc.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    void loadCarceles() {

        carceles = new ArrayList<>();

        double x, y;
        String line, xstr, ystr;
        Coordenada carcelCoord;

        try {

            String path = getClass().getResource("carceles.txt").getFile();
            File file = new File(path);
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {

                line = sc.nextLine();
                xstr = line.split(",")[0];
                ystr = line.split(",")[1];

                x = Double.parseDouble(xstr);
                y = Double.parseDouble(ystr);

                carcelCoord = new Coordenada(x, y);
                carceles.add(carcelCoord);
            }

            sc.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    void loadEntradas() {

        entradas = new ArrayList<>();

        double x, y;
        String line, xstr, ystr;
        Coordenada entradaCoord;

        try {

            String path = getClass().getResource("entradas.txt").getFile();
            File file = new File(path);
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {

                line = sc.nextLine();
                xstr = line.split(",")[0];
                ystr = line.split(",")[1];

                x = Double.parseDouble(xstr);
                y = Double.parseDouble(ystr);

                entradaCoord = new Coordenada(x, y);
                entradas.add(entradaCoord);
            }

            sc.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}

class Coordenada {

    double x, y;

    public Coordenada(double x, double y) {
        this.x = x;
        this.y = y;
    }

}