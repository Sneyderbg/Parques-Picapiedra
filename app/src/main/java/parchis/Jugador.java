package parchis;

import java.awt.Color;

/**
 * 
 */
public class Jugador {

    private String nombre;

    private int numJugador;

    private Color color;

    private boolean vivo;

    private int numFichas;

    private Ficha fichas[];

    private Casilla carcel;

    private Casilla cielo;

    /**
     * 
     * @param nombre
     * @param numJugador
     * @param numFichas
     * @param color
     */
    public Jugador(String nombre, int numJugador, Color color, int numFichas) {

        assert (numFichas >= 2 && numFichas <= 4) : "El número de fichas debe estar entre 2 y 4 incluidos.";

        this.nombre = nombre;
        this.numJugador = numJugador;
        this.numFichas = numFichas;
        this.color = color;
        this.vivo = true;
        crearFichas();

    }

    public void crearFichas() {

        fichas = new Ficha[numFichas];

        for (int i = 0; i < numFichas; i++) {
            fichas[i] = new Ficha(this, this.carcel);
        }

    }

    public String getNombre() {
        return nombre;
    }

    public int getNumJugador() {
        return numJugador;
    }

    public Color getColor() {
        return color;
    }

    public boolean estaVivo() {
        return vivo;
    }

    public void setEstaVivo(boolean vivo) {
        this.vivo = vivo;
    }
    
}
