package parchis;

import java.util.ArrayList;

import parchis.Casilla.TipoCasilla;
import parchis.Message.Type;

public class Tablero {

    /**
     * Número de jugadores que hay en juego.
     */
    private int numJugadores;

    /**
     * Número de fichas con las los jugadores van a jugar.
     */
    private int numFichasPorJugador;

    /**
     * Esta variable guarda el jugador que actualmente tiene el turno para jugar.
     */
    private Jugador jugadorActual;

    /**
     * Vector de jugadores del tablero.
     * 
     * @see {@link Jugador}
     */
    private ArrayList<Jugador> jugadores;

    /**
     * Vector que contiene las casillas especiales por las cuales se mueven todas
     * las fichas.
     * <p>
     * Este vector no contiene ni las cárceles ni las entradas, ya que solo pasan
     * por ellas las fichas que tiene el mismo color que ellas.
     * 
     * @see {@link Casilla}
     */
    private ArrayList<Casilla> casillas;

    /**
     * Dados del juego.
     * 
     * @see {@link Dado}
     */
    private Dado dado0, dado1;

    /**
     * Controla el estado del tablero.
     */
    private Estado estado;

    /**
     * Constructor. Crea el Tablero con los parámetros dados.
     * <p>
     * Para iniciar el tablero, es necesario añadir todos los jugadores usando el
     * método {@link #addJugador(Jugador)}, además luego de añadirlos es necesario
     * invocar el método {@link #construirTablero()}.
     * 
     * @param numJugadores        Número de jugadores que van a jugar.
     * @param numFichasPorJugador Número de fichas que tendrá cada jugador.
     */
    public Tablero(int numJugadores, int numFichasPorJugador) {

        this.numJugadores = numJugadores;
        this.numFichasPorJugador = numFichasPorJugador;

        this.jugadores = new ArrayList<Jugador>();
        this.dado0 = new Dado();
        this.dado1 = new Dado();
        this.estado = Estado.NoIniciado;
        this.jugadorActual = null;

    }

    /**
     * Constructor. Crea un Tablero con los parámetros dados.
     * <p>
     * Si los jugadores dados en el vector <b>jugadores</b> no concuerda con el
     * parámetro <b>numJugadores</b>, ocurrirá un error y no se creará el tablero.
     * 
     * @param numJugadores        Número de jugadores que van a jugar.
     * @param numFichasPorJugador Número de fichas que tendrá cada jugador.
     * @param jugadores           {@link ArrayList} que contiene la lista de
     *                            jugadores.
     */
    public Tablero(int numJugadores, int numFichasPorJugador, ArrayList<Jugador> jugadores) {

        assert (jugadores.size() == numJugadores)
                : String.format("Deben haber exactamente %d nombres de jugadores", numJugadores);

        this.numJugadores = numJugadores;
        this.numFichasPorJugador = numFichasPorJugador;
        this.dado0 = new Dado();
        this.dado1 = new Dado();
        this.estado = Estado.NoIniciado;
        this.jugadorActual = null;

        construirTablero();

    }

    /**
     * Construye las casillas especiales del tablero, siempre que el vector
     * {@link #jugadores} esté completo.
     */
    public void construirTablero() {

        assert (jugadores.size() == numJugadores) : "Se debe rellenar la lista de jugadores.";

        casillas = new ArrayList<Casilla>();

        Jugador j;
        int numCasilla = 0;

        for (int i = 0; i < numJugadores; i++) {

            j = jugadores.get(i);
            j.setIdJugador(i);

            casillas.add(new Casilla(j, numCasilla, TipoCasilla.SEGURO));
            numCasilla = numCasilla + 5;

            casillas.add(new Casilla(j, numCasilla, TipoCasilla.SALIDA));
            numCasilla = numCasilla + 7;

            casillas.add(new Casilla(j, numCasilla, TipoCasilla.SEGURO));
            numCasilla = numCasilla + 5;

        }

    }

    /**
     * Añade un jugador al vector {@link #jugadores} siempre que no esté completo.
     * 
     * @param jugador {@link Jugador} a añadir.
     * @return {@link Message} que describe el procedimiento hecho.
     */
    public Message addJugador(Jugador jugador) {

        // si el vector está lleno
        if (jugadores.size() == numJugadores) {
            return new Message(Type.ERROR, "No es posible añadir más jugadores");
        }

        jugadores.add(jugador);
        return new Message(Type.SUCCESS, String.format("Jugador añadido en la posición %d", jugadores.size()));

    }

    /**
     * Elimina un jugador del vector {@link #jugadores} si existe, de lo contrario
     * no hace nada.
     * 
     * @param jugador {@link Jugador} a eliminar del vector {@link #jugadores}.
     * @return {@link Message} que describe el procedimiento hecho.
     */
    public Message removerJugador(Jugador jugador) {

        int i = jugadores.indexOf(jugador);

        // si el jugador no existe
        if (i == -1) {
            return new Message(Type.ERROR, String.format("No existe el jugador con nombre %s", jugador.getNombre()));
        }

        jugadores.remove(i);
        return new Message(Type.SUCCESS, String.format("Jugador eliminado de la posición %d", i));

    }

    /**
     * Inicia el tablero siempre que estén todos los jugadores y ya se haya
     * construido el tablero.
     * 
     * @see {@link #construirTablero()}
     */
    public void iniciarTablero() {

        assert (jugadores.size() == numJugadores) : "Asigne todos los jugadores de la partida.";

        assert (casillas.size() > 0) : "Primero construya el tablero";

        // el primer turno para el primer jugador
        jugadorActual = jugadores.get(0);
        estado = Estado.EnJuego;

    }

    public void reiniciarTablero() {

        jugadorActual = null;
        estado = Estado.NoIniciado;

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

    /**
     * Busca la siguiente casilla si la ficha <b>f</b> se mueve <b>n</b> posiciones
     * en el tablero.
     * <p>
     * Si el movimiento que se describe con <b>n</b> no conduce a ninguna casilla
     * especial, entonces se retornará {@code null}.
     * 
     * @param f Ficha a mover.
     * @param n Numero de posiciones a mover.
     * @return {@link Casilla} siguiente si se mueve la ficha en <b>n</b>
     *         posiciones.
     */
    public Casilla siguienteCasilla(Ficha f, int n) {

        Casilla casillaActual, casillaSiguiente;
        casillaActual = f.getCasilla();
        int idxNextCasilla, numNecesario;

        idxNextCasilla = casillas.indexOf(casillaActual);

        if (n > 8) {

            // se comprueba la casilla más alejada
            idxNextCasilla += 2;
            casillaSiguiente = casillas.get(idxNextCasilla);

            // se comprueba si la casilla más alejada es la salida con el mismo color que la
            // ficha, por ende no es posible moverse a esa casilla porque la siguiente
            // casilla es la ENTRADA
            if (casillaSiguiente.getTipoCasilla() == TipoCasilla.SALIDA
                    && f.getJugadorPadre() == casillaActual.getJugadorPadre()) {

                return null;

            }

        } else { // n <= 8

            // se comprueba la casilla más cercana
            idxNextCasilla += 1;
            casillaSiguiente = casillas.get(idxNextCasilla);

            // se comprueba si el movimiento conduce a la entrada del jugador al que
            // pertenece la ficha, o sea, si la siguiente casilla es de tipo SALIDA, la
            // casilla actual es del mismo jugador que la casilla siguiente y el movimiento
            // con n es válido
            if (casillaSiguiente.getTipoCasilla() == TipoCasilla.SALIDA
                    && f.getJugadorPadre() == casillaActual.getJugadorPadre() && n == 8) {

                return f.getJugadorPadre().getEntrada();

            }

        }

        // se calcula el movimiento necesario para pasar de esta casilla especial a la
        // siguiente casilla especial usando sus id's de casilla
        numNecesario = casillaSiguiente.getIdCasilla() - casillaActual.getIdCasilla();
        if (n == numNecesario) {
            return casillaSiguiente;
        }

        // si el movimiento no es válido
        return null;

    }

    /**
     * Tira los dos dados y retorna su suma.
     * 
     * @return Suma de los dos dados.
     */
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

    public Estado isTableroEnJuego() {
        return estado;
    }
    
}

enum Estado {
    NoIniciado,
    EnJuego,
    Finalizado,
}