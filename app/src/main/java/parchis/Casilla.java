package parchis;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import parchis.Message.Type;

/**
 * Clase que representa las casillas especiales del parqués, siendo éstas:
 * Cárcel, salida, seguro, entrada.
 */
public class Casilla {

    /**
     * Enumerado para describir el tipo de {@link Casilla}.<br>
     * 
     * - CARCEL.<br>
     * - SALIDA.<br>
     * - SEGURO. <br>
     * - ENTRADA.
     */
    public enum TipoCasilla {
        CARCEL,
        SALIDA,
        SEGURO,
        ENTRADA,
    }

    /**
     * Máximo número de fichas que caben en una casilla.
     */
    private static final int MAX_FICHAS = 4;

    /**
     * Identificador de casilla;
     */
    private int idCasilla;

    /**
     * Atributo que describe el tipo de casilla.
     */
    private TipoCasilla tipoCasilla;

    /**
     * Color de la casilla. Este se asigna automáticamente según el
     * {@link jugadorPadre}.
     */
    private Color color;

    /**
     * Jugador a la que esta casilla pertenece.
     */
    private Jugador jugadorPadre;

    /**
     * Vector que contiene las fichas que actualmente están en esta {@link Casilla}
     */
    private ArrayList<Ficha> fichas;

    /**
     * Constructor. Crea una casilla con los parámetros dados.
     * 
     * @param jugadorPadre Jugador al que pertenece esta {@linkCasilla}.
     * @param IdCasilla    Entero que identifica esta {@link Casilla}.
     * @param tipo         Tipo de {@link Casilla}. Veáse {@link TipoCasilla}.
     */
    public Casilla(Jugador jugadorPadre, int IdCasilla, TipoCasilla tipo) {

        if (idCasilla < 0) {
            assert (tipo == TipoCasilla.CARCEL || tipo == TipoCasilla.ENTRADA);
        }
        this.idCasilla = IdCasilla;
        this.tipoCasilla = tipo;
        this.fichas = new ArrayList<Ficha>();
        this.color = jugadorPadre.getColor();

    }

    /**
     * Inserta una ficha en el vector {@link fichas} siempre que no sobrepase el
     * máximo número de fichas {@link MAX_FICHAS}.
     * 
     * @param ficha {@link Ficha} a insertar en esta {@link Casilla}.
     * @return Un mensaje describiendo el proceso hecho. Veáse {@link Message}.
     */
    public Message insertarFicha(Ficha ficha) {

        if (fichas.size() == MAX_FICHAS) {
            return new Message(Type.ERROR, "No es posible insertar más de %d fichas".formatted(MAX_FICHAS));
        }

        fichas.add(ficha);
        return new Message(Type.SUCCESS, "Ficha insertada en la posición %d".formatted(fichas.size()));

    }

    /**
     * Elimina una ficha del vector {@link fichas} en caso de que ésta esté
     * presente, de lo contrario no hace nada.
     * 
     * @param ficha {@link Ficha} a eliminar del vector {@link fichas}.
     * @return Un mensaje describiendo el proceso hecho. Veáse {@link Message}.
     */
    public Message removerFicha(Ficha ficha) {

        int i = fichas.indexOf(ficha);

        if (i == -1) {
            return new Message(Type.ERROR, "La ficha no existe.");
        }

        fichas.remove(i);
        return new Message(Type.SUCCESS, "ficha eliminada de la posición %d".formatted(i));

    }

    /**
     * Retorna la lista de fichas contenidas en esta casilla.
     * 
     * @return {@link #fichas}.
     */
    public ArrayList<Ficha> getFichas() {
        return fichas;
    }

    /**
     * Retorna el identificador de esta {@link Casilla}.
     * 
     * @return {@link #idCasilla}.
     */
    public int getIdCasilla() {
        return idCasilla;
    }

    /**
     * Retorna el tipo de casilla de esta {@link Casilla}.
     * 
     * @return {@link #tipoCasilla}.
     */
    public TipoCasilla getTipoCasilla() {
        return tipoCasilla;
    }

    /**
     * Retorna el {@link Jugador} al que esta {@link Casilla} pertenece.
     * 
     * @return {@link #jugadorPadre}.
     */
    public Jugador getJugadorPadre() {
        return jugadorPadre;
    }

    /**
     * Retorna el color de esta Casilla.
     * 
     * @return {@link #color}.
     */
    public Color getColor() {
        return color;
    }

}