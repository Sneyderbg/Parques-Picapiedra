package parchis;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Clase que representa las casillas especiales del parqués, siendo estas:
 * Cárcel, salida, seguro, entrada.
 */
public class CasillaEspecial extends Rectangle implements Iterable<Ficha> {

    /**
     * Enumerado para describir el tipo de {@link CasillaEspecial}.<br>
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
     * Identificador de casilla: Es el número que normalmente representa la casilla
     * en un parqués.
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
     * Vector que contiene las fichas que actualmente están en esta
     * {@link CasillaEspecial}
     */
    private ArrayList<Ficha> fichas;

    /**
     * Centro del Rectángulo, se usa para dibujar las fichas alrededor del centro de
     * esta casilla.
     */
    private double centerX, centerY;

    /**
     * Constructor. Crea una casilla con los parámetros dados.
     * 
     * @param IdCasilla Entero que identifica esta {@link CasillaEspecial}.
     * @param tipo      Tipo de {@link CasillaEspecial}. Veáse
     *                  {@link TipoCasilla}.
     * @param color     Color de la {@link CasillaEspecial}.
     */
    public CasillaEspecial(int IdCasilla, TipoCasilla tipo, Color color) {

        super();

        if (idCasilla < 0) {
            assert (tipo == TipoCasilla.CARCEL || tipo == TipoCasilla.ENTRADA);
        }
        this.idCasilla = IdCasilla;
        this.tipoCasilla = tipo;
        this.fichas = new ArrayList<Ficha>();
        this.color = color;

    }

    /**
     * Mueve la ficha <b>f</b> a la casilla <b>c</b>, solo si la ficha se encuentra
     * en esta casilla.
     * 
     * @param f Ficha a mover.
     * @param c Casilla a insertar la ficha.
     */
    public void moverFichaA(Ficha f, CasillaEspecial c) {

        assert (fichas.contains(f)) : "la ficha 'f' no se encuentra en esta casilla";

        fichas.remove(f);
        c.insertarFicha(f);

    }

    /**
     * Inserta una ficha en esta casilla, y cambia su campo {@link Ficha#casilla}.
     * 
     * @param ficha {@link Ficha} a insertar en esta {@link CasillaEspecial}.
     */
    public void insertarFicha(Ficha ficha) {

        ficha.setCasilla(this);
        fichas.add(ficha);

    }

    /**
     * Mueve todas las fichas de esta casilla a la {@link CasillaEspecial} <b>c</b>.
     * 
     * @param c {@link CasillaEspecial} a la cual mover las fichas.
     */
    public void moverTodasLasFichasA(CasillaEspecial c) {

        Iterator<Ficha> it = fichas.iterator();
        Ficha f;

        while (it.hasNext()) {

            f = it.next();
            it.remove();
            c.insertarFicha(f);

        }

    }

    /**
     * Elimina una ficha del vector {@link fichas} en caso de que esta esté
     * presente, de lo contrario no hace nada.
     * 
     * @param ficha {@link Ficha} a eliminar del vector {@link fichas}.
     * @return {@code true} si la ficha existía y se removió, {@code false} de lo
     *         contrario.
     */
    public boolean removerFicha(Ficha ficha) {

        if (fichas.remove(ficha)) {

            ficha.setCasilla(null);
            return true;

        }

        return false;

    }

    /**
     * Elimina todas las fichas de esta casilla.
     */
    public void removerTodasLasFichas() {

        Iterator<Ficha> it = fichas.iterator();
        Ficha f;

        while (it.hasNext()) {

            f = it.next();
            removerFicha(f);

        }

    }

    /**
     * Encarcela todas las fichas de esta casilla, a sus casillas CARCELES
     * correspondientes.
     */
    public void encarcelarTodasLasFichas() {

        Iterator<Ficha> iterator = fichas.iterator();
        Ficha f;

        while (iterator.hasNext()) {

            f = iterator.next();
            moverFichaA(f, f.getCarcel());

        }

    }

    /**
     * Encarcela a todas las fichas que no sean de el color entregado como
     * parámetro.
     * 
     * @param color Fichas con este color no se encarcelan.
     * @return {@code true} si se encarcelo una o más ficas, {@code false} si no se
     *         encarceló ninguna ficha.
     */
    public boolean encarcelarTodasExceptuando(Color color) {

        Ficha f;
        boolean seEncarcelo = false;

        Iterator<Ficha> iterator = fichas.iterator();

        while (iterator.hasNext()) {

            f = iterator.next();

            if (f.getColor() != color) {

                iterator.remove();
                f.getCarcel().insertarFicha(f);
                seEncarcelo = true;

            }

        }

        return seEncarcelo;

    }

    /**
     * Comprueba si existe al menos una ficha que no pertenezca al jugador <b>j</b>.
     * 
     * @param j Jugador a comparar.
     * @return {@code true} si existe al menos una ficha que cumpla con:
     *         {@code f.getJugadorPadre() != j}, {@code false} de lo contrario.
     */
    public boolean contieneFichasOponentes(Jugador j) {

        for (Ficha ficha : fichas) {

            if (ficha.getJugadorPadre() != j) {
                return true;
            }

        }

        return false;

    }

    /**
     * Comprueba si existe al menos una ficha con color igual al color dado como
     * parámetro.
     * 
     * @param color Color a comparar.
     * @return {@code true} si existe al menos una ficha tal que:
     *         {@code f.getColor() == color}, {@code false} de lo contrario.
     */
    public boolean contieneFichasCon(Color color) {

        for (Ficha ficha : fichas) {

            if (ficha.getColor() == color) {
                return true;
            }

        }

        return false;

    }

    /**
     * Retorna una ficha de esta casilla.
     * 
     * @param idx Índice de la ficha a retornar.
     * @return Ficha con índice <b>idx</b> del vector {@link fichas}.
     */
    public Ficha getFicha(int idx) {

        return fichas.get(idx);

    }

    /**
     * Busca y retorna la primera ficha que le pertenezca al jugador entregado como
     * parámetro.
     * 
     * @param jugador Jugador de la ficha a buscar.
     * @return Primera ficha en el array {@link #fichas} que cumpla:
     *         {@code f.getJugadorPadre() == jugador}, si no se encuentra ninguna,
     *         se retornará {@code null}.
     */
    public Ficha getFichaOf(Jugador jugador) {

        for (Ficha f : fichas) {

            if (f.getJugadorPadre() == jugador) {

                return f;

            }

        }

        return null;

    }

    /**
     * Retorna el número de esta {@link CasillaEspecial}.
     * 
     * @return {@link #idCasilla}.
     */
    public int getIdCasilla() {
        return idCasilla;
    }

    /**
     * Retorna el tipo de casilla de esta {@link CasillaEspecial}.
     * 
     * @return {@link #tipoCasilla}.
     */
    public TipoCasilla getTipoCasilla() {
        return tipoCasilla;
    }

    /**
     * Retorna el color de esta Casilla.
     * 
     * @return {@link #color}.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Retorna el número de fichas que contiene esta casilla.
     * 
     * @return {@code fichas.size()}.
     */
    public int length() {
        return fichas.size();
    }

    /**
     * Actualiza el rectangulo contenedor de esta Casilla el cual se usa para
     * dibujar las fichas contenidas en esta casilla,
     * 
     * @param x      Posición central x.
     * @param y      Posición central y.
     * @param width  Ancho del rectángulo.
     * @param height Altura del rectángulo.
     * @param angle  Ángulo a rotar.
     */
    public void updateRect(double x, double y, double width, double height, double angle) {
        this.centerX = x;
        this.centerY = y;
        setX(x - width / 2);
        setY(y - height / 2);
        setWidth(width);
        setHeight(height);
        setRotate(angle);
        setFill(Color.TRANSPARENT);
    }

    /**
     * Dibuja las fichas contenidas en esta casilla usando el contexto gráfico
     * entregado como parámetro.
     * 
     * @param gc {@link GraphicsContext} usado para dibujar.
     */
    public void drawFichas(GraphicsContext gc) {

        // si no hay fichas en esta casilla, se omite el dibujo
        if (fichas.size() == 0) {
            return;
        }

        // si la casilla es tipo carcel o tipo entrada
        if (getTipoCasilla() == TipoCasilla.CARCEL || getTipoCasilla() == TipoCasilla.ENTRADA) {

            drawFichasAsGroup(gc, getWidth() / 4);

        } else { // si es seguro o salida

            drawFichasAsLine(gc, getHeight() / 3);

        }

    }

    /**
     * Dibuja las fichas de esta casilla en forma lineal dentro del rectángulo que
     * describe esta casilla.
     * 
     * @param gc     {@link GraphicsContext} usado para dibujar.
     * @param radius Radio de las fichas a dibujar.
     */
    private void drawFichasAsLine(GraphicsContext gc, double radius) {

        int n = fichas.size();
        double posXEnCasilla[] = new double[n];

        // pos horizontal respecto al rectángulo de la casilla
        for (int i = 0; i < n; i++) {

            posXEnCasilla[i] = ((i + 1) * getWidth()) / (n + 1);

        }

        gc.save();

        gc.translate(centerX, centerY);
        gc.rotate(getRotate());
        gc.translate(-centerX, -centerY);

        // se dibujan las fichas en forma lineal
        for (int i = 0; i < fichas.size(); i++) {

            fichas.get(i).draw(gc, getX() + posXEnCasilla[i], centerY, radius);

        }

        gc.restore();

    }

    /**
     * Dibuja las fichas de esta casilla en forma grupal, esto va para las casillas
     * de tipo CARCEL o ENTRADA.
     * 
     * @param gc       {@link GraphicsContext} usado para dibujar.
     * @param diameter Radio de las fichas a dibujar.
     */
    private void drawFichasAsGroup(GraphicsContext gc, double diameter) {

        double posXEnCasilla, posYEnCasilla;

        int n = length();

        // casos si hay 4 o 3 fichas
        switch (n) {
            case 4:

                // se dibuja en forma cuadrada
                for (int i = 0; i < 2; i++) {

                    for (int j = 0; j < 2; j++) {

                        posXEnCasilla = (1 + 4 * j) * getWidth() / 8;
                        posYEnCasilla = (1 + 4 * i) * getHeight() / 8;

                        fichas.get(2 * i + j).draw(gc, getX() + posXEnCasilla, getY() + posYEnCasilla, diameter);

                    }

                }
                break;

            case 3:

                // se dibuja en forma triángular
                for (int i = 0; i < 2; i++) {

                    for (int j = 0; j < i + 1; j++) {

                        posXEnCasilla = (j) * getWidth() / 2;
                        if (i == 0) {

                            posXEnCasilla += getWidth() / 4;

                        }
                        posYEnCasilla = (i) * getHeight() / 2;

                        fichas.get(i + j).draw(gc, getX() + posXEnCasilla, getY() + posYEnCasilla, diameter);

                    }

                }
                break;

            default: // caso para dos o una ficha
                drawFichasAsLine(gc, diameter);
                break;
        }

    }

    @Override
    public String toString() {

        return String.format("Casilla %s, %s", getColor().toString(), getId());

    }

    @Override
    public Iterator<Ficha> iterator() {

        return fichas.iterator();

    }

}