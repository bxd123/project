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
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class CircleLineFor2D extends Application {

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
        Button b2 = new Button("相切约束");
        Button b3 = new Button("约束");
        Button b4 = new Button("画线");

        vBox.getChildren().addAll(b1, b4, b2, b3);
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
        final int[] linIndex = {0};

        List<Double> list = new ArrayList<>();
        List<Double> list1 = new ArrayList<>();
        //长度
        Map<Line, Double> lengthMapping = new HashMap<>();

        List<Circle> myCircles = new ArrayList<>();
        List<Line> lines = new ArrayList<>();

        AtomicBoolean draw = new AtomicBoolean(false);
        AtomicBoolean show = new AtomicBoolean(false);
        AtomicBoolean drawLine = new AtomicBoolean(false);

        //切点
        final double[] cp = new double[2];

        an.setOnMousePressed(event -> {

            //画线
            if (drawLine.get()){

                if (linIndex[0] % 2 == 0) {
                    list1.add(event.getX());
                    list1.add(event.getY());
                } else {
                    double startX = list1.get(linIndex[0] - 1);
                    double startY = list1.get(linIndex[0]);
                    double endX = event.getX();
                    double endY = event.getY();
//                    double slpoe = (endY - startY) / (endX - startX);
                    double length =  Math.sqrt((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY));
                    Line line1 = new Line(startX, startY, endX, endY);
                    lines.add(line1);
                    lengthMapping.put(line1, length);
                    an.getChildren().add(line1);
                }
                linIndex[0]++;
            }

            //画圆
            if (draw.get()) {
                if (i[0] % 2 == 0) {

                    System.out.println(i[0]);
                    list.add(event.getX());
                    list.add(event.getY());
                    Circle circle = line.createPoint(event.getX(), event.getY(), null);
                    an.getChildren().add(circle);
                } else {
                    if (list.size() > 0) {

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

            if (show.get()) {
                double x = event.getX();
                double y = event.getY();
                Circle circle = line.createPoint(x, y, null);
                cp[0] = x;
                cp[1] = y;
                an.getChildren().add(circle);
            }
        });

        //画线
        b4.setOnAction(event -> {
            draw.set(false);
            show.set(false);
            drawLine.set(true);

        });

        Set<Circle> circles = new HashSet<>();
        final int[] count = {0};
        double[] xy = new double[2];
        final Paint[] color = {null};
        //相切约束
        b2.setOnAction(event -> {

            draw.set(false);
            drawLine.set(false);
            show.set(true);
            if (cp[0] == 0.0) {
                return;
            }

            double cpX = myCircles.get(0).getCenterX();
            double cpY = myCircles.get(0).getCenterY();

            double k1 = (cp[1] - cpY) / (cp[0] - cpX);
            double r = Math.sqrt((cp[1] - cpY) * (cp[1] - cpY) + (cp[0] - cpX) * (cp[0] - cpX));

            double k2 = k1 == 0 ? 1 : -1 / k1;
            System.out.println("k1: " + k1);
            System.out.println("k2 : " + k2);
            System.out.println(k1 * k2);
            for (int j = 0; j < lines.size(); j++) {
                Line line1 = lines.get(j);
                double length = lengthMapping.get(line1);

                double x = cp[0] + Math.sqrt(length * length / (4 * (k2 * k2 + 1)));
//                double y = cp[1] + Math.sqrt(k2 * k2 * length * length / (4 * (k2 * k2 + 1)));
                double y = (r * r + cp[0] * cp[0] + cp[1] * cp[1] - cpX * cpX - cpY * cpY - 2 * x * (cp[0] - cpX)) / (2 * (cp[1] - cpY));

                double x1 = cp[0] - Math.sqrt(length * length / (4 * (k2 * k2 + 1)));
//                double y1 = cp[1] - Math.sqrt(k2 * k2 * length * length / (4 * (k2 * k2 + 1)));
                double y1 = (r * r + cp[0] * cp[0] + cp[1] * cp[1] - cpX * cpX - cpY * cpY - 2 * x1 * (cp[0] - cpX)) / (2 * (cp[1] - cpY));
                Line line2 = new Line(cp[0], cp[1], x, y);
                Line line3 = new Line(cp[0], cp[1], x1, y1);
                an.getChildren().remove(line1);
                an.getChildren().addAll(line2, line3);
            }

        });

        //画圆
        b1.setOnAction(event -> {
            draw.set(true);
            show.set(false);
            drawLine.set(false);
            System.out.println("shucu");
        });

        //约束
        b3.setOnAction(event -> {
            Iterator<Circle> iterator =  circles.iterator();
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
