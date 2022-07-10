package parchis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import parchis.CasillaEspecial.TipoCasilla;

public class Tablero extends Thread {

    public enum Estado {
        NO_INICIADO,
        EN_JUEGO,
        FINALIZADO,
    }

    private enum TipoDeMovimiento {

        ELIGIENDO_PRIMER_JUGADOR,
        PRIMERA_RONDA,
        MOVIENDO_FICHA,
        ENCARCELANDO_OPONENTE_ANTERIOR,
        SACANDO_FICHA_DEL_JUEGO,

    }

    /**
     * Número de jugadores que hay en juego.
     */
    private final int numJugadores;

    /**
     * Número de jugadores no nulos que hay en el vector {@link #jugadores}.
     */
    private int numJugadoresAñadidos;

    /**
     * Número de fichas con las los jugadores van a jugar.
     */
    private final int numFichasPorJugador;

    private Jugador primerJugador;

    private Jugador jugadorAnterior;

    /**
     * Esta variable guarda el jugador que actualmente tiene el turno para jugar.
     */
    private Jugador jugadorActual;

    /**
     * Jugador ganador del juego.
     */
    private Jugador jugadorGanador;

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
     * @see {@link CasillaEspecial}
     */
    private ArrayList<CasillaEspecial> casillas;

    /**
     * Colores a usar según la imagen "parchis6.png", en sentido antihorario.
     */
    private static ArrayList<Color> colors;

    /**
     * Dados del juego.
     * 
     * @see {@link Dados}
     */
    private Dados dados;

    /**
     * Controla el estado del tablero.
     */
    private Estado estado;

    private TipoDeMovimiento tipoDeMovimiento;

    private int rollCount;

    private int pairsInARow;

    private final Image background;

    private boolean waitForTirarDados, waitForPresionarCasilla;

    private final Canvas drawingCanvas;

    private final Label infoLabel;

    private final Label nextPlayerLabel;

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
    public Tablero(int numJugadores, int numFichasPorJugador, Canvas drawingCanvas, ImageView dado0ImageView,
            ImageView dado1ImageView, Label infoLabel, Label nextPlayerLabel) {

        super();

        assert (numJugadores >= 2 && numJugadores <= 6);
        assert (numFichasPorJugador >= 2 && numFichasPorJugador <= 4);

        this.numJugadores = numJugadores;
        this.numFichasPorJugador = numFichasPorJugador;
        this.numJugadoresAñadidos = 0;
        this.estado = Estado.NO_INICIADO;
        this.primerJugador = null;
        this.jugadorAnterior = null;
        this.jugadorActual = null;
        this.jugadorGanador = null;

        this.jugadores = new Jugador[6];
        this.dados = new Dados(dado0ImageView, dado1ImageView, 2, 3);
        this.rollCount = 0;
        this.pairsInARow = 0;
        this.waitForPresionarCasilla = false;
        this.waitForTirarDados = false;

        this.background = new Image(getClass().getResourceAsStream("parchis6.png"));
        this.drawingCanvas = drawingCanvas;
        this.infoLabel = infoLabel;
        this.nextPlayerLabel = nextPlayerLabel;

        if (colors == null) {

            colors = new ArrayList<>();
            Collections.addAll(colors,
                    Color.GREEN, Color.ORANGE, Color.PURPLE, Color.RED, Color.YELLOW, Color.BLUE);

        }

    }

    /**
     * Construye las casillas especiales del tablero, siempre que el vector
     * {@link #jugadores} contenga 2 jugadores como mínimo, y no se haya construido
     * antes el tablero.
     * <p>
     * Este método es llamado en {@link #addJugador(Jugador)} cuando se añadan los
     * suficientes jugadores, según {@link #numJugadores}.
     * <p>
     * {@code numJugadoresAñadidos == numJugadores}
     */
    private void construirTablero() {

        assert (numJugadoresAñadidos == 2) : "Se debe rellenar la lista de jugadores.";
        assert (casillas == null) : "tablero ya construido";

        casillas = new ArrayList<CasillaEspecial>();

        CasillaEspecial casillaSeguro1, casillaSalida, casillaSeguro2, casillaCarcel, casillaEntrada;

        Jugador j;
        Color colorCasilla;
        int numCasilla, i;
        numCasilla = 0;

        i = 0;

        // 6 ciclos ya que hay seis posiciones de diferente color en el tablero
        while (i < 6) {

            colorCasilla = colors.get(i);
            j = jugadores[i];

            // se crean y se añaden las casillas con color 'colorCasilla', y se asignan los
            // id's según se ve en la imagen "parchis6.png"

            // primera casilla seguro
            casillaSeguro1 = new CasillaEspecial(numCasilla, TipoCasilla.SEGURO, colorCasilla);
            casillas.add(casillaSeguro1);
            casillaSeguro1.setId("seg1-player" + i);
            numCasilla = numCasilla + 5;

            // casilla salida
            casillaSalida = new CasillaEspecial(numCasilla, TipoCasilla.SALIDA, colorCasilla);
            casillas.add(casillaSalida);
            casillaSalida.setId("sal-player" + i);
            numCasilla = numCasilla + 7;

            // segunda casilla seguro
            casillaSeguro2 = new CasillaEspecial(numCasilla, TipoCasilla.SEGURO, colorCasilla);
            casillas.add(casillaSeguro2);
            casillaSeguro2.setId("seg2-player" + i);
            numCasilla = numCasilla + 5;

            // si hay un jugador en la posición i
            if (j != null) {

                // se crean las casillas carcel y entrada para este jugador
                casillaCarcel = new CasillaEspecial(-1, TipoCasilla.CARCEL, colorCasilla);
                casillaEntrada = new CasillaEspecial(-1, TipoCasilla.ENTRADA, colorCasilla);
                casillaCarcel.setId("car-player" + i);
                casillaEntrada.setId("ent-player" + i);

                // se asignan
                j.setCarcel(casillaCarcel);
                j.setEntrada(casillaEntrada);
                j.setSalida(casillaSalida);

                // se crean las fichas de este jugador.
                j.crearFichas();

            }

            i++;

        }

    }

    /**
     * Añade un jugador al vector {@link #jugadores} siempre que no estén los
     * jugadores necesarios. Si ya existe un jugador con el mismo color, este se
     * reemplazará con el nuevo jugador.
     * <p>
     * Si se añade al jugador, y este es el último, o sea,
     * {@code numJugadoresAñadidos == numJugadores}, se invoca al método
     * {@link #construirTablero()}.
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

            // si se añade el último jugador, se contruyen las casillas del tablero
            if (numJugadoresAñadidos == numJugadores) {
                construirTablero();
            }
            return true;

        }

        // si no se puede añadir más jugadores.
        return false;

    }

    /**
     * Actualiza el siguiente jugador no nulo al jugador actual.
     * <p>
     * si {@link #jugadorActual} es {@code null}, se asignará el siguiente jugador
     * no nulo del vector al campo {@link #jugadorActual}.
     */
    private void siguienteJugador() {

        assert (numJugadoresAñadidos == numJugadores) : "Asigne todos los jugadores de la partida.";

        int i = 0;

        // si no se ha asignado el jugador actual
        if (jugadorActual == null) {

            // se busca el primero no nulo
            while (i < jugadores.length && jugadores[i] == null) {
                i++;
            }

            jugadorActual = jugadores[i];
            jugadorActual.setSoploJugadaAnterior(false);
            nextPlayerLabel.setText("Turno de " + jugadorActual.getNombre());
            return;
        }

        // se avanza hasta el jugadorActual
        while (i < jugadores.length && jugadores[i] != jugadorActual) {
            i++;
        }

        i++;
        // se busca el siguiente no nulo
        while (i < jugadores.length && jugadores[i] == null) {
            i++;
        }

        // si no existe un jugador no nulo antes de terminar el vector
        if (i >= jugadores.length) {

            // se busca el siguiente no nulo desde el inicio del vector.
            i = 0;
            while (i < jugadores.length && jugadores[i] == null) {
                i++;
            }

        }

        // se actualiza el jugadorAnterior
        jugadorAnterior = jugadorActual;

        // se actualiza el jugadorActual
        jugadorActual = jugadores[i];

        // se reinicia el campo 'soploJugadaAnterior'
        jugadorActual.setSoploJugadaAnterior(false);

        nextPlayerLabel.setText("Turno de " + jugadorActual.getNombre());

    }

    /**
     * Mueve la ficha <b>f</b> a la casilla <b>c</b>, enviando a la cárcel a los
     * oponentes según sea el caso.
     * <p>
     * Si la casilla a la que se quiere mover la ficha es:
     * <p>
     * <ul>
     * <li>SEGURO: se envían a todos los oponentes de esa casilla a sus
     * cárceles.</li>
     * <li>SALIDA: se envían a todos los oponentes de esa casilla a sus cárceles, si
     * la ficha <b>f</b> está saliendo de la cárcel.</li>
     * <li>ENTRADA: si al mover esta ficha a la casilla entrada ya no quedan más
     * fichas en juego, el jugador al que le pertenece la ficha será el
     * ganador.</li>
     * </ul>
     * 
     * <p>
     * Si en cualquier caso, el jugador actual envió alguna ficha a la cárcel, se
     * actualizará su campo {@link Jugador#envioACarcel}.
     * 
     * @param f La ficha a mover.
     * @param c La casilla a la que se quiere mover la ficha.
     * @return {@code true} si se pudo mover la ficha, {@code false} de lo
     *         contrario.
     */
    public boolean moverFicha(Ficha f, CasillaEspecial c) {

        boolean seEncarceloAlgunaFicha = false;

        // inicialmente el jugador no ha enviado a ningún oponente a la cárcel
        jugadorActual.setEnvioACarcel(false);

        if (c == null) {
            return false;
        }

        // casos para el tipo de casilla a la cual se quiere mover la ficha 'f'
        switch (c.getTipoCasilla()) {
            case SEGURO:

                // se encarcelan todas las fichas que sean de diferente color a la ficha 'f'
                seEncarceloAlgunaFicha = c.encarcelarTodasExceptuando(f.getColor());

                // se actualiza el campo 'envioACarcel' del jugadorActual
                jugadorActual.setEnvioACarcel(seEncarceloAlgunaFicha);
                break;

            case SALIDA:

                // si el movimiento es CARCEL -> SALIDA
                if (f.getCasilla() == f.getCarcel()) {

                    // se encarcelan todas las fichas que sean de diferente color a la ficha 'f'
                    seEncarceloAlgunaFicha = c.encarcelarTodasExceptuando(f.getColor());

                    // se actualiza el campo 'envioACarcel' del jugadorActual
                    jugadorActual.setEnvioACarcel(seEncarceloAlgunaFicha);
                }
                break;

            default:
                break;
        }

        // se mueve esta ficha de la casilla actual a la casilla 'c'
        f.getCasilla().removerFicha(f);
        c.insertarFicha(f);

        if (jugadorActual.isEntradaLlena()) {

            jugadorGanador = jugadorActual;
            finalizarTablero();

        }

        return true;

    }

    private boolean liberarFichas(Jugador j) {

        CasillaEspecial casillaCarcel, casillaSalida;

        casillaCarcel = j.getCarcel();
        casillaSalida = j.getSalida();

        if (casillaCarcel.size() == 0) {
            return false;
        }

        // se encarcelan todas las fichas que sean de diferente color a la ficha 'f'
        boolean seEncarceloAlgunaFicha = casillaSalida.encarcelarTodasExceptuando(j.getColor());

        // se actualiza el campo 'envioACarcel' del jugadorActual
        jugadorActual.setEnvioACarcel(seEncarceloAlgunaFicha);

        // se mueven todas las fichas de la carcel a la salida
        casillaSalida.moverTodasLasFichasDe(casillaCarcel);

        return true;

    }

    /**
     * Busca la siguiente casilla si la ficha <b>f</b> se mueve <b>n</b> posiciones
     * en el tablero, donde <b>n</b> es la suma de los dados.
     * <p>
     * Si el movimiento que se describe con <b>n</b> no conduce a ninguna casilla
     * especial, entonces se retornará {@code null}.
     * 
     * @param f Ficha a mover.
     * @return {@link CasillaEspecial} siguiente si se mueve la ficha en <b>n</b>
     *         posiciones.
     */
    public CasillaEspecial siguienteCasillaEspecial(Ficha f) {

        CasillaEspecial casillaActual, casillaSiguiente;
        casillaActual = f.getCasilla();
        int idxNextCasilla, numNecesario;

        // si la casilla actual es la CARCEL, se retornará la casilla SALIDA si y solo
        // si los dados forman un par
        if (f.getCasilla() == f.getCarcel() && dados.esPar()) {
            idxNextCasilla = colors.indexOf(f.getColor());
            idxNextCasilla = idxNextCasilla * 3 + 1;
            return casillas.get(idxNextCasilla);
        }

        if (f.getCarcel() == f.getEntrada()) {

            return null;

        }

        idxNextCasilla = casillas.indexOf(casillaActual);

        if (dados.getSuma() > 8) {

            // se comprueba la casilla más alejada
            idxNextCasilla = (idxNextCasilla + 2) % casillas.size();
            casillaSiguiente = casillas.get(idxNextCasilla);

            // se comprueba si la casilla más alejada es la salida o el seguro con el mismo
            // color que la
            // ficha, por ende no es posible moverse a esa casilla porque la siguiente
            // casilla es la ENTRADA
            if ((casillaSiguiente.getTipoCasilla() == TipoCasilla.SALIDA
                    || casillaSiguiente.getTipoCasilla() == TipoCasilla.SEGURO)
                    && f.getColor() == casillaSiguiente.getColor()) {

                return null;

            }

        } else { // sumDados <= 8

            // se comprueba la casilla más cercana
            idxNextCasilla = (idxNextCasilla + 1) % casillas.size();
            casillaSiguiente = casillas.get(idxNextCasilla);

            // se comprueba si el movimiento conduce a la entrada del mismo color que la
            // ficha, o sea, si la siguiente casilla es de tipo SALIDA, laficha actual es
            // del mismo color que la casilla siguiente y el movimiento con los dados es
            // válido
            if (casillaSiguiente.getTipoCasilla() == TipoCasilla.SALIDA
                    && f.getColor() == casillaActual.getColor() && dados.getSuma() == 8) {

                return f.getEntrada();

            }

        }

        // se calcula el movimiento necesario para pasar de esta casilla especial a la
        // siguiente casilla especial usando sus id's de casilla
        numNecesario = casillaSiguiente.getIdCasilla() - casillaActual.getIdCasilla();

        // modulo positivo
        numNecesario = numNecesario % 17;
        if (numNecesario < 0) {
            numNecesario += 17;
        }
        if (dados.getSuma() == numNecesario) {
            return casillaSiguiente;
        }

        // si el movimiento no es válido
        return null;

    }

    /**
     * Comprueba si el jugador actual puede enviar a algún oponente a la cárcel con
     * cualquiera de sus fichas. Luego actualiza el campo
     * {@link Jugador#pudoEnviarACarcel}.
     */
    public boolean checkPuedeEncarcelarOponentes() {

        CasillaEspecial casillaSiguiente;
        Ficha f;
        Iterator<Ficha> it = jugadorActual.getFichas().iterator();

        while (it.hasNext()) {

            f = it.next();
            casillaSiguiente = siguienteCasillaEspecial(f);
            if (casillaSiguiente != null && casillaSiguiente.contieneFichasOponentes(jugadorActual)) {

                return true;

            }

        }

        return false;

    }

    /**
     * Comprueba si el jugador actual puede mover alguna ficha a una casilla
     * especial, si se mueve <b>n</b> posiciones, donde <b>n</b> es la suma de los
     * dos dados.
     * 
     * @return {@code true} si hay un posible movimiento por parte del jugador
     *         actual, {@code false} de lo contrario.
     */
    public boolean posibleMovimiento() {

        assert (jugadorActual != null) : "Jugador actual no asignado";

        for (Ficha f : jugadorActual.getFichas()) {

            if (siguienteCasillaEspecial(f) != null) {
                return true;
            }

        }

        return false;

    }

    public Jugador[] getJugadores() {
        return jugadores;
    }

    public ArrayList<CasillaEspecial> getCasillas() {
        return casillas;
    }

    public void setJugadorAnterior(Jugador jugadorAnterior) {
        this.jugadorAnterior = jugadorAnterior;
    }

    public Jugador getJugadorAnterior() {
        return jugadorAnterior;
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    public void setJugadorActual(Jugador jugadorActual) {
        this.jugadorActual = jugadorActual;
    }

    public void setPrimerJugador(Jugador primerJugador) {
        this.primerJugador = primerJugador;
    }

    public Jugador getPrimerJugador() {
        return primerJugador;
    }

    public int getNumFichasPorJugador() {
        return numFichasPorJugador;
    }

    public int getNumJugadores() {
        return numJugadores;
    }

    public Dados getDados() {

        return dados;
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

    public Estado getEstado() {
        return estado;
    }

    /**
     * Inicia el tablero siempre que estén todos los jugadores ya añadidos.
     * {@code numJugadoresAñadidos == numJugadores}.
     * 
     * @see {@link #addJugador(Jugador)}
     */
    public void iniciarTablero() {

        assert (numJugadoresAñadidos == numJugadores) : "Asigne todos los jugadores de la partida.";

        infoLabel.setText("Eligiendo primer jugador, presione Roll para empezar.");
        siguienteJugador();
        estado = Estado.EN_JUEGO;
        tipoDeMovimiento = TipoDeMovimiento.ELIGIENDO_PRIMER_JUGADOR;
        waitForTirarDados = true;

    }

    /**
     * Reinicia las fichas del tablero a sus posiciones iniciales.
     */
    public void reiniciarTablero() {

        jugadorGanador = null;
        jugadorAnterior = null;
        jugadorActual = null;
        estado = Estado.NO_INICIADO;

        rollCount = 0;
        pairsInARow = 0;

        dados.reset();

        // se mueven todas las fichas de todas las casillas a sus carceles
        for (CasillaEspecial c : casillas) {

            c.encarcelarTodasLasFichas();

        }

        // lo mismo pero con todas las entradas de cada jugador
        for (Jugador j : jugadores) {

            if (j == null) {

                continue;

            }

            j.getEntrada().encarcelarTodasLasFichas();

        }

    }

    public void finalizarTablero() {

        estado = Estado.NO_INICIADO;
        tipoDeMovimiento = null;

    }

    /**
     * Tira los dos dados y evalua el estado del tablero.
     * <p>
     * Casos:
     * <ul>
     * <li><b>ELIGIENDO_PRIMER_JUGADOR</b>: Se compara el número que saca el jugador
     * actual con el número de sacó el jugador anterior (si hubo anterior), y asigna
     * el primer jugador al mayor y más reciente entre ellos</li>
     * <li><b>PRIMERA_RONDA</b>: Cada jugador tendrá 3 oportunidades de sacar un
     * par,
     * para así liberar a todas sus fichas. Después de usar sus 3 oportunidades o
     * sacar par y liberar sus fichas, se cambia el turno al siguiente jugador del
     * vector {@link #jugadores} (sentido antihorario en el tablero).</li>
     * <li><b>MOVIENDO_FICHA</b>: Este es el estado más común. Cuando se tiren los
     * dados, se compruba si se puede mover alguna ficha, además si saca par, el
     * mismo jugador puede volver a tirar, y si saca par 3 veces seguidas, el
     * jugador podrá sacar una ficha del juego.</li>
     * </ul>
     * <p>
     * Los demás casos se controlan al presionar alguna casilla o al soplar al
     * oponente anterior.
     * 
     * @param infoLabel          Label el cual se actualizará para dar información
     *                           después de tirar los dados.
     * @param currentPlayerLabel Label el cual se actualizará para dar información
     *                           acerca del jugador actual.
     * @see {@link #presionarCasilla(CasillaEspecial, Label, Label)}
     */
    public void tirarDados(String values) {

        assert (numJugadoresAñadidos == numJugadores) : "Asigne todos los jugadores de la partida.";
        assert (getEstado() == Estado.EN_JUEGO)
                : "No se puede realizar ninguna acción si no se ha iniciado o reiniciado el tablero";

        // si no se esperaba tirar los dados se cancela la operación
        if (!waitForTirarDados) {

            infoLabel.setText(infoLabel.getText() + "!!!");
            return;

        }

        // se aumenta en 1 el conteo de tiradas.
        rollCount++;

        dados.tirar();

        dados.setOnFinished(evt -> evaluarDados(values));

    }

    private void evaluarDados(String values) {

        if (values != null) { // debug: se reemplazan los valores de los dados

            int v0, v1;

            try {

                v0 = Integer.parseInt(values.split("\\.")[0]);
                v1 = Integer.parseInt(values.split("\\.")[1]);

                dados.setValores(v0, v1);

            } catch (Exception e) {

                e.printStackTrace();
                return;

            }

        }

        // se actualiza el número que saco este jugador como la ultima tirada.
        jugadorActual.setLastRoll(dados.getSuma());

        // si saca par, se aumenta el conteo de pares seguidos
        if (tipoDeMovimiento == TipoDeMovimiento.MOVIENDO_FICHA && dados.esPar()) {

            pairsInARow++;

        } else { // se reinicia el conteo

            pairsInARow = 0;

        }

        // estado del tablero
        switch (tipoDeMovimiento) {

            case ELIGIENDO_PRIMER_JUGADOR:

                // si es el primer jugador en tirar los dados
                if (getJugadorAnterior() == null) {

                    // el jugador actual es el que ha sacado el mayor número, ya que no hay primer
                    // jugador
                    setPrimerJugador(getJugadorActual());

                } else { // no es el primer jugador, o sea que ya tiraron los dados anteriormente

                    // si el número que saco el jugador actual es mayor o igual al del primer
                    // jugador
                    if (jugadorActual.getLastRoll() >= primerJugador.getLastRoll()) {

                        // se actualiza
                        setPrimerJugador(getJugadorActual());

                    } // else: no se actualiza

                }

                infoLabel.setText("El jugador que más ha sacado es " + primerJugador.getNombre());
                nextPlayerLabel.setText("Turno de " + primerJugador.getNombre());

                // si todos lo jugadores ya tiraron sus dados en la elección del primer jugador
                if (rollCount >= getNumJugadores()) {

                    // se cambia el estado a PRIMERA_RONDA, y se reinicia el conteo de tiradas y
                    // pares seguidos
                    tipoDeMovimiento = TipoDeMovimiento.PRIMERA_RONDA;
                    rollCount = 0;
                    pairsInARow = 0;

                    // se pasa el turno al primer jugador, que ya se debió haber elegido, y se
                    // reinicia el jugador anterior
                    jugadorAnterior = null;
                    jugadorActual = primerJugador;

                    infoLabel.setText("El primer turno va para: " + primerJugador.getNombre());
                    break;

                } // else: no se ha terminado de elegir el primer jugador

                // se pasa el turno al siguiente jugador
                siguienteJugador();
                break;

            case PRIMERA_RONDA:

                // si se sacó par
                if (dados.esPar()) {

                    infoLabel.setText("Presione su carcel para sacar sus fichas");

                    // se espera a que se presione una casilla
                    waitForPresionarCasilla = true;
                    waitForTirarDados = false;
                    break;

                } // else: no se sacó par

                // si ya se tiro 3 veces en la primera ronda
                if (rollCount >= 3) {

                    // se reinicia el conteo de tiradas, y se pasa el turno al siguiente jugador
                    rollCount = 0;
                    infoLabel.setText("Suerte para la próxima. Turno del siguiente jugador.");
                    siguienteJugador();

                    // si después de cambiar el turno al siguiente jugador, este es el primer
                    // jugador, significa que ya se terminó la primera ronda
                    if (jugadorActual == primerJugador) {

                        tipoDeMovimiento = TipoDeMovimiento.MOVIENDO_FICHA;
                        infoLabel.setText("Primera ronda terminada. Turno del siguiente jugador.");

                    }

                    break;

                } // else: no se ha tirado 3 veces

                // continua tirando el mismo jugador hasta que tire 3 veces o saque par
                infoLabel.setText("Vuelve a tirar los dados");
                break;

            case MOVIENDO_FICHA:

                // si sacó 3 pares seguidos
                if (pairsInARow >= 3) {

                    // se espera a que se presione la casilla en la cual está la ficha que quiere
                    // sacar del juego
                    tipoDeMovimiento = TipoDeMovimiento.SACANDO_FICHA_DEL_JUEGO;
                    infoLabel.setText("Sacaste 3 pares seguidos. Presiona una casilla para sacar una ficha.");

                    // se espera a que se presione una casilla
                    waitForPresionarCasilla = true;
                    waitForTirarDados = false;
                    break;

                } // else: no se ha sacado 3 pares seguidos

                // si no hay movimiento posible con ninguna ficha
                if (!posibleMovimiento()) {

                    infoLabel.setText(
                            "No hay ninguna ficha que se pueda mover.");

                    // si no sacó par
                    if (pairsInARow == 0) {

                        // se pasa el turno
                        siguienteJugador();
                        infoLabel.setText(infoLabel.getText() + " Turno del siguiente jugador.");
                        break;

                    } // else: se sacó par, por tanto el turno es para el mismo jugador

                    infoLabel.setText(infoLabel.getText() + " Vuelve a tirar los dados.");
                    break;

                } // else: hay al menos una ficha que se puede mover

                jugadorActual.setPudoEnviarACarcel(checkPuedeEncarcelarOponentes());

                // si saca par y todas las fichas están en la cárcel
                if (pairsInARow > 0 && jugadorActual.isCarcelLlena()) {

                    infoLabel.setText("Sacaste par, puedes liberar tus fichas :)");

                    // se espera a que se presione una casilla
                    waitForPresionarCasilla = true;
                    waitForTirarDados = false;
                    break;

                } // else: si no saca par o la cárcel no está llena

                infoLabel.setText("Presiona una casilla para mover una ficha.");

                // se espera a que se presione una casilla
                waitForPresionarCasilla = true;
                waitForTirarDados = false;
                break;

            default:
                break;
        }

        dados.resetAnim();
        draw();

    }

    /**
     * Evalúa el movimiento que se quiere hacer según el estado del tablero, y la
     * casilla presionada.
     * <p>
     * Casos:
     * <ul>
     * <li><b>PRIMERA_RONDA</b>:
     * 
     * @param casillaPresionada
     * @param infoLabel
     * @param currentPlayerLabel
     */
    public void presionarCasilla(CasillaEspecial casillaPresionada) {

        assert (getEstado() == Estado.EN_JUEGO)
                : "No se puede realizar ninguna acción si no se ha iniciado o reiniciado el tablero";

        // si no se esperaba por presionar una casilla se cancela la operación
        if (!waitForPresionarCasilla) {
            infoLabel.setText("Debes tirar los dados!!!");
            return;
        }

        CasillaEspecial casillaSiguiente;
        Ficha ficha;

        // estado del tablero
        switch (tipoDeMovimiento) {

            case PRIMERA_RONDA:

                // si se presiono la casilla cárcel de otro jugador diferente al actual
                if (casillaPresionada != jugadorActual.getCarcel()) {

                    infoLabel.setText("Esa cárcel no es tuya, presiona la tuya.");
                    break;

                } // else: se presiono la casilla cárcel del jugador actual

                // se sacan todas las fichas de la cárcel
                liberarFichas(jugadorActual);
                infoLabel.setText("Fichas liberadas.");

                // se reinicia el número de tiradas
                rollCount = 0;

                // saque o no par, es la primera ronda, por tanto después de liberar sus fichas,
                // se pasa el turno al siguiente jugador
                siguienteJugador();

                // si después de cambiar el turno al siguiente jugador, este es el primer
                // jugador, significa que ya se terminó la primera ronda
                if (jugadorActual == primerJugador) {

                    tipoDeMovimiento = TipoDeMovimiento.MOVIENDO_FICHA;
                    infoLabel.setText(
                            infoLabel.getText() + " Se ha terminado la primera ronda, turno del primer jugador");

                }

                // se espera a que se tiren los dados;
                waitForPresionarCasilla = false;
                waitForTirarDados = true;
                break;

            case MOVIENDO_FICHA:

                ficha = casillaPresionada.getFichaOf(getJugadorActual());

                // si no hay fichas del jugador actual en la casilla presionada
                if (ficha == null) {

                    infoLabel.setText("Esa casilla no contiene fichas tuyas, presiona otra.");
                    break;

                } // else: si hay fichas, al menos una

                // se obtiene la casilla siguiente si se mueve la ficha con la suma de los dados
                casillaSiguiente = siguienteCasillaEspecial(ficha);

                // si no se puede mover ninguna ficha de la casilla actual
                if (casillaSiguiente == null) {

                    infoLabel.setText("Ninguna ficha de esa casilla se puede mover, presiona otra.");
                    break;

                } // else: si se puede mover alguna ficha

                // si la casilla presionada es la casilla cárcel del jugador actual
                if (casillaPresionada == jugadorActual.getCarcel()) {

                    // se sacan todas las fichas de la cárcel
                    liberarFichas(jugadorActual);
                    infoLabel.setText("Fichas liberadas. Vuelta a tirar los dados.");

                    // no se cambia el turno ya que para liberar de la cárcel tuvo que haber sacado
                    // par

                    // se espera a que se tiren los dados
                    waitForTirarDados = true;
                    waitForPresionarCasilla = false;
                    break;

                } // else: no se presiono la casilla cárcel

                // se mueve una ficha de la casilla presionada a su siguiente casilla
                moverFicha(ficha, casillaSiguiente);

                // si se encarceló a algún oponente
                if (jugadorActual.envioACarcel()) {

                    infoLabel.setText("Ficha movida y oponentes encarcelados.");

                } else {

                    infoLabel.setText("Ficha movida.");

                }

                // si se saca par, el jugador actual no cambia
                if (pairsInARow > 0) {

                    infoLabel.setText(infoLabel.getText() + " Vueva a tirar.");

                } else { // si no se saca par

                    // se pasa el turno al siguiente jugador
                    siguienteJugador();
                    infoLabel.setText(infoLabel.getText() + " Turno del siguiente jugador.");

                }

                // se espera a que se tiren los dados
                waitForTirarDados = true;
                waitForPresionarCasilla = false;
                break;

            case ENCARCELANDO_OPONENTE_ANTERIOR:

                if (casillaPresionada.getTipoCasilla() == TipoCasilla.CARCEL) {

                    infoLabel.setText("No puedes encarcelar fichas ya encarceladas. Presiona otra casilla.");
                    break;

                }

                // se obtiene alguna ficha de la casilla presionada que le pertenezca al jugador
                // anterior
                ficha = casillaPresionada.getFichaOf(getJugadorAnterior());

                // si no hay fichas del jugador anterior en la casilla presionada
                if (ficha == null) {

                    infoLabel.setText("No hay fichas del jugador anterior en esa casilla, presiona otra");
                    break;

                } // else: si hay al menos una ficha

                // se encarcela solo una ficha de la casilla presionada
                casillaPresionada.moverFichaA(ficha, ficha.getCarcel());

                // se cambia el estado, y el jugador actual sigue siendo el mismo
                tipoDeMovimiento = TipoDeMovimiento.MOVIENDO_FICHA;
                infoLabel.setText("Ficha oponente encarcelada, tire los dados.");

                // se espera a que se tiren los dados
                waitForTirarDados = true;
                waitForPresionarCasilla = false;
                break;

            case SACANDO_FICHA_DEL_JUEGO:

                // se obtiene una ficha en la casilla presionada del jugador actual
                ficha = casillaPresionada.getFichaOf(getJugadorActual());

                // si no hay fichas del jugador actual en la casilla presionada
                if (ficha == null) {

                    infoLabel.setText("No hay fichas tuyas en esa casilla, presiona otra.");
                    break;

                } // else: si hay

                // se saca una ficha del juego, o sea, se manda a la casilla entrada
                ficha.sacarDelJuego();

                // si al mover esa ficha a la casilla entrada, ya no quedan más fichas en juego
                if (jugadorActual.isEntradaLlena()) {

                    // se asigna el ganador y se finaliza el tablero
                    jugadorGanador = jugadorActual;
                    finalizarTablero();

                    infoLabel.setText("El ganador es: " + jugadorGanador.getNombre());
                    break;

                } // else: aún quedan fichas en juego

                // se cambia de estado después de sacar la ficha, y se reinicia el conteo de
                // pares seguidos, sin actualizar el turno del jugador
                tipoDeMovimiento = TipoDeMovimiento.MOVIENDO_FICHA;
                pairsInARow = 0;
                infoLabel.setText("Ficha sacada del juego. Puedes volver a tirar los dados.");

                // se espera a que se tiren los dados
                waitForTirarDados = true;
                waitForPresionarCasilla = false;
                break;

            default:
                break;
        }

        // se vuelve a dibujar
        draw();

    }

    /**
     * Sopla la jugada anterior si y solo si el jugador anterior no envio a ningún
     * oponente a la cárcel aún pudiendo hacerlo. Además solo se puede soplar una
     * vez por turno.
     * 
     * @param infoLabel Label el cual se actualizará para dar información después de
     *                  soplar
     */
    public void soplar() {

        assert (getEstado() == Estado.EN_JUEGO)
                : "No se puede realizar ninguna acción si no se ha iniciado o reiniciado el tablero";

        // si se está haciendo algo que no es mover una ficha
        if (tipoDeMovimiento != TipoDeMovimiento.MOVIENDO_FICHA) {

            // se cancela
            infoLabel.setText("No puedes hacer eso, nadie ha movido una ficha fuera de la casilla SALIDA.");
            return;

        } // else: si sí se está moviendo alguna ficha

        // si no hay jugador anterior al cual soplar
        if (getJugadorAnterior() == null) {

            // se cancela
            infoLabel.setText("No puedes hacer eso, nadie ha jugado antes que tú.");
            return;

        } // else: alguién más ya jugó anteriormente

        // si el jugador anterior pudo enviar a la carcel y no envió a ningún oponente,
        // y el jugador anterior no ha soplado en su turno
        if (jugadorAnterior.pudoEnviarACarcel() && !jugadorAnterior.envioACarcel()
                && !jugadorActual.soploJugadaAnterior()) {

            // se cambia el estado
            tipoDeMovimiento = TipoDeMovimiento.ENCARCELANDO_OPONENTE_ANTERIOR;

            // el jugador actual ya soplo una vez
            jugadorActual.setSoploJugadaAnterior(true);

            infoLabel.setText(
                    "Presione una casilla en la cual se encuentre una ficha del jugador anterior, para encarcelarla.");

            // se espera a que se presione una casilla
            waitForPresionarCasilla = true;
            waitForTirarDados = false;
            return;

        } // else: jugador anterior no podía enviar a cárcel, o sí envió, o el jugador
          // actual ya soplo

        infoLabel.setText("No puedes hacer eso");

    }

    public void draw() {

        double min, canvasWidth, canvasHeight;
        GraphicsContext gc;

        gc = drawingCanvas.getGraphicsContext2D();

        canvasWidth = drawingCanvas.getWidth();
        canvasHeight = drawingCanvas.getHeight();
        min = Math.min(canvasWidth, canvasHeight);

        gc.clearRect(0, 0, canvasWidth, canvasHeight);
        gc.drawImage(background, 0, 0, min, min);

        for (CasillaEspecial c : casillas) {

            c.drawFichas(gc);

        }

        for (Jugador j : jugadores) {

            if (j != null) {
                j.getCarcel().drawFichas(gc);
                j.getEntrada().drawFichas(gc);
            }

        }

        gc.save();

        gc.setGlobalAlpha(0.5);
        gc.setFill(Color.BLACK);

        double x, y, w, h;
        x = jugadorActual.getCarcel().getX();
        y = jugadorActual.getCarcel().getY();
        w = jugadorActual.getCarcel().getWidth();
        h = jugadorActual.getCarcel().getHeight();

        gc.fillOval(x, y, w, h);

        gc.restore();

    }

}