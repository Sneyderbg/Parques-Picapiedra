package parchis;

import java.util.Random;

public class Dado {

    private int valor;

    private Random rand;

    public Dado() {

        rand = new Random();
        this.valor = 6;
        
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
