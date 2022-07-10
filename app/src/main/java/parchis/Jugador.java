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
     * Última tirada de dados.
     */
    private int lastRoll;

    /**
     * Determina si este jugador pudo enviar a la cárcel a algún oponente en su
     * anterior jugada, independientemente si envió o no a algún oponente a la
     * cárcel.
     */
    private boolean pudoEnviarACarcel;

    /**
     * Determina si este jugador envió a algún oponente a la cárcel en su anterior
     * jugada.
     */
    private boolean envioACarcel;

    private boolean soploJugadaAnterior;

    /**
     * Número de fichas para cualquier instancia de la clase Jugador.
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
    private CasillaEspecial carcel;

    private CasillaEspecial salida;

    /**
     * Casilla que representa la entrada al cielo (a donde las fichas deben llegar),
     * de
     * este Jugador.
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
        this.pudoEnviarACarcel = false;
        this.envioACarcel = false;
        this.soploJugadaAnterior = false;

        this.carcel = null;
        this.salida = null;
        this.entrada = null;

    }

    /**
     * Crea <i>n</i> fichas para este Jugador, donde <i>n</i> está descrito en
     * {@link #numFichas}.
     */
    public void crearFichas() {

        assert (numFichas >= 2);

        assert (getCarcel() != null && getEntrada() != null)
                : "Se debe asignar tanto la casilla carcel, como la casilla entrada de este jugador";

        fichas = new ArrayList<Ficha>();

        for (int i = 0; i < numFichas; i++) {
            fichas.add(new Ficha(this)); // Se crea cada ficha y se insertan en la carcel
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
    public static void setNumFichas(final int numFichas) {
        Jugador.numFichas = numFichas;
    }

    public static int getNumFichas() {
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

    public boolean isCarcelLlena() {

        return getCarcel().size() == numFichas;

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

    public boolean isEntradaLlena() {

        return getEntrada().size() == numFichas;

    }

    public void setSalida(CasillaEspecial salida) {
        this.salida = salida;
    }

    public CasillaEspecial getSalida() {
        return salida;
    }

    public void setLastRoll(int lastRoll) {
        this.lastRoll = lastRoll;
    }

    public int getLastRoll() {
        return lastRoll;
    }

    public void setPudoEnviarACarcel(boolean pudoEnviarACarcel) {
        this.pudoEnviarACarcel = pudoEnviarACarcel;
    }

    public boolean pudoEnviarACarcel() {
        return pudoEnviarACarcel;
    }

    public void setEnvioACarcel(boolean envioACarcel) {
        this.envioACarcel = envioACarcel;
    }

    public boolean envioACarcel() {
        return envioACarcel;
    }

    public void setSoploJugadaAnterior(boolean soploJugadaAnterior) {
        this.soploJugadaAnterior = soploJugadaAnterior;
    }

    public boolean soploJugadaAnterior() {
        return soploJugadaAnterior;
    }

    @Override
    public String toString() {

        return String.format("Jugador %s, %s", getColor().toString(), getNombre());

    }

}
