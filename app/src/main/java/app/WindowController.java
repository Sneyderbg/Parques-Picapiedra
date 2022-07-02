package app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class WindowController implements Initializable{
    
    private ParchisDrawingCanvas parchisDrawingCanvas;
    
    @FXML
    private AnchorPane drawingPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        parchisDrawingCanvas = new ParchisDrawingCanvas();
        System.out.println(drawingPane.getWidth() + ", " + drawingPane.getHeight());
        parchisDrawingCanvas.widthProperty().bind(drawingPane.widthProperty().subtract(2));
        parchisDrawingCanvas.heightProperty().bind(drawingPane.heightProperty().subtract(2));
        drawingPane.getChildren().add(parchisDrawingCanvas);
        
        parchisDrawingCanvas.nuevoTablero();
        
    }

    @FXML
    void click(MouseEvent evt){
        

        
    }
    
}
