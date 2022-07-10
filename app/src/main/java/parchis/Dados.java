package parchis;

import java.util.Random;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Dados extends Transition {

    private final int SPRITE_WIDTH, SPRITE_HEIGHT, ROWS, COLUMNS;

    private long lastUpdate;

    private int valor0;

    private int valor1;

    private Random rand;

    private ImageView imageView0, imageView1;

    public Dados(ImageView imageView0, ImageView imageView1, int rows, int columns) {

        assert (imageView0.getImage() != null) : "Asigne la imagen de sprites al imageView";

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

    public void tirar() {

        play();

    }

    // debugging
    public void setValores(int valor0, int valor1) {

        this.valor0 = valor0;
        this.valor1 = valor1;

    }

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

    public int getSuma() {

        return valor0 + valor1;

    }

    public boolean esPar() {

        return valor0 == valor1;

    }

    public void reset() {

        this.valor0 = 6;
        this.valor1 = 6;
        updateSprites();

    }

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

        if (lastUpdate == 0) {

            lastUpdate = System.currentTimeMillis();
            return;

        }

        long elapsedMillis = System.currentTimeMillis() - lastUpdate;
        if (elapsedMillis > 100) {

            valor0 = rand.nextInt(5) + 1;
            valor1 = rand.nextInt(5) + 1;
            updateSprites();
            lastUpdate = System.currentTimeMillis();

        }

    }

    public void resetAnim() {

        stop();
        updateSprites();
        lastUpdate = 0;

    }

}
