package app;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import parchis.Jugador;
import parchis.Tablero;

public class ParchisDrawingCanvas extends Canvas {

    private Tablero tablero;

    private GraphicsContext gc;

    private Image background;

    public ParchisDrawingCanvas() {
        super();

        gc = getGraphicsContext2D();
            
        background = new Image(getClass().getResourceAsStream("parchis6.png"));
        
        widthProperty().addListener(evt -> draw());
		heightProperty().addListener(evt -> draw());
    }

    public void draw() {

        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(background, 0, 0, getWidth(), getHeight());
        
    }

    public void nuevoTablero() {

        draw();
        
    }

}