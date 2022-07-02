package parchis;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import parchis.Casilla.TipoCasilla;

/**
 * Esta clase representa un Jugador con sus fichas y casillas que le pertenecen.
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
     * Número de fichas para cualquier Jugador.
     */
    private static int numFichas;

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
     * Casilla que representa la entrada al cielo (a donde las fichas deben llegar),
     * de
     * este Jugador.
     */
    private Casilla entrada;

    /**
     * Describe si este jugador es el ganador.
     */
    private boolean ganador;

    private boolean puedeEnviarACarcel;

    /**
     * Constructor. Crea un jugador con los parámetros dados.
     * 
     * @param nombre    Nombre del Jugador.
     * @param numFichas Número de fichas que tendrá el Jugador.
     * @param color     {@link Color} del Jugador, de sus Fichas y sus Casillas.
     */
    public Jugador(String nombre, Color color, int numFichas) {

        assert (numFichas >= 2 && numFichas <= 4) : "El número de fichas debe estar entre 2 y 4 incluidos.";

        this.nombre = nombre;
        this.color = color;
        this.carcel = new Casilla(this, -1, TipoCasilla.CARCEL);
        this.entrada = new Casilla(this, -1, TipoCasilla.ENTRADA);
        this.ganador = false;
        this.puedeEnviarACarcel = false;

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

    /**
     * Asigna el número de fichas que tendrá cada jugador que se cree.
     * 
     * @param numFichas Número de fichas que tendrá cada jugador.
     */
    public static void setNumFichas(int numFichas) {
        Jugador.numFichas = numFichas;
    }

    /**
     * Retorna la casilla cárcel de este Jugador.
     * 
     * @return {@link Casilla} cárcel.
     */
    public Casilla getCarcel() {
        return carcel;
    }

    /**
     * Retorna la casilla entrada de este Jugador.
     * 
     * @return {@link Casilla} entrada.
     */
    public Casilla getEntrada() {
        return entrada;
    }

    public void setGanador(boolean ganador) {
        this.ganador = ganador;
    }
    
    public boolean isGanador() {
        return ganador;
    }

    public void setPuedeEnviarACarcel(boolean puedeEnviarACarcel) {
        this.puedeEnviarACarcel = puedeEnviarACarcel;
    }

    public boolean puedeEnviarACarcel(){
        return this.puedeEnviarACarcel;
    }

}
