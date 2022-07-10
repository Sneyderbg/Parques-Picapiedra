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
    private CasillaEspecial casilla;

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

    /**
     * Asigna la casilla en la cual se debe encontrar esta ficha.
     * <ul>
     * <li>Si la casilla entregada como parámetro no contiene está ficha, se lanzará
     * un {@link IllegalArgumentException}
     * 
     * @param casilla {@link CasillaEspecial} la cual ya debe contener está ficha.
     */
    protected void setCasilla(CasillaEspecial casilla) {

        // if (casilla.getFichas().contains(this)) {
        // throw new IllegalArgumentException(
        // "La casilla debe contener esta ficha para poder asignarse a esta ficha.");
        // }

        this.casilla = casilla;

    }

    /**
     * Retorna la {@link CasillaEspecial} en la cual se encuentra esta Ficha.
     * 
     * @return {@link #casilla}.
     */
    public CasillaEspecial getCasilla() {
        return casilla;
    }

    /**
     * Retorna {@code true} si la ficha se encuentra en la cárcel, {@code false} de
     * lo contrario
     *
     * @return {@code true} o {@code false}.
     */
    public boolean isInCarcel() {
        return casilla == getJugadorPadre().getCarcel();
    }

    /**
     * Retorna {@code true} si la ficha llegó a su casilla ENTRADA, {@code false} de
     * lo contrario.
     * 
     * @return {@code true} o {@code false}.
     */
    public boolean isInEntrada() {
        return casilla == getJugadorPadre().getEntrada();
    }

    /**
     * Retorna el color de esta ficha.
     * 
     * @return {@link #color}.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * sace esta ficha del juego. Dicho de otro modo, elimina esta ficha de la
     * casilla en
     * la cual está, y la añade a su casilla ENTRADA correspondiente.
     */
    public void sacarDelJuego() {

        getCasilla().removerFicha(this);
        getEntrada().insertarFicha(this);

    }

    /**
     * Retorna la casilla CARCEL relacionada a esta ficha.
     * 
     * @return {@link CasillaEspecial} CARCEL.
     */
    public CasillaEspecial getCarcel() {
        return getJugadorPadre().getCarcel();
    }

    /**
     * Retorna la casilla ENTRADA a la cual debe llegar esta ficha.
     * 
     * @return {@link CasillaEspecial} Entrada.
     */
    public CasillaEspecial getEntrada() {
        return getJugadorPadre().getEntrada();
    }

    /**
     * Dibuja esta ficha en la posición dada por <b>(x, y)</b> y con el radio dado.
     * 
     * @param gc       {@link GraphicsContext} usado para dibujar.
     * @param x        Centro X de la ficha.
     * @param y        Centro Y de la ficha.
     * @param diameter Radio de la ficha.
     */
    public void draw(GraphicsContext gc, double x, double y, double diameter) {

        gc.save();
        
        gc.setFill(getColor());
        gc.setStroke(Color.WHITE);

        if (getColor() == Color.YELLOW) {
            gc.setStroke(Color.BLACK);
        }

        gc.fillOval(x, y, diameter, diameter);
        gc.strokeOval(x, y, diameter, diameter);

        gc.restore();

    }

    @Override
    public String toString() {

        return String.format("Ficha %s, in %s", getColor().toString(), getCasilla().toString());

    }

}
