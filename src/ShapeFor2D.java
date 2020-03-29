import callout.DrawArrow;
import drawline.MyLine;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShapeFor2D extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane an = new AnchorPane();

        MyLine line = new MyLine();

        DrawArrow.DrawPanel arrow = new DrawArrow.DrawPanel();

        VBox vBox = new VBox(10);
        Button b1 = new Button("加标注");
        Button b2 = new Button("绘制");
        b1.setId("button");
        b2.setId("button");
        vBox.getChildren().add(b1);
        vBox.getChildren().add(b2);
        arrow.getChildren().add(vBox);
        AnchorPane.setTopAnchor(vBox, 20.0);
        AnchorPane.setLeftAnchor(vBox, 40.0);
        an.getChildren().add(arrow);
        Scene scene = new Scene(an, 800, 600);

        arrow.setHeighth(scene.getHeight());
        arrow.setWidth(scene.getWidth());

        primaryStage.setScene(scene);
        primaryStage.show();

        final int[] i = {0};
        List<Double> list = new ArrayList<>();
        List<Double> list1 = new ArrayList<>();
        List<Line> mylines = new ArrayList<>();

        AtomicBoolean drawLine = new AtomicBoolean(false);
        AtomicBoolean drawCallOut = new AtomicBoolean(false);

        scene.setOnMousePressed(event -> {

            if (i[0] % 2 == 0) {
                System.out.println(i[0]);
                list.add(event.getX());
                list.add(event.getY());
                Circle circle = line.createPoint(event.getX(), event.getY(), null);
                an.getChildren().add(circle);
            } else {
                if (list.size() > 0) {

                    list1.add(event.getX());
                    list1.add(event.getY());

                    if (drawCallOut.get()) {
                        arrow.setStart(list.get(i[0] - 1), list.get(i[0]));
                        arrow.setEnd(list1.get(i[0] - 1), list1.get(i[0]));

                        if (mylines.size() > 0) {
                            arrow.change(mylines);
                        }
                        arrow.draw();
                    }
//                    arrow.change();
                    if (drawLine.get()) {
                        Circle circle = line.createPoint(event.getX(), event.getY(), Color.BLUE);
                        Line line1 = line.drawLine(list.get(i[0] - 1), list.get(i[0]), event.getX(), event.getY());
                        mylines.add(line1);
                        an.getChildren().add(line1);
                        an.getChildren().add(circle);
                    }
                }
            }

            i[0]++;
        });

        b2.setOnAction(event -> {

            drawCallOut.set(false);
            drawLine.set(true);

        });

        b1.setOnAction(event -> {
            drawCallOut.set(true);
            drawLine.set(false);
            System.out.println("shucu");
        });
    }
}
