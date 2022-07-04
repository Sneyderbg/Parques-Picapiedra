package parchis;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Clase que representa las fichas con las que se juega en el Parqués.
 */
public class Ficha {

    /**
     * {@link Jugador} al cual pertenece esta Ficha.
     */
    private Jugador jugadorPadre;

    /**
     * {@Casilla} en la cual se encuentra esta Ficha.
     */
    private Casilla casilla;

    /**
     * Color de esta Ficha.
     */
    private Color color;

    /**
     * Constructor. Crea una Ficha con los parámetros dados, y la inserta en la
     * casilla carcel del jugador al cual pertence.
     * 
     * @param jugadorPadre {@link Jugador} al cual se le asigna esta Ficha.
     */
    public Ficha(Jugador jugadorPadre) {

        this.jugadorPadre = jugadorPadre;
        this.casilla = jugadorPadre.getCarcel();
        this.color = jugadorPadre.getColor();

        this.casilla.insertarFicha(this);

    }

    /**
     * Retorna el {@link Jugador} al cual pertenece esta Ficha.
     * 
     * @return {@link #jugadorPadre}.
     */
    public Jugador getJugadorPadre() {
        return jugadorPadre;
    }

    public void setCasilla(Casilla casilla) {
        this.casilla = casilla;
    }

    /**
     * Retorna la {@link Casilla} en la cual se encuentra esta Ficha.
     * 
     * @return {@link #casilla}.
     */
    public Casilla getCasilla() {
        return casilla;
    }

    /**
     * Retorna {@code true} si la ficha se encuentra en la cárcel, {@code false} de
     * lo contrario
     *
     * @return {@code true} o {@code false}.
     */
    public boolean isPrisionera() {
        return casilla == getJugadorPadre().getCarcel();
    }

    /**
     * Retorna {@code true} si la ficha llegó al cielo, {@code false} de lo
     * contrario.
     * 
     * @return {@code true} o {@code false}.
     */
    public boolean isAngel() {
        return casilla == getJugadorPadre().getEntrada();
    }

    public Color getColor() {
        return this.color;
    }

    public void encarcelar() {
        getCasilla().removerFicha(this);
        getCarcel().insertarFicha(this);
    }

    public Casilla getCarcel() {
        return getJugadorPadre().getCarcel();
    }

    public Casilla getEntrada() {
        return getJugadorPadre().getEntrada();
    }

    public void draw(GraphicsContext gc, double centerX, double centerY, double radius) {

        gc.setFill(getColor());
        gc.setStroke(Color.WHITE);

        gc.fillOval(centerX, centerY, radius, radius);
        gc.strokeOval(centerX, centerY, radius, radius);

    }

}
