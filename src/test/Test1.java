package test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import sun.security.jgss.wrapper.GSSCredElement;

import java.awt.*;

public class Test1 extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Path path = new Path();
//        gc.moveTo(10, 10);
//        gc.lineTo(20, 20);
//        gc.setStroke(Color.RED);
//        gc.stroke();

        final Canvas canvas = new Canvas(250,250);
        AnchorPane anchorPane = new AnchorPane();

        anchorPane.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);

        gc.beginPath();
        gc.moveTo(50, 50);
        gc.lineTo(80, 100);
        gc.lineTo(80, 50);
        gc.closePath();
        gc.fill();

        gc.moveTo(100, 100);
        gc.lineTo(200, 200);
        gc.lineTo(200, 100);
        gc.closePath();
        gc.stroke();

        primaryStage.setScene(new Scene(anchorPane, 800, 600));
        primaryStage.show();

        anchorPane.setOnMousePressed(event -> {
            gc.clearRect(0,0, 800, 600);
        });
    }
}
