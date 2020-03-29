import callout.DrawArrow;
import drawline.MyLine;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class CircleFor2D extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane an = new AnchorPane();

        MyLine line = new MyLine();

        DrawArrow.DrawPanel arrow = new DrawArrow.DrawPanel();

        VBox vBox = new VBox(10);
        Button b1 = new Button("画圆");
        Button b2 = new Button("同心约束");
        Button b3 = new Button("约束");

        b1.setId("button");
        b2.setId("button");

        vBox.getChildren().addAll(b1, b2, b3);
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
        List<Circle> myCircles = new ArrayList<>();

        AtomicBoolean draw = new AtomicBoolean(false);
        AtomicBoolean show = new AtomicBoolean(false);

        an.setOnMousePressed(event -> {

            if (show.get()) {
                event.consume();
            }

            if (draw.get()) {
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

                        Circle c1 = new Circle();
                        myCircles.add(c1);

                        double startX = list.get(i[0] - 1);
                        double startY = list.get(i[0]);
                        double endX = event.getX();
                        double endY = event.getY();

                        c1.setCenterX(startX);
                        c1.setCenterY(startY);
                        double radius = Math.sqrt((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY));
                        c1.setRadius(radius);
                        c1.setStroke(Color.RED);
                        c1.setFill(Color.color(1,1,1,0));
                        c1.setId(String.valueOf(i[0]));
                        an.getChildren().add(c1);
                    }
                }

                i[0]++;
            }

        });
        Set<Circle> circles = new HashSet<>();

        final int[] count = {0};
        double[] xy = new double[2];
        final Paint[] color = {null};
        b2.setOnAction(event -> {

            draw.set(false);
            show.set(true);
            myCircles.forEach(new Consumer<Circle>() {
                @Override
                public void accept(Circle circle) {

                    if (show.get()) {
                        circle.setOnMouseClicked(event -> {

                            circles.add(circle);
                            System.out.println("被点了");
                        });

                    }
                }
            });
        });

        b1.setOnAction(event -> {
            draw.set(true);
            show.set(false);
            System.out.println("shucu");
        });

        b3.setOnAction(event -> {
            Iterator<Circle> iterator =  circles.iterator();
            System.out.println(iterator.hasNext());
            while (iterator.hasNext()) {
                Circle circle = iterator.next();
                if (count[0] == 0) {
                    xy[0] = circle.getCenterX();
                    xy[1] = circle.getCenterY();
                    color[0] = circle.getFill();
                } else {
                    double radius = circle.getRadius();
                    Circle circle1 = new Circle();
                    circle1.setCenterY(xy[1]);
                    circle1.setCenterX(xy[0]);
                    circle1.setFill(color[0]);
                    circle1.setRadius(radius);
                    circle1.setStroke(Color.RED);
                    an.getChildren().remove(circle);
                    an.getChildren().add(circle1);
                }
                count[0]++;
            }
        });
    }
}
