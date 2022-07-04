package app;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import parchis.Casilla;
import parchis.Ficha;
import parchis.Jugador;
import parchis.Tablero;

public class WindowController implements Initializable {

    private Tablero tablero;

    @FXML
    private AnchorPane drawingPane;

    @FXML
    private AnchorPane sidePane;

    @FXML
    Button newGameBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void sizeChanged(ObservableValue<? extends Number> obs, Number oldValue, Number newValue) {

        if (tablero == null) {
            return;
        }
        updateControls();
        tablero.draw();

    }

    @FXML
    void click(MouseEvent evt) {

    }

    void clickRect(MouseEvent evt) {

    }

    /**
     * Convierte un valor x relativo a un tablero de tamaño 800x800, a un valor
     * relativo a el tamaño de este tablero.
     * 
     * @param x Valor a convertir.
     * @return Valor relativo de un tablero 800x800 convertido a otro relativo a
     *         este tablero.
     */
    double transform(double x) {
        double min = Math.min(tablero.getWidth(), tablero.getHeight());
        return (x * min) / 800;
    }

    /**
     * Convierta la coordenada polar a una coordenada x cartesiana absoluta para
     * dibujar en el tablero.
     * 
     * @param r     Radio
     * @param angle Ángulo en grados.
     * @return Coordenada x en forma cartesiana absoluta.
     */
    double polarToAbsCartesianX(double r, double angle) {

        double x, angleRad;
        angleRad = Math.toRadians(angle);

        x = r * Math.cos(angleRad);
        x = transform(Math.abs(x + 400));

        return x;

    }

    /**
     * Convierta la coordenada polar a una coordenada y cartesiana absoluta para
     * dibujar en el tablero.
     * 
     * @param r     Radio
     * @param angle Ángulo en grados.
     * @return Coordenada y en forma cartesiana absoluta.
     */
    double polarToAbsCartesianY(double r, double angle) {

        double y, angleRad;
        angleRad = Math.toRadians(angle);

        y = r * Math.sin(angleRad);
        y = transform(Math.abs(y + 400));

        return y;

    }

    private void updateControls() {

        double r1, r2, r3, rc, re, x, y, a1, a2, a3, ac, ae;
        Casilla casillaSeguro1, casillaSalida, casillaSeguro2, casillaCarcel, casillaEntrada;
        Jugador j;

        double width, height;
        width = transform(70);
        height = transform(30);

        r1 = 383;
        a1 = 90;

        r2 = 270;
        a2 = 74;

        r3 = 270;
        a3 = 46.5;

        rc = 333;
        ac = 60;

        re = 100;
        ae = 90;

        for (int i = 0; i < 6; i++) {

            casillaSeguro1 = tablero.getCasillas().get(3 * i);
            casillaSalida = tablero.getCasillas().get(3 * i + 1);
            casillaSeguro2 = tablero.getCasillas().get(3 * i + 2);

            x = polarToAbsCartesianX(r1, a1 - 60 * i);
            y = polarToAbsCartesianY(r1, a1 - 60 * i);

            casillaSeguro1.setId("seg1-player" + i);
            updateAndAddToDrawingPane(casillaSeguro1, x, y, width, height, -60 * i);

            x = polarToAbsCartesianX(r2, a2 - 60 * i);
            y = polarToAbsCartesianY(r2, a2 - 60 * i);

            casillaSalida.setId("sal-player" + i);
            updateAndAddToDrawingPane(casillaSalida, x, y, width, height, -60 * i);

            x = polarToAbsCartesianX(r3, a3 - 60 * i);
            y = polarToAbsCartesianY(r3, a3 - 60 * i);

            casillaSeguro2.setId("seg2-player" + i);
            updateAndAddToDrawingPane(casillaSeguro2, x, y, width, height, -60 * ((i + 1) % 6));

            j = tablero.getJugadores()[i];
            if (j == null) {
                continue;
            }

            casillaCarcel = j.getCarcel();
            casillaEntrada = j.getEntrada();

            x = polarToAbsCartesianX(rc, ac - 60 * i);
            y = polarToAbsCartesianY(rc, ac - 60 * i);

            casillaCarcel.setId("car-player" + i);
            updateAndAddToDrawingPane(casillaCarcel, x, y, width, width, 0);

            x = polarToAbsCartesianX(re, ae - 60 * i);
            y = polarToAbsCartesianY(re, ae - 60 * i);

            casillaEntrada.setId("ent-player" + i);
            updateAndAddToDrawingPane(casillaEntrada, x, y, width, width, 0);

        }

    }

    private void updateAndAddToDrawingPane(Casilla casilla, double x, double y, double width, double height,
            double angle) {

        casilla.updateRect(x, y, width, height, angle);

        if (drawingPane.getChildren().contains(casilla)) {
            return;
        }

        casilla.setOnMouseClicked(evt -> casillaClicked(evt));
        drawingPane.getChildren().add(casilla);

    }

    void casillaClicked(MouseEvent evt) {

        Casilla cas = (Casilla) evt.getSource();
        Ficha f = cas.getFichas().get(0);
        tablero.moverFicha(f, tablero.siguienteCasillaEspecial(f));
        tablero.draw();

        System.out.println(cas.getId() + " clicked.");

    }

    @FXML
    void newGameEvt(Event evt) {

        // si hay un tablero ya añadido se elimina
        drawingPane.getChildren().remove(tablero);

        // nuevo tablero
        tablero = new Tablero(4, 4);

        // se añaden jugadores
        tablero.addJugador(new Jugador("red", Color.RED));
        tablero.addJugador(new Jugador("green", Color.GREEN));
        tablero.addJugador(new Jugador("purple", Color.PURPLE));
        tablero.addJugador(new Jugador("orange", Color.ORANGE));

        // se construye el tablero
        tablero.construirTablero();

        // esto hace que el canvas tablero sea del mismo tamaño que su padre
        // (drawingPane)
        tablero.widthProperty().bind(drawingPane.widthProperty());
        tablero.heightProperty().bind(drawingPane.heightProperty());

        // listener para cuando el tamaño del tablero cambie, se ajusten los controles y
        // los componentes que se dibujan
        tablero.widthProperty().addListener(((obs, oldValue, newValue) -> sizeChanged(obs, oldValue, newValue)));
        tablero.heightProperty().addListener(((obs, oldValue, newValue) -> sizeChanged(obs, oldValue, newValue)));

        // se añade el tablero al AnchorPane
        drawingPane.getChildren().add(tablero);

        // se actualizan las posiciones y tamaños de los controles
        updateControls();

        // se inicia y se dibuja el tablero con sus fichas
        tablero.iniciarTablero();
        tablero.draw();

    }

    @FXML
    void resetEvt(ActionEvent evt) {

        tablero.reiniciarTablero();
        tablero.iniciarTablero();
        tablero.draw();
        
    }

    @FXML
    void exitEvt(ActionEvent evt) {

        Platform.exit();

    }

}
