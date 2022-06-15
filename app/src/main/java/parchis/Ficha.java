package parchis;

import parchis.Message.Type;

public class Ficha {

    private Jugador jugadorPadre;

    private Casilla casilla;

    public Ficha(Jugador jugadorPadre, Casilla casillaInicial) {

        this.jugadorPadre = jugadorPadre;
        this.casilla = casillaInicial;
        
    }

    public Message mover(Casilla c) {

        Message m = c.insertarFicha(this);
        if (m.type == Message.Type.ERROR) {
            return m;
        }

        this.casilla.removerFicha(this);
        this.casilla = c;
        return new Message(Type.SUCCESS, "Ficha movida a la casilla %d".formatted(c.getNumeroCasilla()));
        
    }

    public Jugador getJugadorPadre() {
        return jugadorPadre;
    }

    public Casilla getCasilla() {
        return casilla;
    }

}
