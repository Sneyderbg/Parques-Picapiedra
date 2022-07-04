package parchis;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import parchis.Casilla.TipoCasilla;

public class Tablero extends Canvas {

    public enum Estado {
        NoIniciado,
        EnJuego,
        Finalizado,
    }

    /**
     * Número de jugadores que hay en juego.
     */
    private int numJugadores;

    /**
     * Número de jugadores no nulos que hay en el vector {@link #jugadores}.
     */
    private int numJugadoresAñadidos;

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
    private Jugador jugadores[];

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
     * Colores a usar según la imagen "parchis6.png", en sentido antihorario.
     */
    private ArrayList<Color> colors;

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

    private Image background;

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

        super();
        this.numJugadores = numJugadores;
        this.numFichasPorJugador = numFichasPorJugador;
        this.numJugadoresAñadidos = 0;
        this.jugadores = new Jugador[6];
        this.dado0 = new Dado();
        this.dado1 = new Dado();
        this.estado = Estado.NoIniciado;
        this.jugadorActual = null;
        this.colors = new ArrayList<Color>();
        this.colors.add(Color.GREEN);
        this.colors.add(Color.ORANGE);
        this.colors.add(Color.PURPLE);
        this.colors.add(Color.RED);
        this.colors.add(Color.YELLOW);
        this.colors.add(Color.BLUE);
        this.background = new Image(getClass().getResourceAsStream("parchis6.png"));

    }

    /**
     * Construye las casillas especiales del tablero, siempre que el vector
     * {@link #jugadores} contenga 2 jugadores como mínimo.
     */
    public void construirTablero() {

        assert (numJugadoresAñadidos == 2) : "Se debe rellenar la lista de jugadores.";

        casillas = new ArrayList<Casilla>();

        Casilla casillaSeguro1, casillaSalida, casillaSeguro2, casillaCarcel, casillaEntrada;

        Jugador j;
        Color colorCasilla;
        int numCasilla, i;
        numCasilla = 0;

        i = 0;

        // 6 ciclos ya que hay seis posiciones de diferente color en el tablero
        while (i < 6) {

            colorCasilla = colors.get(i);
            j = jugadores[i];

            // si hay un jugador en la posición i
            if (j != null) {

                // se crean las casillas carcel y entrada para este jugador
                casillaCarcel = new Casilla(-1, TipoCasilla.CARCEL, colorCasilla);
                casillaEntrada = new Casilla(-1, TipoCasilla.ENTRADA, colorCasilla);

                // se asignan
                j.setCarcel(casillaCarcel);
                j.setEntrada(casillaEntrada);

                // se crean las fichas de este jugador.
                j.crearFichas();

            }

            // se crean y se añaden las casillas con color 'colorCasilla', y se asignan los
            // id's según se ve en la imagen "parchis6.png"

            // primera casilla seguro
            casillaSeguro1 = new Casilla(numCasilla, TipoCasilla.SEGURO, colorCasilla);
            casillas.add(casillaSeguro1);
            numCasilla = numCasilla + 5;

            // casilla salida
            casillaSalida = new Casilla(numCasilla, TipoCasilla.SALIDA, colorCasilla);
            casillas.add(casillaSalida);
            numCasilla = numCasilla + 7;

            // segunda casilla seguro
            casillaSeguro2 = new Casilla(numCasilla, TipoCasilla.SEGURO, colorCasilla);
            casillas.add(casillaSeguro2);
            numCasilla = numCasilla + 5;

            i++;

        }

    }

    /**
     * Añade un jugador al vector {@link #jugadores} siempre que no estén los
     * jugadores necesarios. Si ya existe un jugador con el mismo color, este se
     * reemplazará con el nuevo jugador.
     * 
     * @param jugador {@link Jugador} a añadir o reemplazar.
     * @return {@code true} si se añadió o reemplazó el jugador, {@code false} si ya
     *         están los jugadores necesarios.
     */
    public boolean addJugador(Jugador jugador) {

        Color color = jugador.getColor();
        int idx = colors.indexOf(color);

        // si ya existe un jugador con el mismo color se reemplaza
        if (jugadores[idx] != null) {

            Jugador.setNumFichas(numFichasPorJugador);
            jugadores[idx] = jugador;
            return true;

        }

        // si aún faltan jugadores por añadir
        if (numJugadoresAñadidos < numJugadores) {

            Jugador.setNumFichas(numFichasPorJugador);
            jugadores[idx] = jugador;
            numJugadoresAñadidos++;
            return true;

        }

        // si no se puede añadir más jugadores.
        return false;

    }

    /**
     * Elimina un jugador del vector {@link #jugadores} si existe, de lo contrario
     * no hace nada.
     * 
     * @param jugador {@link Jugador} a eliminar del vector {@link #jugadores}.
     * @return {@code true} si existía el jugador y se eliminó, {@code false} de lo
     *         contrario.
     */
    public boolean removerJugador(Jugador jugador) {

        int i;
        Jugador j;

        for (i = 0; i < numJugadores; i++) {

            j = jugadores[i];

            // si el jugador sí existe, se elimina
            if (j == jugador) {

                jugadores[i] = null;
                numJugadoresAñadidos--;
                return true;

            }

        }

        // si el jugador no existe
        return false;

    }

    /**
     * Inicia el tablero siempre que estén todos los jugadores y ya se haya
     * construido el tablero.
     * 
     * @see {@link #construirTablero()}
     */
    public void iniciarTablero() {

        assert (numJugadoresAñadidos == numJugadores) : "Asigne todos los jugadores de la partida.";

        assert (casillas.size() > 0) : "Primero construya el tablero";

        // el primer turno para el primer jugador
        jugadorActual = jugadores[0];
        estado = Estado.EnJuego;

    }

    public void reiniciarTablero() {

        jugadorActual = null;
        estado = Estado.NoIniciado;

        for (Casilla c : casillas) {
            
            for (Ficha f : c.getFichas()) {

                f.getCarcel().insertarFicha(f);

            }

            c.removerTodasLasFichas();

        }

    }

    /**
     * Mueve la ficha <b>f</b> a la casilla <b>c</b>, enviando a la cárcel a los
     * oponentes según sea el caso.
     * <p>
     * Si la casilla a la que se quiere mover la ficha es:
     * <p>
     * <ul>
     * <li>SEGURO: se envían a todos los oponentes de esa casilla a la cárcel.</li>
     * <li>SALIDA: se envían a todos los oponentes de esa casilla a la cárcel, si la
     * ficha <b>f</b> está saliendo de la cárcel.</li>
     * <li>ENTRADA: si al mover esta ficha a la casilla entrada ya no quedan más
     * fichas en juego, el jugador al que le pertenece la ficha será el
     * ganador.</li>
     * </ul>
     * 
     * @param f La ficha a mover.
     * @param c La casilla a la que se quiere mover la ficha.
     * @return {@code true} si se pudo mover la ficha, {@code false} de lo
     *         contrario.
     */
    public boolean moverFicha(Ficha f, Casilla c) {

        if (c == null) {
            return false;
        }

        switch (c.getTipoCasilla()) {
            case SEGURO:

                for (Ficha fichaOponente : c.getFichas()) {

                    if (fichaOponente.getColor() != f.getColor()) {
                        fichaOponente.encarcelar();
                    }

                }
                break;

            case SALIDA:

                if (f.getCasilla() == f.getCarcel()) {

                    for (Ficha fichasOponente : c.getFichas()) {

                        if (fichasOponente.getColor() != f.getColor()) {
                            fichasOponente.encarcelar();
                        }

                    }

                }
                break;

            case ENTRADA:

                if (f.getEntrada().getFichas().size() == numFichasPorJugador - 1) {

                    f.getJugadorPadre().setGanador(true);

                }
                break;

            default:
                break;
        }

        f.getCasilla().removerFicha(f);
        c.insertarFicha(f);
        return true;

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
    public Casilla siguienteCasillaEspecial(Ficha f) {

        Casilla casillaActual, casillaSiguiente;
        casillaActual = f.getCasilla();
        int idxNextCasilla, numNecesario, sumDados;

        sumDados = dado0.getValor() + dado1.getValor();

        // si la casilla actual es la CARCEL se retornará la casilla SALIDA si los dados
        // forman un par
        if (f.getCasilla() == f.getCarcel() && dado0.getValor() == dado1.getValor()) {
            idxNextCasilla = colors.indexOf(f.getColor());
            idxNextCasilla = idxNextCasilla * 3 + 1;
            return casillas.get(idxNextCasilla);
        }

        idxNextCasilla = casillas.indexOf(casillaActual);

        if (sumDados > 8) {

            // se comprueba la casilla más alejada
            idxNextCasilla = idxNextCasilla + 2;
            casillaSiguiente = casillas.get(idxNextCasilla);

            // se comprueba si la casilla más alejada es la salida con el mismo color que la
            // ficha, por ende no es posible moverse a esa casilla porque la siguiente
            // casilla es la ENTRADA
            if (casillaSiguiente.getTipoCasilla() == TipoCasilla.SALIDA
                    && f.getColor() == casillaActual.getColor()) {

                return null;

            }

        } else { // sumDados <= 8

            // se comprueba la casilla más cercana
            idxNextCasilla = idxNextCasilla + 1;
            casillaSiguiente = casillas.get(idxNextCasilla);

            // se comprueba si el movimiento conduce a la entrada del mismo color que la
            // ficha, o sea, si la siguiente casilla es de tipo SALIDA, laficha actual es
            // del mismo color que la casilla siguiente y el movimiento con los dados es
            // válido
            if (casillaSiguiente.getTipoCasilla() == TipoCasilla.SALIDA
                    && f.getColor() == casillaActual.getColor() && sumDados == 8) {

                return f.getEntrada();

            }

        }

        // se calcula el movimiento necesario para pasar de esta casilla especial a la
        // siguiente casilla especial usando sus id's de casilla
        numNecesario = casillaSiguiente.getIdCasilla() - casillaActual.getIdCasilla();
        numNecesario = numNecesario % 17;
        if (sumDados == numNecesario) {
            return casillaSiguiente;
        }

        // si el movimiento no es válido
        return null;

    }

    /**
     * Tira los dos dados.
     */
    public void tirarDados() {

        dado0.tirar();
        dado1.tirar();

    }

    public Jugador[] getJugadores() {
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

    public Estado getEstado() {
        return estado;
    }

    public void draw() {

        GraphicsContext gc = getGraphicsContext2D();

        double min = Math.min(getWidth(), getHeight());

        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(background, 0, 0, min, min);

        for (Casilla c : casillas) {

            c.drawFichas(gc);

        }

        for (Jugador j : jugadores) {

            if (j != null) {
                j.getCarcel().drawFichas(gc);
                j.getEntrada().drawFichas(gc);
            }

        }

    }

}