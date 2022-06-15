package parchis;

import java.util.Random;

public class Dado {

    private int valor = 6;

    private Random rand;

    public Dado() {

    }

    public void tirar() {

        this.valor = rand.nextInt(5) + 1;

    }

    public int getValor() {

        return valor;

    }

    public void reset() {
        this.valor = 6;
    }

}
