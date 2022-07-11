package parchis;

import java.util.Random;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Clase que representa dos dados, y sus animaciones.
 */
public class Dados extends Transition {

    /**
     * Valores para obtener los sprites según el valor de cada dado.
     */
    private final int SPRITE_WIDTH, SPRITE_HEIGHT, ROWS, COLUMNS;

    /**
     * Última actualización de los imageView de los dados, en milisegundos.
     */
    private long lastUpdate;

    /**
     * Valor del primer dado.
     */
    private int valor0;

    /**
     * Valor del segundo dado.
     */
    private int valor1;

    /**
     * Random para randomizar el valor de los dados.
     */
    private Random rand;

    /**
     * ImageView's para actualizar según el valor de los dados.
     */
    private ImageView imageView0, imageView1;

    /**
     * Constructor. Construye los dados con los parámetros dados.
     * <p>
     * NOTA: La imagen que contiene los sprites ya debería estar asignada a los
     * ImageView's.
     * 
     * @param imageView0 ImageView para el primer dado.
     * @param imageView1 ImageView para el segundo dado.
     * @param rows       Número de filas de los sprites (estados) de los dados.
     * @param columns    Número de columnas de los sprites (estados) de los dados.
     */
    public Dados(ImageView imageView0, ImageView imageView1, int rows, int columns) {

        assert (imageView0.getImage() != null) : "Asigne la imagen de sprites a los imageView's";

        rand = new Random();
        this.imageView0 = imageView0;
        this.imageView1 = imageView1;
        this.valor0 = 6;
        this.valor1 = 6;
        this.lastUpdate = 0;

        this.ROWS = rows;
        this.COLUMNS = columns;
        this.SPRITE_WIDTH = (int) (imageView0.getImage().getWidth() / COLUMNS);
        this.SPRITE_HEIGHT = (int) (imageView0.getImage().getHeight() / ROWS);

        setCycleCount(1);
        setCycleDuration(Duration.millis(1000));
        setInterpolator(Interpolator.LINEAR);
        updateSprites();

    }

    /**
     * Tira los dados, reproduciendo una animación que dura 1 segundo, y
     * actualizando los ImageView's cada 0.1 segundos.
     */
    public void tirar() {

        play();

    }

    // for debugging purposes, see DEBUGGING in WindowController.java
    protected void setValores(int valor0, int valor1) {

        this.valor0 = valor0;
        this.valor1 = valor1;

    }

    /**
     * Retorna el valor del dado <b>i</b>.
     * 
     * @param i Dado.
     * @return Valor del dado <b>i</b>.
     */
    public int getValor(int i) {

        switch (i) {
            case 0:
                return valor0;

            case 1:
                return valor1;

            default:
                throw new IllegalArgumentException("'i' solo puede ser 0 o 1.");
        }

    }

    /**
     * Retorna la suma de los valores de los dos dados.
     * 
     * @return {@link #valor0} + {@link #valor1}.
     */
    public int getSuma() {

        return valor0 + valor1;

    }

    /**
     * Retorna {@code true} si los dos dados forman un par.
     * 
     * @return {@code true} si {@link #valor0} + {@link #valor1}.
     */
    public boolean esPar() {

        return valor0 == valor1;

    }

    /**
     * Reinicia los valores de los dos dados, y actualiza sus sprites.
     */
    public void reset() {

        this.valor0 = 6;
        this.valor1 = 6;
        updateSprites();

    }

    /**
     * Actualiza los sprites de cada dado en los ImageView's, según sus valores.
     */
    private void updateSprites() {

        int spriteIdx0, spriteIdx1, x, y;

        spriteIdx0 = getValor(0) - 1;
        spriteIdx1 = getValor(1) - 1;

        x = (spriteIdx0 % COLUMNS) * SPRITE_WIDTH;
        y = (spriteIdx0 / COLUMNS) * SPRITE_HEIGHT;
        imageView0.setViewport(new Rectangle2D(x, y, SPRITE_WIDTH, SPRITE_HEIGHT));

        x = (spriteIdx1 % COLUMNS) * SPRITE_WIDTH;
        y = (spriteIdx1 / COLUMNS) * SPRITE_HEIGHT;
        imageView1.setViewport(new Rectangle2D(x, y, SPRITE_WIDTH, SPRITE_HEIGHT));

    }

    @Override
    protected void interpolate(double frac) {

        // animación
        if (lastUpdate == 0) {

            lastUpdate = System.currentTimeMillis();
            return;

        }

        long elapsedMillis = System.currentTimeMillis() - lastUpdate;

        // cada 100 ms randomiza los dados, y actualiza sus sprites.
        if (elapsedMillis > 100) {

            valor0 = rand.nextInt(6) + 1;
            valor1 = rand.nextInt(6) + 1;
            updateSprites();
            lastUpdate = System.currentTimeMillis();

        }

    }

    /**
     * Reinicia la animación.
     */
    public void resetAnim() {

        stop();
        updateSprites();
        lastUpdate = 0;

    }

}
