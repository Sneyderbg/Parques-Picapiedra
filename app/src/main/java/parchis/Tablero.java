package parchis;

import java.awt.Color;

import parchis.Casilla.TipoCasilla;
import parchis.Message.Type;

public class Tablero {

    private int numJugadores;

    private int numFichasPorJugador;

    private Jugador jugadorActual;

    private Casilla casillas[];

    private Jugador jugadores[];

    private Dado dados[];

    private boolean tableroEnJuego;

    public Tablero(int numJugadores, int numFichasPorJugador) {

        this.numJugadores = numJugadores;
        this.numFichasPorJugador = numFichasPorJugador;
        this.jugadores = new Jugador[numJugadores];
        this.dados = new Dado[2];
        this.tableroEnJuego = false;

    }

    public Tablero(int numJugadores, int numFichasPorJugador, Jugador jugadores[]) {

        assert (jugadores.length == numJugadores)
                : "Deben haber exactamente %d nombres de jugadores".formatted(numJugadores);

        this.numJugadores = numJugadores;
        this.numFichasPorJugador = numFichasPorJugador;
        this.dados = new Dado[2];
        this.jugadores = jugadores;
        this.tableroEnJuego = false;

        contruirTablero();

    }

    public void contruirTablero() {

        casillas = new Casilla[numJugadores * 3];
        int count = 0;

        for (int i = 0; i < numJugadores; i++) {
            casillas[i] = new Casilla(jugadores[i], count, TipoCasilla.SEGURO);
            count += 5;
            casillas[i + 1] = new Casilla(jugadores[i], count, TipoCasilla.SALIDA);
            count += 7;
            casillas[i + 2] = new Casilla(jugadores[i], count, TipoCasilla.SEGURO);
            count += 5;
        }

    }

    public Message addJugador(Jugador j) {

        int i = 0;
        while (i < numJugadores && jugadores[i] != null) {
            i++;
        }

        if (i == numJugadores) {
            return new Message(Type.ERROR, "No es posible añadir más jugadores");
        }

        jugadores[i] = j;
        return new Message(Type.SUCCESS, "Jugador añadido en la posición %d".formatted(i));

    }

    public Message removerJugador(Jugador j) {

        int i = 0;
        while (i < numJugadores && jugadores[i] != j) {
            i++;
        }

        if (i == numJugadores) {
            return new Message(Type.ERROR, "No existe el jugador con nombre %s".formatted(j.getNombre()));
        }

        jugadores[i] = null;
        return new Message(Type.SUCCESS, "Jugador eliminado de la posición %d".formatted(i));

    }

    public Message iniciarTablero() {

        if (jugadores == null) {
            return new Message(Type.ERROR, "Asigne los jugadores de la partida.");
        }

        for (int i = 0; i < jugadores.length; i++) {
            if (jugadores[i] == null) {
                return new Message(Type.ERROR, "Asigne todos los jugadores de la partida.");
            }
        }

        jugadorActual = jugadores[0];
        tableroEnJuego = true;
        return new Message(Type.SUCCESS, "Tablero iniciado.");

    }

    public Message reiniciarTablero() {

        jugadorActual = null;
        tableroEnJuego = false;
        return new Message(Type.SUCCESS, "Tablero reiniciado.");
        
    }

    public int tirarDados(){

        dados[0].tirar();
        dados[1].tirar();
        return dados[0].getValor() + dados[1].getValor();
        
    }

    public boolean comprobarMovimiento(Ficha ficha, int n){
        //TODO: implement
        return false;
    }

}
