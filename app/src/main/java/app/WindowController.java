package app;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import parchis.CasillaEspecial;
import parchis.Jugador;
import parchis.Tablero;
import parchis.Tablero.Estado;
import util.Coords;

public class WindowController implements Initializable {

    /**
     * Flag for debugging.
     */
    private final boolean DEBUGGING = false;

    /**
     * Tablero
     */
    private Tablero tablero;

    /**
     * Número de jugadores elegidos para jugar.
     */
    private int choosenPlayers;

    /**
     * Imagen con los sprites de los dados.
     */
    private Image dadosSpritesImage;

    @FXML
    private AnchorPane drawingPane;

    @FXML
    private AnchorPane sidePane;

    @FXML
    private VBox chooseVBox;

    @FXML
    private VBox chooseNumbersVBox;

    @FXML
    private GridPane choosePlayersPane;

    @FXML
    private ComboBox<Integer> nPawnsComboBox;

    @FXML
    private ImageView dado0ImageView;

    @FXML
    private ImageView dado1ImageView;

    @FXML
    private Label nextPlayerLabel;

    @FXML
    private Label infoLabel;

    @FXML
    private TextField customDadosTextField;

    @FXML
    private Canvas drawingCanvas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // se añaden las opciones para elegir el número de fichas por jugador
        nPawnsComboBox.getItems().addAll(2, 3, 4);

        dadosSpritesImage = new Image(getClass().getResource("dados.png").toExternalForm(), 150, 100, false, false);
        dado0ImageView.setImage(dadosSpritesImage);
        dado1ImageView.setImage(dadosSpritesImage);

        if (DEBUGGING) {

            customDadosTextField.setVisible(true);

        }

    }

    /**
     * Actualiza los controles (posición de las casillas en pantalla) del tablero
     * según se cambie el tamaño de la ventana, y se dibujan nuevamente las fichas
     * del tablero.
     * 
     * @param obs      ObservableValue.
     * @param oldValue Old value.
     * @param newValue New value.
     */
    private void sizeChanged(ObservableValue<? extends Number> obs, Number oldValue, Number newValue) {

        if (tablero == null) {
            return;
        }
        updateControls();
        tablero.draw();

    }

    /**
     * Actualiza los controles de cada casilla especial del tablero, sean estas:
     * <b>CARCEL, SALIDA, SEGURO, ENTRADA</b>, utilizando coordenadas polares para
     * cada centro de cada casilla.
     * <p>
     * Esto es necesario para dibujar correctamente las fichas que contiene cada
     * casilla.
     */
    private void updateControls() {

        double r1, r2, r3, rc, re, x, y, a1, a2, a3, ac, ae;
        CasillaEspecial casillaSeguro1, casillaSalida, casillaSeguro2, casillaCarcel, casillaEntrada;
        Jugador j;

        double width, height, minSize;
        minSize = Math.min(drawingCanvas.getWidth(), drawingCanvas.getHeight());
        width = Coords.translate(70, minSize);
        height = Coords.translate(30, minSize);

        // ---- coordenadas polares para las casillas iniciales del color verde ----//
        // ----------------- Centro (r, a) de cada casilla -------------------------//

        // Casilla SEGURO1 (la que está antes de la SALIDA del respectivo color)
        r1 = 383;
        a1 = 90;

        // Casilla SALIDA
        r2 = 270;
        a2 = 74;

        // Casilla SEGURO2 (la que está despues de SALIDA)
        r3 = 270;
        a3 = 46.5;

        // Casilla CARCEL
        rc = 333;
        ac = 60;

        // Casilla ENTRADA
        re = 100;
        ae = 90;

        // para cada color del tablero
        for (int i = 0; i < 6; i++) {

            // se obtienen las casillas ya creadas del tablero
            casillaSeguro1 = tablero.getCasillas().get(3 * i);
            casillaSalida = tablero.getCasillas().get(3 * i + 1);
            casillaSeguro2 = tablero.getCasillas().get(3 * i + 2);

            // polares a cartesianas
            x = Coords.polarToAbsCartesianX(r1, a1 - 60 * i, minSize);
            y = Coords.polarToAbsCartesianY(r1, a1 - 60 * i, minSize);

            // se actualiza la posición y rotación de la casilla
            updateAndAddToDrawingPane(casillaSeguro1, x, y, width, height, -60 * i);

            x = Coords.polarToAbsCartesianX(r2, a2 - 60 * i, minSize);
            y = Coords.polarToAbsCartesianY(r2, a2 - 60 * i, minSize);

            updateAndAddToDrawingPane(casillaSalida, x, y, width, height, -60 * i);

            x = Coords.polarToAbsCartesianX(r3, a3 - 60 * i, minSize);
            y = Coords.polarToAbsCartesianY(r3, a3 - 60 * i, minSize);

            updateAndAddToDrawingPane(casillaSeguro2, x, y, width, height, -60 * ((i + 1) % 6));

            j = tablero.getJugadores()[i];
            // si no existe un jugador en la posición i del tablero, se omite lo siguiente
            if (j == null) {
                continue;
            }

            // si sí exite, se obtienen su carcel y entrada, posteriormente se actualizan
            // sus posiciones y rotaciones
            casillaCarcel = j.getCarcel();
            casillaEntrada = j.getEntrada();

            x = Coords.polarToAbsCartesianX(rc, ac - 60 * i, minSize);
            y = Coords.polarToAbsCartesianY(rc, ac - 60 * i, minSize);

            updateAndAddToDrawingPane(casillaCarcel, x, y, width, width, 0);

            x = Coords.polarToAbsCartesianX(re, ae - 60 * i, minSize);
            y = Coords.polarToAbsCartesianY(re, ae - 60 * i, minSize);

            updateAndAddToDrawingPane(casillaEntrada, x, y, width, width, 0);

        }

    }

    /**
     * Actualiza la posición y rotación de una casilla especial con los parámetros
     * dados.
     * 
     * @param casilla {@link CasillaEspecial} a actualizar.
     * @param x       Posición central x.
     * @param y       Posición central y.
     * @param width   Ancho
     * @param height  Alto
     * @param angle   Rotación (sentido horario).
     */
    private void updateAndAddToDrawingPane(CasillaEspecial casilla, double x, double y, double width, double height,
            double angle) {

        casilla.updateRect(x, y, width, height, angle);

        if (drawingPane.getChildren().contains(casilla)) {
            return;
        }

        casilla.setOnMouseClicked(evt -> casillaClicked(evt));
        drawingPane.getChildren().add(casilla);

    }

    /**
     * Maneja los clicks en los controles de todas las casillas, es decir si se
     * presiona una casilla.
     * 
     * @param evt event.
     */
    void casillaClicked(MouseEvent evt) {

        CasillaEspecial casilla = (CasillaEspecial) evt.getSource();

        tablero.presionarCasilla(casilla);

        tablero.draw();

    }

    /**
     * Se llama al presionar el botón 'tirar'. Tira los dados.
     * 
     * @param evt event.
     */
    @FXML
    void rollTheDice(ActionEvent evt) {

        if (tablero == null || tablero.getEstado() == Estado.NO_INICIADO) {
            return;
        }

        if (DEBUGGING) {

            if (customDadosTextField.getText() == null || customDadosTextField.getText().length() == 0) {

                tablero.tirarDados(null);

            } else {

                tablero.tirarDados(customDadosTextField.getText());

            }

        } else {

            tablero.tirarDados(null);

        }

    }

    /**
     * Se llama al presionar el botón 'soplar'. Se intenta soplar al jugador
     * anterior en el tablero, si se puede.
     * 
     * @param evt event.
     */
    @FXML
    void soplarEvt(ActionEvent evt) {

        if (tablero == null || tablero.getEstado() == Estado.NO_INICIADO) {

            return;

        }

        tablero.soplar();

    }

    /**
     * Se llama al presionar el botón 'nuevo juego'. Muestra la vista para
     * configurar los jugadores e iniciar un nuevo juego.
     * 
     * @param evt event.
     */
    @FXML
    void newGameEvt(ActionEvent evt) {

        // se oculta el canvas de dibujo
        drawingCanvas.setVisible(false);

        // muestra la vista para elegir y configurar los jugadores
        chooseVBox.setVisible(true);

        choosenPlayers = 0;

        clearEvt(evt);

        infoLabel.setText("Aquí se mostrará información sobre el juego.");
        nextPlayerLabel.setText("Aquí se mostrará el turno");

        if (DEBUGGING) {

            nPawnsComboBox.setValue(4);

            HBox hb;
            CheckBox c;
            TextField t;

            int idx = 0;

            for (Node node : choosePlayersPane.getChildren()) {

                if (idx == 0 || idx == 3 || idx == 5) {

                    hb = (HBox) node;
                    c = (CheckBox) hb.getChildren().get(0);
                    t = ((TextField) hb.getChildren().get(1));

                    c.setSelected(true);
                    t.setText("player-" + idx);

                    choosenPlayers++;

                }

                idx++;

            }
        }

    }

    /**
     * Se llama al presionar el botón 'reiniciar'. Reinicia el tablero.
     * 
     * @param evt
     */
    @FXML
    void resetEvt(ActionEvent evt) {

        tablero.reiniciarTablero();
        tablero.iniciarTablero();
        tablero.draw();

    }

    /**
     * Se llama al presionar el botón 'salir'. Finaliza la ejecución.
     * 
     * @param evt
     */
    @FXML
    void exitEvt(ActionEvent evt) {

        Platform.exit();

    }

    /**
     * Se llama al presionar el botón 'limpiar' en la vista de configuración de
     * jugadores. Limpia las entradas de texto y reinicia los jugadores elegidos.
     * 
     * @param evt event.
     */
    @FXML
    void clearEvt(ActionEvent evt) {

        HBox playerHBox;
        CheckBox playerCheckBox;
        TextField playerTextField;

        for (Node node : choosePlayersPane.getChildren()) {

            playerHBox = (HBox) node;
            playerCheckBox = (CheckBox) playerHBox.getChildren().get(0);
            playerTextField = (TextField) playerHBox.getChildren().get(1);

            playerCheckBox.setSelected(false);
            playerTextField.setText("");
            playerTextField.setDisable(true);

        }

        choosenPlayers = 0;

    }

    /**
     * Se llama al presionar el botón 'iniciar' en la vista de configuración de
     * jugadores. Inicia un nuevo juego creando un tablero con los datos dados.
     * 
     * @param evt
     */
    @FXML
    void startEvt(ActionEvent evt) {

        if (validateInputs() == false) {
            return;
        }

        int numFichasPorJugador, idx;
        Jugador j;
        numFichasPorJugador = nPawnsComboBox.getValue();

        HBox playerHBox;
        CheckBox playerCheckBox;
        TextField playerTextField;

        // nuevo tablero
        tablero = new Tablero(choosenPlayers, numFichasPorJugador, drawingCanvas, dado0ImageView,
                dado1ImageView, infoLabel, nextPlayerLabel);

        // se añaden los jugadores seleccionados
        idx = 0;
        for (Node node : choosePlayersPane.getChildren()) {

            playerHBox = (HBox) node;
            playerCheckBox = (CheckBox) playerHBox.getChildren().get(0);
            playerTextField = (TextField) playerHBox.getChildren().get(1);

            // si está seleccionado
            if (playerCheckBox.isSelected()) {

                // se crean los jugadores con los datos de cada HBox, y sus respectivos colores
                // según el indíce idx
                j = new Jugador(playerTextField.getText(), tablero.getColors().get(idx));
                tablero.addJugador(j);

            }

            idx++;

        }

        // esto hace que el canvas de dibujo sea del mismo tamaño que su padre
        // (drawingPane)
        drawingCanvas.widthProperty().bind(drawingPane.widthProperty());
        drawingCanvas.heightProperty().bind(drawingPane.heightProperty());

        // listener para cuando el tamaño del canvas cambie, se ajusten los controles y
        // los componentes que se dibujan
        drawingCanvas.widthProperty().addListener(((obs, oldValue, newValue) -> sizeChanged(obs, oldValue, newValue)));
        drawingCanvas.heightProperty().addListener(((obs, oldValue, newValue) -> sizeChanged(obs, oldValue, newValue)));

        // se oculta la vista para elegir y configurar los jugadores
        chooseVBox.setVisible(false);

        // se añade el tablero al AnchorPane
        drawingCanvas.setVisible(true);

        // se actualizan las posiciones y tamaños de los controles(casillas) del
        // tablero.
        updateControls();

        // se inicia y se dibuja el tablero con sus fichas
        tablero.iniciarTablero();
        tablero.draw();

    }

    /**
     * Valida las entradas en la vista de configuración de jugadores.
     * <p>
     * Se válida que:
     * <ul>
     * <li>Se haya elegido un número de fichas por jugador.</li>
     * <li>Se hayan elegido y dado un nombre a al menos 2 jugadores.</li>
     * </ul>
     * 
     * @return {@code true} si se cumple lo anterior, {@code false} de lo contrario.
     */
    private boolean validateInputs() {

        Alert warningAlert = new Alert(AlertType.WARNING);
        warningAlert.setTitle("Warning");

        // si no se ha elegido número de fichas por jugador
        if (nPawnsComboBox.getValue() == null) {

            warningAlert.setHeaderText("Invalid number of pawns per player");
            warningAlert.setContentText("You must select a number of pawns per player to start the game");
            warningAlert.showAndWait();
            return false;

        }

        // si hay menos de 2 jugadores elegidos
        if (choosenPlayers < 2) {

            warningAlert.setHeaderText("Invalid number of players");
            warningAlert.setContentText("You must select at least 2 players to start the game");
            warningAlert.showAndWait();
            return false;

        }

        HBox playerHBox;
        CheckBox playerCheckBox;
        TextField playerTextField;

        // para cada checkbox y textfield
        for (Node node : choosePlayersPane.getChildren()) {

            playerHBox = (HBox) node;
            playerCheckBox = (CheckBox) playerHBox.getChildren().get(0);
            playerTextField = (TextField) playerHBox.getChildren().get(1);

            // si hay un jugador elegido, y este tiene un nombre no válido.
            if (playerCheckBox.isSelected()
                    && (playerTextField.getText() == null || playerTextField.getText().trim().length() == 0)) {

                warningAlert.setHeaderText("Nombre de jugadores no válidos.");
                warningAlert.setContentText("Los jugadores deben tener nombres válidos.");
                warningAlert.showAndWait();
                return false;

            }

        }

        return true;

    }

    /**
     * Se llama al presionar un CheckBox de la vista de configuración de jugadores.
     * Si el checkbox presionado se activó, se suma 1 a los jugadores elegidos, si
     * se desactivó, se resta 1.
     * 
     * @param evt event.
     */
    @FXML
    void checkBoxClicked(ActionEvent evt) {

        CheckBox playerCheckBox = (CheckBox) evt.getSource();
        HBox playerHBox = (HBox) playerCheckBox.getParent();
        TextField playerTextField = (TextField) playerHBox.getChildren().get(1);

        if (playerCheckBox.isSelected()) {

            playerTextField.setDisable(false);
            choosenPlayers++;

        } else {

            playerTextField.setDisable(true);
            choosenPlayers--;

        }

    }

}
