package callout;

import com.sun.imageio.stream.CloseableDisposerRecord;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * 绘制箭头
 *
 * @author xiangqian
 * @date 16:00 2019/10/31
 */
public class DrawArrow extends Application {


    /**
     * 绘制面板
     *
     * @author xiangqian
     * @date 15:38 2019/11/03
     */
    public static class DrawPanel extends AnchorPane {

        private Canvas canvas;
        private SimpleDoubleProperty textvalue = new SimpleDoubleProperty(0.0);
        private SimpleDoubleProperty width = new SimpleDoubleProperty(600);
        private SimpleDoubleProperty height = new SimpleDoubleProperty(600);

        public void setWidth(double w) {
            width.set(w);
            canvas.setWidth(w);
        }
        public void setHeighth(double h) {
            height.set(h);
            canvas.setHeight(h);
        }

        public double getCanvasWidth() {
            return width.get();
        }

        public double getCanvasHeight() {
            return height.get();
        }

        public DrawPanel() {

            canvas = new Canvas(width.doubleValue(), height.doubleValue());
            this.getChildren().add(canvas);

//            draw();
        }

        /**
         * 绘制
         *
         * @param g2d
         */

        private Line line2D = new Line(0, 0, 1, 1);

        public void setStart(double x, double y) {
            line2D.setStartX(x);
            line2D.setStartY(y);
        }

        public void setEnd(double x, double y) {
            line2D.setEndX(x);
            line2D.setEndY(y);
        }

        java.util.List<Line> line;
        public void change(java.util.List<Line> line) {
            this.line = line;
            TextField textField = new TextField();
            this.getChildren().add(textField);
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {

//                    double startX = line2D.getStartX();
//                    double startY = line2D.getStartY();

                    canvas.getGraphicsContext2D().clearRect(0.0, 0.0, width.doubleValue(), height.doubleValue());
//                    setStart(startX, startY);
                    //水平方向
                    setEnd(line2D.getStartX() + Double.valueOf(newValue), line2D.getEndY());
                    draw();
//                    line.setEndY( line2D.getEndY());
                    System.out.println("输出");
                }
            });
            draw();

        }

        public void draw() {

            Arrow.Attributes arrowAttributes = null;

//             绘制线的“方向1”箭头
//            line2D = new Line(120, 100, 300, 300);
//            arrowAttributes = new Arrow.Attributes();
//            drawLineArrowDirection1(g2d, arrowAttributes, line2D);
//
//            // 绘制线的“方向2”箭头
//            line2D = new Line(110, 330, 300, 330);
//            arrowAttributes = new Arrow.Attributes();
//            arrowAttributes.angle = 45;
//            arrowAttributes.height = 80;
//            drawLineArrowDirection2(g2d, arrowAttributes, line2D);

            // 绘制线的“双向”箭头

            arrowAttributes = new Arrow.Attributes();
            arrowAttributes.angle = 30;
            arrowAttributes.height = 30;
            drawLineArrowDirectionAll(canvas, arrowAttributes, line2D);
        }


        /**
         * 绘制线的“方向1”箭头
         *
         * @param g2d
         * @param arrowAttributes 箭头属性
         * @param line2D          线
         */
        private void drawLineArrowDirection1(Canvas g2d, Arrow.Attributes arrowAttributes, Line line2D) {
            drawLine(g2d, line2D);
            javafx.geometry.Point2D point1 = new javafx.geometry.Point2D(line2D.getStartX(), line2D.getStartY());
            javafx.geometry.Point2D point2 = new javafx.geometry.Point2D(line2D.getEndX(), line2D.getEndY());
            drawArrow(g2d, arrowAttributes, point1, point2);
        }

        /**
         * 绘制线的“方向2”箭头
         *
         * @param g2d
         * @param arrowAttributes 箭头属性
         * @param line2D          线
         */
        private void drawLineArrowDirection2(Canvas g2d, Arrow.Attributes arrowAttributes, Line line2D) {
            drawLine(g2d, line2D);
            javafx.geometry.Point2D point1 = new javafx.geometry.Point2D(line2D.getStartX(), line2D.getStartY());
            javafx.geometry.Point2D point2 = new javafx.geometry.Point2D(line2D.getEndX(), line2D.getEndY());
            drawArrow(g2d, arrowAttributes, point2, point1);
        }

        /**
         * 绘制线的“双向”箭头
         *
         * @param g2d
         * @param arrowAttributes 箭头属性
         * @param line2D          线
         */
        private void drawLineArrowDirectionAll(Canvas g2d, Arrow.Attributes arrowAttributes, Line line2D) {
            drawLine(g2d, line2D);

            javafx.geometry.Point2D point1 = new javafx.geometry.Point2D(line2D.getStartX(), line2D.getStartY());
            javafx.geometry.Point2D point2 = new javafx.geometry.Point2D(line2D.getEndX(), line2D.getEndY());

            drawArrow(g2d, arrowAttributes, point1, point2);
            drawArrow(g2d, arrowAttributes, point2, point1);
        }


        /**
         * 绘制线
         *
         */
        private void drawLine(Canvas canvas, Line l) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setStroke(Color.BLACK);
            gc.beginPath();
            gc.moveTo(l.getStartX(), l.getStartY());
            gc.lineTo(l.getEndX(), l.getEndY());
            for (int i = 0; i < line.size(); i++) {
                line.get(i).setEndX(l.getEndX());
            }
//            line.setEndY(l.getEndY());
            gc.stroke();
        }

        /**
         * 绘制箭头
         *
         * @param arrowAttributes 箭头属性
         * @param point1          线的第一个点
         * @param point2          线的第二个点
         */
        private void drawArrow(Canvas canvas, Arrow.Attributes arrowAttributes, javafx.geometry.Point2D point1, javafx.geometry.Point2D point2) {
            // 获取Arrow实例
            Arrow arrow = getArrow(arrowAttributes, point1, point2);
            // 绘制
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.BLACK);
            gc.beginPath();
            gc.moveTo(arrow.point1.getX(), arrow.point1.getY());
            gc.lineTo(arrow.point2.getX(), arrow.point2.getY());
            gc.lineTo(arrow.point3.getX(), arrow.point3.getY());
            gc.closePath();
            gc.fill();
            //绘制两道杠
            drawTopLine(canvas, arrow);
        }

        private void drawTopLine(Canvas g2d, Arrow arrow) {
            double offsetx = arrow.point1.getX() - (arrow.point2.getX() + arrow.point3.getX()) / 2;
            double offsety = arrow.point1.getY() - (arrow.point2.getY() + arrow.point3.getY()) / 2;
            Point2D point2D001 = new Point2D.Double(arrow.point2.getX() + offsetx, arrow.point2.getY() + offsety);
            Point2D point2D002 = new Point2D.Double(arrow.point3.getX() + offsetx, arrow.point3.getY() + offsety);
            double offsetx01 = point2D001.getX() - arrow.point1.getX();
            double offsety01 = point2D001.getY() - arrow.point1.getY();

            Line line2D = new Line(arrow.point1.getX() + offsetx01 * 1.5, arrow.point1.getY() + offsety01 * 1.5, arrow.point1.getX() - offsetx01 * 1.5, arrow.point1.getY() - offsety01 * 1.5);
            drawLine(g2d, line2D);
        }

        /**
         * 获取箭头实体类
         *
         * @param arrowAttributes 箭头属性
         * @param point1          线的第一个点
         * @param point2          线的第二个点
         * @return
         */
        private Arrow getArrow(Arrow.Attributes arrowAttributes, javafx.geometry.Point2D point1, javafx.geometry.Point2D point2) {
            Arrow arrow = new Arrow(arrowAttributes);

            // 计算斜边
            double hypotenuse = arrow.attributes.height / Math.cos(Math.toRadians(arrow.attributes.angle / 2));

            // 计算当前线所在的象限
            int quadrant = -1;
            if (point1.getX() > point2.getX() && point1.getY() < point2.getY()) {
                quadrant = 1;
            } else if (point1.getX() < point2.getX() && point1.getY() < point2.getY()) {
                quadrant = 2;
            } else if (point1.getX() < point2.getX() && point1.getY() > point2.getY()) {
                quadrant = 3;
            } else if (point1.getX() > point2.getX() && point1.getY() > point2.getY()) {
                quadrant = 4;
            }

            // 计算线的夹角
            double linAngle = getLineAngle(point1.getX(), point1.getY(), point2.getX(), point2.getY());
            if (Double.isNaN(linAngle)) {
                // 线与x轴垂直
                if (point1.getX() == point2.getX()) {
                    if (point1.getY() < point2.getY()) {
                        linAngle = 90;
                    } else {
                        linAngle = 270;
                    }
                    quadrant = 2;
                }
            }
            // 线与y轴垂直
            else if (linAngle == 0) {
                if (point1.getY() == point2.getY()) {
                    if (point1.getX() < point2.getX()) {
                        linAngle = 0;
                    } else {
                        linAngle = 180;
                    }
                    quadrant = 2;
                }
            }

            // 上侧一半箭头
            double xAngle = linAngle - arrow.attributes.angle / 2; // 与x轴夹角
            double py0 = hypotenuse * Math.sin(Math.toRadians(xAngle)); // 计算y方向增量
            double px0 = hypotenuse * Math.cos(Math.toRadians(xAngle)); // 计算x方向增量

            // 下侧一半箭头
            double yAngle = 90 - linAngle - arrow.attributes.angle / 2; // 与y轴夹角
            double px1 = hypotenuse * Math.sin(Math.toRadians(yAngle));
            double py1 = hypotenuse * Math.cos(Math.toRadians(yAngle));

            // 第一象限
            if (quadrant == 1) {
                px0 = -px0;
                px1 = -px1;

            } else if (quadrant == 2) {
                // do nothing
            } else if (quadrant == 3) {
                py0 = -py0;
                py1 = -py1;

            } else if (quadrant == 4) {
                py0 = -py0;
                px0 = -px0;

                px1 = -px1;
                py1 = -py1;
            }

            // build
            arrow.point1 = new javafx.geometry.Point2D(point1.getX(), point1.getY());

            arrow.point2 = new javafx.geometry.Point2D(point1.getX() + px0, point1.getY() + py0);

            arrow.point3 = new javafx.geometry.Point2D(point1.getX() + px1, point1.getY() + py1);
            return arrow;
        }

        /**
         * 获取线与X轴的夹角
         *
         * @param x1
         * @param y1
         * @param x2
         * @param y2
         * @return
         */
        protected double getLineAngle(double x1, double y1, double x2, double y2) {
            double k1 = (y2 - y1) / (x2 - x1);
            double k2 = 0;
            return Math.abs(Math.toDegrees(Math.atan((k2 - k1) / (1 + k1 * k2))));
        }
    }


    /**
     * 箭头实体类
     *
     * @author xiangqian
     * @date 16:06 2019/10/31
     */
    public static class Arrow {
        Attributes attributes;
        javafx.geometry.Point2D point1;
        javafx.geometry.Point2D point2;
        javafx.geometry.Point2D point3;

        public Arrow(Attributes attributes) {
            this.attributes = attributes;
        }

        /**
         * 箭头属性
         *
         * @author xiangqian
         * @date 15:41 2019/11/03
         */
        public static class Attributes {
            double height; // 箭头的高度
            double angle; // 箭头角度
            javafx.scene.paint.Color color; // 箭头颜色

            public Attributes() {
                this.height = 60;
                this.angle = 30;
                this.color = javafx.scene.paint.Color.BLACK;
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane an = new DrawPanel();
        Scene scene = new Scene(an, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}

