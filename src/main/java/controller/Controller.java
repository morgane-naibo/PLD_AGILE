package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Controller {

    @FXML
    private Button myButton;

    @FXML
    private Line myLine;

    @FXML
    private Pane pane;

    @FXML
    public void handleButtonClick() {
        myButton.setText("You clicked the button!");
    }

    @FXML
    public void handleLineClick(MouseEvent event) {
        double x=event.getX();
        double y=event.getY();

        // Create a new Circle at the clicked position
        Circle newPoint = new Circle(x, y, 5, Color.RED);
            
        // Add the new Circle to the Pane
        pane.getChildren().add(newPoint);

        System.out.println("Point added at (" + x + ", " + y + ")");
        
        System.out.println("Line clicked!");
    }

    @FXML
    public void handleCircleClick() {
        System.out.println("Cirlce clicked!");
    }
}