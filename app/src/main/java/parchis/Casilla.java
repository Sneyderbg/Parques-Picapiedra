package parchis;

import parchis.Message.Type;

public class Casilla {

    public enum TipoCasilla {
        CARCEL,
        SALIDA,
        SEGURO,
        ENTRADA,
    }

    private static final int MAX_FICHAS = 4;

    private int numeroCasilla;

    private TipoCasilla tipoCasilla;

    private Jugador jugadorPadre;

    private Ficha fichas[];

    public Casilla(Jugador jugadorPadre, int numeroCasilla, TipoCasilla tipo) {

        this.numeroCasilla = numeroCasilla;
        this.tipoCasilla = tipo;
        this.fichas = new Ficha[MAX_FICHAS];

    }

    public Message insertarFicha(Ficha ficha) {

        int i = 0;
        while (i < MAX_FICHAS && fichas[i] != null) {
            i++;
        }

        if (i == MAX_FICHAS) {
            return new Message(Type.ERROR, "No es posible insertar más de %d fichas".formatted(MAX_FICHAS));
        }

        fichas[i] = ficha;
        return new Message(Type.SUCCESS, "Ficha insertada en la posición %d".formatted(i));

    }

    public Message removerFicha(Ficha ficha) {

        int i = 0;
        while (i < MAX_FICHAS && fichas[i] != ficha) {
            i++;
        }

        if (i == MAX_FICHAS) {
            return new Message(Type.ERROR, "La ficha no existe.");
        }

        fichas[i] = null;
        return new Message(Type.SUCCESS, "ficha eliminada de la posición %d".formatted(i));

    }

    public int getNumeroCasilla() {
        return this.numeroCasilla;
    }

    public TipoCasilla getTipoCasilla() {
        return tipoCasilla;
    }

    public Jugador getJugadorPadre() {
        return jugadorPadre;
    }

    
    
}