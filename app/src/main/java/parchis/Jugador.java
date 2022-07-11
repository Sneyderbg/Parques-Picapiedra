package parchis;

import java.util.ArrayList;

import javafx.scene.paint.Color;

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
     * Color con el cuál se mostrará las fichas y casillas de este Jugador.
     */
    private Color color;

    /**
     * Contiene el último número que sacó este jugador al tirar los dados.
     */
    private int lastRoll;

    /**
     * Determina si este jugador envió a algún oponente a la cárcel en su anterior
     * jugada.
     */
    private boolean envioACarcel;

    /**
     * Determina si este jugador ya sopló al jugador anterior, en el turno actual.
     */
    private boolean soploJugadaAnterior;

    /**
     * Número de fichas para cualquier instancia de la clase Jugador.
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
    private CasillaEspecial carcel;

    /**
     * Casilla que representa la salida que pertenece a este Jugador.
     */
    private CasillaEspecial salida;

    /**
     * Casilla que representa la entrada al cielo (a donde las fichas deben llegar),
     * de este Jugador.
     */
    private CasillaEspecial entrada;

    /**
     * Constructor. Crea un jugador con los parámetros dados.
     * 
     * @param nombre Nombre del Jugador.
     * @param color  {@link Color} del Jugador, de sus Fichas y sus Casillas.
     */
    public Jugador(String nombre, Color color) {

        this.nombre = nombre;
        this.color = color;
        this.lastRoll = 0;
        this.envioACarcel = false;
        this.soploJugadaAnterior = false;

        this.carcel = null;
        this.salida = null;
        this.entrada = null;

    }

    /**
     * Crea <i>n</i> fichas para este Jugador, donde <i>n</i> es {@link #numFichas}.
     */
    public void crearFichas() {

        // minimo 1 ficha
        assert (numFichas > 0) : "el mínimo de fichas debe ser 1.";

        assert (getCarcel() != null && getEntrada() != null && getSalida() != null)
                : "se debe asignar todas las casillas relacionadas a este jugador.";

        fichas = new ArrayList<Ficha>();
        Ficha f;

        // Se crean las ficha y se insertan en la carcel
        for (int i = 0; i < numFichas; i++) {
            f = new Ficha(this);
            fichas.add(f);
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
    public void setNumFichas(final int numFichas) {
        this.numFichas = numFichas;
    }

    /**
     * Retorna el número de fichas de este Jugador.
     * 
     * @return {@link #numFichas}.
     */
    public int getNumFichas() {
        return numFichas;
    }

    /**
     * Establece la {@link CasillaEspecial} de tipo CARCEL que le corresponde a este
     * Jugador.
     * 
     * @param carcel {@link CasillaEspecial} CARCEL de este Jugador.
     */
    public void setCarcel(CasillaEspecial carcel) {
        this.carcel = carcel;
    }

    /**
     * Retorna el vector fichas de este Jugador.
     * 
     * @return {@link #fichas}.
     */
    public ArrayList<Ficha> getFichas() {
        return fichas;
    }

    /**
     * Retorna la casilla cárcel de este Jugador.
     * 
     * @return {@link CasillaEspecial} cárcel.
     */
    public CasillaEspecial getCarcel() {
        return carcel;
    }

    /**
     * Combrueba si la casilla {@link #carcel} contiene <i>n</i> fichas, donde
     * <i>n</i> es {@link #numFichas}.
     * 
     * @return {@code true} si la cárcel contiene {@code numFichas}, {@code false}
     *         de lo contrario.
     */
    public boolean isCarcelLlena() {

        return getCarcel().length() == numFichas;

    }

    /**
     * Establece la {@link CasillaEspecial} de tipo ENTRADA que le corresponde a
     * este Jugador.
     * 
     * @param entrada {@link CasillaEspecial} ENTRADA de este Jugador.
     */
    public void setEntrada(CasillaEspecial entrada) {
        this.entrada = entrada;
    }

    /**
     * Retorna la casilla entrada de este Jugador.
     * 
     * @return {@link CasillaEspecial} entrada.
     */
    public CasillaEspecial getEntrada() {
        return entrada;
    }

    /**
     * Comprueba si la casilla {@link #entrada} contiene <i>n</i> fichas donde
     * <i>n</i> es {@link #numFichas}.
     * 
     * @return {@code true} si la entrada contiene {@code numFichas}, {@code false}
     *         de lo contrario.
     */
    public boolean isEntradaLlena() {

        return getEntrada().length() == numFichas;

    }

    /**
     * Establece la {@link CasillaEspecial} de tipo SALIDA que le corresponde a este
     * Jugador.
     * 
     * @param salida {@link CasillaEspecial} SALIDA de este Jugador.
     */
    public void setSalida(CasillaEspecial salida) {
        this.salida = salida;
    }

    /**
     * Retorna la {@link CasillaEspecial} SALIDA de este Jugador.
     * 
     * @return {@link #salida}.
     */
    public CasillaEspecial getSalida() {
        return salida;
    }

    /**
     * Establece el último número que sacó este Jugador.
     * 
     * @param lastRoll Último número sacado al tirar los dados.
     */
    public void setLastRoll(int lastRoll) {
        this.lastRoll = lastRoll;
    }

    /**
     * Retorna el último número que sacó este Jugador.
     * 
     * @return Último número sacado al tirar los dados.
     */
    public int getLastRoll() {
        return lastRoll;
    }

    /**
     * Cambia la variable {@link #envioACarcel}.
     * 
     * @param envioACarcel valor.
     */
    public void setEnvioACarcel(boolean envioACarcel) {
        this.envioACarcel = envioACarcel;
    }

    /**
     * Retorna la variable {@link #envioACarcel}.
     * 
     * @return {@code true} si este jugador envió a algún oponente a la cárcel en su
     *         jugada anterior, {@code false} de lo contrario.
     */
    public boolean envioACarcel() {
        return envioACarcel;
    }

    /**
     * Cambia la variable {@link #soploJugadaAnterior}.
     * 
     * @param soploJugadaAnterior valor.
     */
    public void setSoploJugadaAnterior(boolean soploJugadaAnterior) {
        this.soploJugadaAnterior = soploJugadaAnterior;
    }

    /**
     * Retorna la variable {@link #soploJugadaAnterior}.
     * 
     * @return {@code true} si este jugador ya sopló cuando tenía su turno.
     */
    public boolean soploJugadaAnterior() {
        return soploJugadaAnterior;
    }

    @Override
    public String toString() {

        return String.format("Jugador[%s, %s]", getColor().toString(), getNombre());

    }

}
