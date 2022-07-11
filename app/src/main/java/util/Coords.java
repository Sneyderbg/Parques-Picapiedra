package util;

public class Coords {

    private static final double BASE_SIZE = 800;

    /**
     * Convierte un valor x relativo a un tablero de tamaño 800*800, a otro valor
     * relativo a un tablero <b>size*size</b>.
     * 
     * @param x    Valor a convertir.
     * @param size Tamaño destino a convertir x.
     * @return Valor relativo de un tablero 800x800 convertido a otro relativo a
     *         este tablero.
     */
    public static double translate(double x, double size) {
        return (x * size) / BASE_SIZE;
    }

    /**
     * Convierta la coordenada polar a una coordenada 'x' cartesiana absoluta para
     * dibujar en el tablero, luego se traduce de un tablero 800*800 a un tablero
     * <b>size*size</b>.
     * 
     * @param r     Radio.
     * @param angle Ángulo en grados.
     * @param size  Tamaño relativo.
     * @return Coordenada x en forma cartesiana absoluta.
     */
    public static double polarToAbsCartesianX(double r, double angle, double size) {

        double x, angleRad;
        angleRad = Math.toRadians(angle);

        x = r * Math.cos(angleRad);
        x = Math.abs(x + (BASE_SIZE / 2));
        x = translate(x, size);

        return x;

    }

    /**
     * Convierta la coordenada polar a una coordenada 'y' cartesiana absoluta para
     * dibujar en el tablero, luego se traduce de un tablero 800*800 a un tablero
     * <b>size*size</b>.
     * 
     * @param r     Radio.
     * @param angle Ángulo en grados.
     * @param size  Tamaño relativo.
     * @return Coordenada y en forma cartesiana absoluta.
     */
    public static double polarToAbsCartesianY(double r, double angle, double size) {

        double y, angleRad;
        angleRad = Math.toRadians(angle);

        y = r * Math.sin(angleRad);
        y = Math.abs(y + (BASE_SIZE / 2));
        y = translate(y, size);

        return y;

    }

}