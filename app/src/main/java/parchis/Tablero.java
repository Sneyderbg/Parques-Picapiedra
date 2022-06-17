package parchis;

import java.util.ArrayList;

import parchis.Casilla.TipoCasilla;
import parchis.Message.Type;

public class Tablero {

    private static final int SEGURO_TO_SALIDA = 5;

    private static final int SALIDA_TO_SEGURO = 7;

    private static final int SEGURO_TO_SEGURO = 5;

    private static final int SEGURO_TO_ENTRADA = 8;

    private int numJugadores;

    private int numFichasPorJugador;

    private Jugador jugadorActual;

    private ArrayList<Jugador> jugadores;

    private ArrayList<Casilla> casillas;

    private Dado dado0, dado1;

    private boolean tableroEnJuego;

    public Tablero(int numJugadores, int numFichasPorJugador) {

        this.numJugadores = numJugadores;
        this.numFichasPorJugador = numFichasPorJugador;
        this.jugadores = new ArrayList<Jugador>();
        this.dado0 = new Dado();
        this.dado1 = new Dado();
        this.tableroEnJuego = false;
        this.jugadorActual = null;

    }

    public Tablero(int numJugadores, int numFichasPorJugador, ArrayList<Jugador> jugadores) {

        assert (jugadores.size() == numJugadores)
                : "Deben haber exactamente %d nombres de jugadores".formatted(numJugadores);

        this.numJugadores = numJugadores;
        this.numFichasPorJugador = numFichasPorJugador;
        this.dado0 = new Dado();
        this.dado1 = new Dado();
        this.tableroEnJuego = false;
        this.jugadorActual = null;

        contruirTablero();

    }

    public void contruirTablero() {

        assert (jugadores.size() == numJugadores) : "Se debe rellenar la lista de jugadores.";

        casillas = new ArrayList<Casilla>();

        Jugador j;
        int numCasilla = 0;

        for (int i = 0; i < numJugadores; i++) {

            j = jugadores.get(i);
            j.setIdJugador(i);

            casillas.add(new Casilla(j, numCasilla, TipoCasilla.SEGURO));
            numCasilla = numCasilla + SEGURO_TO_SALIDA;

            casillas.add(new Casilla(j, numCasilla, TipoCasilla.SALIDA));
            numCasilla = numCasilla + SALIDA_TO_SEGURO;

            casillas.add(new Casilla(j, numCasilla, TipoCasilla.SEGURO));
            numCasilla = numCasilla + SEGURO_TO_SEGURO;

        }

    }

    public Message addJugador(Jugador jugador) {

        if (jugadores.size() == numJugadores) {
            return new Message(Type.ERROR, "No es posible añadir más jugadores");
        }

        jugadores.add(jugador);
        return new Message(Type.SUCCESS, "Jugador añadido en la posición %d".formatted(jugadores.size()));

    }

    public Message removerJugador(Jugador jugador) {

        int i = jugadores.indexOf(jugador);
        if (i == -1) {
            return new Message(Type.ERROR, "No existe el jugador con nombre %s".formatted(jugador.getNombre()));
        }

        jugadores.remove(i);
        return new Message(Type.SUCCESS, "Jugador eliminado de la posición %d".formatted(i));

    }

    public void iniciarTablero() {

        assert (jugadores.size() == numJugadores) : "Asigne todos los jugadores de la partida.";

        jugadorActual = jugadores.get(0);
        tableroEnJuego = true;

    }

    public void reiniciarTablero() {

        jugadorActual = null;
        tableroEnJuego = false;

        for (Casilla c : casillas) {

            for (Ficha f : c.getFichas()) {

                f.mover(f.getJugadorPadre().getCarcel());

            }

        }

    }

    public Message moverFicha(Ficha f, Casilla c) {

        return f.mover(c);
        
        // TODO completar para que las fichas coman otras fichas.
        
    }

    public Casilla siguienteCasilla(Ficha f, int n) {

        Casilla casillaActual, casillaSiguiente;
        casillaActual = f.getCasilla();

        int idxNextCasilla = casillas.indexOf(casillaActual) + 1;
        casillaSiguiente = casillas.get(idxNextCasilla);

        if (casillaSiguiente.getTipoCasilla() == TipoCasilla.SALIDA) {

            // comprobar si el movimiento conduce al cielo del jugador al que pertenece la
            // ficha
            if (f.getJugadorPadre() == casillaActual.getJugadorPadre() && n == SEGURO_TO_ENTRADA) {

                return f.getJugadorPadre().getEntrada();

            }

        }

        int numNecesario = casillaSiguiente.getIdCasilla() - casillaActual.getIdCasilla();
        numNecesario = numNecesario % (SEGURO_TO_SALIDA + SALIDA_TO_SEGURO + SEGURO_TO_SEGURO);

        if (n == numNecesario) {
            return casillaSiguiente;
        }

        return null;

    }

    public int tirarDados() {

        dado0.tirar();
        dado1.tirar();
        return dado0.getValor() + dado1.getValor();

    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public ArrayList<Casilla> getCasillas() {
        return casillas;
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    public int getNumFichasPorJugador() {
        return numFichasPorJugador;
    }

    public int getNumJugadores() {
        return numJugadores;
    }

    public int getValorDado0() {
        return dado0.getValor();
    }

    public int getValorDado1() {
        return dado1.getValor();
    }

    public boolean isTableroEnJuego() {
        return tableroEnJuego;
    }

}
