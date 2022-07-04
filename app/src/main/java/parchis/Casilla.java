package parchis;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Clase que representa las casillas especiales del parqués, siendo estas:
 * Cárcel, salida, seguro, entrada.
 */
public class Casilla extends Rectangle {

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
     * Vector que contiene las fichas que actualmente están en esta {@link Casilla}
     */
    private ArrayList<Ficha> fichas;

    private double centerX, centerY;

    /**
     * Constructor. Crea una casilla con los parámetros dados.
     * 
     * @param jugadorPadre Jugador al que pertenece esta {@link Casilla}.
     * @param IdCasilla    Entero que identifica esta {@link Casilla}.
     * @param tipo         Tipo de {@link Casilla}. Veáse {@link TipoCasilla}.
     */
    public Casilla(int IdCasilla, TipoCasilla tipo, Color color) {

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
     * Inserta una ficha en el vector {@link fichas}.
     * 
     * @param ficha {@link Ficha} a insertar en esta {@link Casilla}.
     */
    public void insertarFicha(Ficha ficha) {

        ficha.setCasilla(this);
        fichas.add(ficha);

    }

    public void insertarTodasLasFichas(ArrayList<Ficha> fichasAInsertar) {

        for (Ficha ficha : fichasAInsertar) {

            ficha.setCasilla(this);
            fichas.add(ficha);
            
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

        return fichas.remove(ficha);

    }

    public void removerTodasLasFichas(){

        fichas.clear();
        
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
     * Retorna el color de esta Casilla.
     * 
     * @return {@link #color}.
     */
    public Color getColor() {
        return color;
    }

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

    public void drawFichas(GraphicsContext gc) {

        if (fichas.size() == 0) {
            return;
        }

        if (getTipoCasilla() == TipoCasilla.CARCEL || getTipoCasilla() == TipoCasilla.ENTRADA) {

            drawFichasAsGroup(gc, getWidth() / 6);

        } else {

            drawFichasAsLine(gc, getHeight() / 3);

        }

    }

    private void drawFichasAsLine(GraphicsContext gc, double radius) {

        int n = fichas.size();
        double posXEnCasilla[] = new double[n];

        for (int i = 0; i < n; i++) {

            posXEnCasilla[i] = ((i + 1) * getWidth()) / (n + 1);

        }

        gc.save();

        gc.translate(centerX, centerY);
        gc.rotate(getRotate());
        gc.translate(-centerX, -centerY);

        for (int i = 0; i < fichas.size(); i++) {

            fichas.get(i).draw(gc, getX() + posXEnCasilla[i], centerY, radius);

        }

        gc.restore();

    }

    private void drawFichasAsGroup(GraphicsContext gc, double radius) {

        int n = fichas.size();
        double posXEnCasilla, posYEnCasilla;

        switch (n) {
            case 4:

                for (int i = 0; i < 2; i++) {

                    posXEnCasilla = (i + 1) * getWidth() / 3;
                    for (int j = 0; j < 2; j++) {

                        posYEnCasilla = (j + 1) * getHeight() / 3;

                        fichas.get(2 * i + j).draw(gc, getX() + posXEnCasilla, getY() + posYEnCasilla, radius);

                    }

                }
                break;

            case 3:

                for (int i = 0; i < 2; i++) {

                    posXEnCasilla = (i + 1) * getWidth() / (i + 2);
                    for (int j = 0; j < i + 1; j++) {

                        posYEnCasilla = (j + 1) * getHeight() / 3;

                        fichas.get(i + j).draw(gc, getX() + posXEnCasilla, getY() + posYEnCasilla, radius);

                    }

                }
                break;

            default:
                drawFichasAsLine(gc, radius);
                break;
        }

    }

}