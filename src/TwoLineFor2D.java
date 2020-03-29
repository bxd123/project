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

//这是直线共点的方法类
public class TwoLineFor2D extends Application {

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

                    if (drawLine.get()) {
                        Circle circle = line.createPoint(event.getX(), event.getY(), Color.BLUE);
                        Line line1 = line.drawLine(list.get(i[0] - 1), list.get(i[0]), event.getX(), event.getY());
                        mylines.add(line1);
                        arrow.getChildren().add(line1);
                        arrow.getChildren().add(circle);
                    }
                }
            }

            i[0]++;
        });

        b2.setOnAction(event -> {

            drawCallOut.set(false);
            drawLine.set(true);
            mylines.clear();
            System.out.println("绘制线");

        });

        double[] cp = new double[4];
        List<Double> kList = new ArrayList<>();
        List<Double> lList = new ArrayList<>();

        //直线共点事件触发
        b1.setOnAction(event -> {
            drawCallOut.set(true);
            drawLine.set(false);
            System.out.println("共点");

            if (mylines.size() > 1) {

                for (int j = 0; j < mylines.size(); j++) {
                    Line line1 = mylines.get(j);
                    if (j==0) {
                        double x = line1.getEndX();
                        double y = line1.getEndY();
                        cp[0] = x;
                        cp[1] = y;
                        cp[2] = line1.getStartX();
                        cp[3] = line1.getStartY();

                    } else {
                        double startX = line1.getStartX();
                        double startY = line1.getStartY();
                        double endX = line1.getEndX();
                        double endY = line1.getEndY();

                        double k = (endY - startY) / (endX - startX);
                        double l = Math.sqrt((endY - startY) * (endY - startY) + (endX - startX) * (endX - startX));
                        kList.add(k);
                        lList.add(l);
                        arrow.getChildren().remove(mylines.get(j));
                    }
                }
            }

            if (kList.size() > 0 && lList.size() > 0) {
                for (int j = 0; j < kList.size(); j++) {
                    //共点线的两种位置，line1和 line3分别为所在共点的两个不同的方向，夹角之和为180°
                    double startx = cp[0] + Math.sqrt(lList.get(j) * lList.get(j) / (kList.get(j) * kList.get(j) + 1));
                    double starty = kList.get(j) * (startx - cp[0]) + cp[1];
                    double startx1 = cp[0] - Math.sqrt(lList.get(j) * lList.get(j) / (kList.get(j) * kList.get(j) + 1));
                    double starty1 = kList.get(j) * (startx1 - cp[0]) + cp[1];

//                    double endX = cp[2] + Math.sqrt(lList.get(j) * lList.get(j) / (kList.get(j) * kList.get(j) + 1));
//                    double endY = kList.get(j) * (endX - cp[2]) + cp[3];
                    Line line1 = new Line(startx, starty, cp[0], cp[1]);
//                    Line line2 = new Line(endX, endY, cp[2], cp[3]);
                    Line line3 = new Line(startx1, starty1, cp[0], cp[1]);
                    Line line4 = new Line(cp[0], cp[1], cp[2], cp[3]);

                    arrow.getChildren().addAll(line1, line4);
                    if (mylines.size() > 0) {
                        if (arrow.getChildren().contains(mylines.get(0)))
                            arrow.getChildren().remove(mylines.get(0));
                        mylines.remove(0);
                    }

                }
            }
        });

        b3.setOnAction(event -> {
            cp[0] += 50;
            cp[1] += 50;


//            arrow.getChildren().clear();
        });
    }
}
