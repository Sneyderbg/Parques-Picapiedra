package parchis;

import parchis.Message.Type;

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
     * Constructor. Crea una Ficha con los parámetros dados.
     * 
     * @param jugadorPadre   {@link Jugador} al cual se le asigna esta Ficha.
     * @param casillaInicial {@link Casilla} en la cual se ubicara esta Ficha
     *                       inicialmente.
     */
    public Ficha(Jugador jugadorPadre, Casilla casillaInicial) {

        this.jugadorPadre = jugadorPadre;
        this.casilla = casillaInicial;

    }

    /**
     * Mueve esta Ficha de la casilla actual a la casilla <i><b>c</b></i> entregada
     * como parámetro, siempre y cuando <i><b>c</b></i> no esté llena.
     * 
     * @param c {@link Casilla} a la cual se moverá esta Ficha.
     * @return Un mensaje describiendo el proceso hecho. Veáse {@link Message}.
     */
    public Message mover(Casilla c) {

        Message m = c.insertarFicha(this);
        if (m.type == Message.Type.ERROR) {
            return m;
        }

        this.casilla.removerFicha(this);
        this.casilla = c;
        return new Message(Type.SUCCESS, "Ficha movida a la casilla %d".formatted(c.getIdCasilla()));

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
     * Retorna la {@link Casilla} en la cual se encuentra esta Ficha.
     * 
     * @return {@link #casilla}.
     */
    public Casilla getCasilla() {
        return casilla;
    }

}
