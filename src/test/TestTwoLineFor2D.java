package test;

import callout.CallOutClass;
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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestTwoLineFor2D extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane an = new AnchorPane();

        MyLine line = new MyLine();

        DrawArrow.DrawPanel arrow = new DrawArrow.DrawPanel();

        VBox vBox = new VBox(10);
        Button b1 = new Button("共点");
        Button b2 = new Button("绘制");
        Button b3 = new Button("改变共点");
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

        //这是画线的计数器
        final int[] i = {0};
        //这是线段的起点
        List<Double> list = new ArrayList<>();

        //这是线段的终点
        List<Double> list1 = new ArrayList<>();

        //这是所有直线的集合
        List<Line> mylines = new ArrayList<>();

        AtomicBoolean drawLine = new AtomicBoolean(false);
        AtomicBoolean drawCallOut = new AtomicBoolean(false);

        scene.setOnMousePressed(event -> {

            if (i[0] % 2 == 0) {
                System.out.println(i[0]);
                list.add(event.getX());
                list.add(event.getY());
                Circle circle = line.createPoint(event.getX(), event.getY(), null);
                arrow.getChildren().add(circle);
            } else {
                if (list.size() > 0) {

                    list1.add(event.getX());
                    list1.add(event.getY());

//                    if (drawLine.get()) {
                        Circle circle = line.createPoint(event.getX(), event.getY(), Color.BLUE);
                        Line line1 = line.drawLine(list.get(i[0] - 1), list.get(i[0]), event.getX(), event.getY());
                        mylines.add(line1);
//                        arrow.getChildren().add(line1);
                        arrow.getChildren().add(circle);
//                    }
                }
            }

            i[0]++;
        });
        CallOutClass callOutClass = new CallOutClass(arrow);
        b1.setOnAction(event -> {
            callOutClass.twoLineFor2D(mylines.toArray(new Line[mylines.size()]));
        });
    }
}
