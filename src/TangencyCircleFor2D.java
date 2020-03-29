import callout.DrawArrow;
import drawline.MyLine;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class TangencyCircleFor2D extends Application {

    //共綫
    public boolean collinear(double x1, double y1, double x2,
                             double y2, double x3, double y3) {

    /* Calculation the area of
    triangle. We have skipped
    multiplication with 0.5
    to avoid floating point
    computations */
        double a = x1 * (y2 - y3) +
                x2 * (y3 - y1) +
                x3 * (y1 - y2);

        if (a == 0)
            return true;
        else
            return false;
    }

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
        Button b2 = new Button("选择对象");
        Button b3 = new Button("相切约束");
        Button b4 = new Button("选择切点");


        b1.setId("button");
        b2.setId("button");

        vBox.getChildren().addAll(b1, b2, b3, b4);
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
        AtomicBoolean tangency = new AtomicBoolean(false);
        final double[] tan = new double[2];

        an.setOnMousePressed(event -> {

            if (tangency.get()) {
                double x = event.getX();
                double y = event.getY();
                tan[0] = x;
                tan[1] = y;
                Circle circle = new Circle();
                circle.setFill(Color.BLUE);
                circle.setRadius(2);
                circle.setCenterY(y);
                circle.setCenterX(x);
                an.getChildren().add(circle);

            }

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
                        c1.setFill(Color.color(1, 1, 1, 0));
                        c1.setId(String.valueOf(i[0]));
                        an.getChildren().add(c1);
                    }
                }

                i[0]++;
            }

        });

        b4.setOnAction(event -> {
            draw.set(false);
            show.set(false);
            tangency.set(true);
        });
        List<Circle> circles = new ArrayList<>();

        final int[] count = {0};
        final double[] cir = new double[3];
        final Paint[] color = {null};
        //选择对象
        b2.setOnAction(event -> {
            tangency.set(false);
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

        //画圆
        b1.setOnAction(event -> {
            draw.set(true);
            show.set(false);
            tangency.set(false);
//            System.out.println("shucu");
        });

        //相切约束
        b3.setOnAction(event -> {

            for (int j = 0; j < circles.size(); j++) {

                Circle circle = circles.get(j);
                if (j == 0) {
                    cir[0] = circle.getCenterX();
                    cir[1] = circle.getCenterY();
                    cir[2] = circle.getRadius();
                    color[0] = circle.getFill();
                } else {

                    double radius = circle.getRadius();
                    Circle circle1 = new Circle();
                    double x = 0;
                    double y = 0;
//                    if (cir[2] > radius) {
//                    x = (tan[0] - (radius * cir[0] / (cir[2] + radius))) / (1 - radius / (radius + cir[2]));
//                    y = Math.sqrt((radius + cir[2]) * (radius + cir[2]) - (x - cir[0]) * (x - cir[0])) + cir[1];
//                    } else {
                    //内切圆
                        x = tan[0] - radius * (tan[0] - cir[0]) / cir[2];
                        y = -Math.sqrt((radius - cir[2]) * (radius - cir[2]) - (x - cir[0]) * (x - cir[0])) + cir[1];
//                    }
                    circle1.setCenterY(x);
                    circle1.setCenterX(y);
                    circle1.setFill(color[0]);
                    circle1.setRadius(radius);
                    circle1.setStroke(Color.RED);
                    an.getChildren().remove(circle);
                    an.getChildren().add(circle1);
                }
            }
        });
    }
}
