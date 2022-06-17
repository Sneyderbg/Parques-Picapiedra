package parchis;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import parchis.Casilla.TipoCasilla;

/**
 * 
 */
public class Jugador {

    /**
     * String que describe el nombre de este Jugador.
     */
    private String nombre;

    /**
     * Entero con el cuál se identifica este Jugador.
     */
    private int idJugador;

    /**
     * Color con el cuál se mostrará las fichas y casillas de este Jugador.
     */
    private Color color;

    /**
     * Número de fichas que tiene este Jugador.
     */
    private int numFichas;

    /**
     * Vector que contiene las fichas pertenecientes a este Jugador.
     */
    private ArrayList<Ficha> fichas;

    /**
     * Casilla que representa la cárcel (en donde las fichas están inicialmente), de
     * este Jugador.
     */
    private Casilla carcel;

    /**
     * Casilla que representa la entrada al cielo (a donde las fichas deben llegar), de
     * este Jugador.
     */
    private Casilla entrada;

    /**
     * Constructor. Crea un jugador con los parámetros dados.
     * 
     * @param nombre    Nombre del Jugador.
     * @param idJugador Identificador del Jugador.
     * @param numFichas Número de fichas que tendrá el Jugador.
     * @param color     {@link Color} del Jugador, de sus Fichas y sus Casillas.
     */
    public Jugador(String nombre, Color color, int numFichas) {

        assert (numFichas >= 2 && numFichas <= 4) : "El número de fichas debe estar entre 2 y 4 incluidos.";

        this.nombre = nombre;
        this.numFichas = numFichas;
        this.color = color;
        this.carcel = new Casilla(this, -1, TipoCasilla.CARCEL);
        this.entrada = new Casilla(this, -1, TipoCasilla.ENTRADA);
        crearFichas();

    }

    /**
     * Crea <i>n</i> fichas para este Jugador, donde <i>n</i> está descrito en
     * {@link #numFichas}.
     */
    public void crearFichas() {

        fichas = new ArrayList<Ficha>();

        for (int i = 0; i < numFichas; i++) {
            fichas.add(new Ficha(this, this.carcel));
        }

    }

    /**
     * Retorna el nombre de este Jugador.
     * 
     * @return {@link #nombre}.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el número con el cuál se identifica a este jugador.
     * 
     * @param idJugador Id a asignar al jugador.
     */
    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }
    
    /**
     * Retorna el identificador de este Jugador.
     * 
     * @return {@link #idJugador}.
     */
    public int getIdJugador() {
        return idJugador;
    }

    /**
     * Retorna el color de este Jugador.
     * 
     * @return {@link #color}.
     */
    public Color getColor() {
        return color;
    }

    public Casilla getCarcel() {
        return carcel;
    }

    public Casilla getEntrada() {
        return entrada;
    }

}
