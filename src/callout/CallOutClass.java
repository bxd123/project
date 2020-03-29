package callout;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

public class CallOutClass extends Shape {
    @Override
    public com.sun.javafx.geom.Shape impl_configShape() {
        return null;
    }

    final Pane pane;

    /**
     * 初始化时，传入绘制二维模型的面板
     * @param pane
     */
    public CallOutClass(Pane pane) {
        this.pane = pane;
    }

    public CallOutClass() {
        pane = new Pane();
    }

    private boolean remvoeShapeFromPane(Shape shape) {

        int shapeNum = pane.getChildren().size();
        if (shapeNum > 0 && pane.getChildren().contains(shape)) {
            pane.getChildren().remove(shape);
            return true;
        }
        return false;
    }

    /**
     * 该方法用于删除面板上的二维图形，例如直线、圆等等Shape的子类
     * @param shapes
     */
    public void removerShapes(Shape ... shapes) {

        int shapesNum = shapes.length;
            for (int i = 0; i < shapesNum; i++) {
                remvoeShapeFromPane(shapes[i]);
            }
    }

    /**
     * 根据被共点直线的斜率和长度，重新添加被共点直线
     * @param lines
     * @return
     */
    private boolean addCommonPointLine(Line ... lines) {

        if (kList.size() > 0 && lList.size() > 0) {
            for (int j = 0; j < kList.size(); j++) {
                //共点线的两种位置，line1和 line3分别为所在共点的两个不同的方向，夹角之和为180°
                double startx = basePoint[0] + Math.sqrt(lList.get(j) * lList.get(j) / (kList.get(j) * kList.get(j) + 1));
                double starty = kList.get(j) * (startx - basePoint[0]) + basePoint[1];
                double startx1 = basePoint[0] - Math.sqrt(lList.get(j) * lList.get(j) / (kList.get(j) * kList.get(j) + 1));
                double starty1 = kList.get(j) * (startx1 - basePoint[0]) + basePoint[1];

//                    double endX = cp[2] + Math.sqrt(lList.get(j) * lList.get(j) / (kList.get(j) * kList.get(j) + 1));
//                    double endY = kList.get(j) * (endX - cp[2]) + cp[3];
                Line line1 = new Line(startx, starty, basePoint[0], basePoint[1]);
//                    Line line2 = new Line(endX, endY, cp[2], cp[3]);
                Line line3 = new Line(startx1, starty1, basePoint[0], basePoint[1]);
                Line line4 = new Line(basePoint[0], basePoint[1], basePoint[2], basePoint[3]);

                lineList.add(line1);
                lineList.add(line4);
                pane.getChildren().addAll(line1, line4);

                if (lines.length > 0) {
                    if (pane.getChildren().contains(lines[0]))
                        pane.getChildren().remove(lines[0]);
//                    lines[0].remove(0);
                }

            }
            return true;
        }
        return false;
    }

    /**
     * 这里计算被共线的直线的斜率和长度
     * @param line 传入的被共线的直线
     * @return
     */
    List<Double> lList;
    List<Double> kList;
    List<Line> lineList;
    private void SlopeAndLengthList(Line line) {

        double startX = line.getStartX();
        double startY = line.getStartY();
        double endX = line.getEndX();
        double endY = line.getEndY();

        double k = (endY - startY) / (endX - startX);
        double l = Math.sqrt((endY - startY) * (endY - startY) + (endX - startX) * (endX - startX));
        kList.add(k);
        lList.add(l);
    }
    /**
     * 当前方法是直线共点的约束方法
     * 默认传入的第一个直线对象为基准直线
     * @param lines
     * @return
     */
    //基准直线的起始点数组
    final double[] basePoint = new double[4];
    public boolean twoLineFor2D(Line ... lines) {

        //这里创建新的共点直线集合
        List<Line> lineList = new ArrayList<>();
        this.lineList = lineList;

        //这里是在共点时，创建被共点直线的斜率和长度的集合
        List<Double> lList = new ArrayList();
        List<Double> kList = new ArrayList();
        this.lList = lList;
        this.kList = kList;

        int length = lines.length;

        if (length > 1) {
            for (int i = 0; i < length; i++) {
                Line line = lines[i];
                if (i == 0) {
                    basePoint[0] = line.getStartX();
                    basePoint[1] = line.getStartY();
                    basePoint[2] = line.getEndX();
                    basePoint[3] = line.getEndY();
//                    pane.getChildren().add(line);
                } else {
                    //计算被共线直线的斜率和长度
                    SlopeAndLengthList(line);
                    //从面板中删除被共线的直线
                    pane.getChildren().remove(line);
                }
            }
//            return true;
        }

        //共线的两种位置，添加被共线直线
        return addCommonPointLine(lines);
    }

    public List<Line> getLineFromPane() {
        return lineList;
    }

    //若要改变基准点的位置只需改变basePoint中的值即可,但是需要再调用一个twoLineFor2D方法
    public void setStart(double x, double y) {
        basePoint[0] = x;
        basePoint[1] = y;
    }
    public void setEnd(double x, double y) {
        basePoint[2] = x;
        basePoint[3] = y;
    }

    /*****************************************************************************************/

    /**
     * 这是圆与圆同心的约束方法
     * @param circles
     * @return
     */
    List<Circle> circleList;
    //0 X, 1 Y, 2 radius
    double[] circleBase = new double[3];
    //圆的填充颜色
    final Paint[] color = {null};
    //圆的边线颜色
    final Paint[] stroke = {null};
    public boolean CircleFor2D(Circle ... circles) {

        int length = circles.length;
        //创建圆的集合，方便删除圆
        List<Circle> circleList = new ArrayList<>();
        this.circleList = circleList;

        if (length > 1) {
            for (int i = 0; i < length; i++) {

                //默认第一个参数圆为基准圆
                Circle circle = circles[i];
                if (i == 0) {
                    circleBase[0] = circle.getCenterX();
                    circleBase[1] = circle.getCenterY();
                    circleBase[2] = circle.getRadius();
                } else {
                    double radius = circle.getRadius();
                    Circle circle1 = new Circle();
                    circle1.setCenterY(circleBase[1]);
                    circle1.setCenterX(circleBase[0]);
                    circle1.setFill(color[0]);
                    circle1.setRadius(radius);
                    circle1.setStroke(stroke[0] == null ? Color.RED : stroke[0]);
                    pane.getChildren().remove(circle);
                    pane.getChildren().add(circle1);
                }
            }
            return true;
        }
        return false;
    }

    /*******************************直线和圆相切约束************************************************/

    //切点数组
    final double[] cutPoint = new double[2];
    public boolean CirleLineFor2D(double x, double y, Shape ... shapes) {

        //给切点赋值
        cutPoint[0] = x;
        cutPoint[1] = y;

        int num = shapes.length;
        //圆心到切点构成的直线斜率
        double k1 = 0;
        //圆半径
        double r = 0;
        //过切点垂直于圆心到切点的直线的斜率
        double k2 = 0;

        //圆心X值
        double cpX = 0, cpY = 0;

        if (num > 1) {
            for (int i = 0; i < num; i++) {
                Shape shape = shapes[i];
                if (i == 0) {
                    cpX = ((Circle) shape).getCenterX();
                    cpY = ((Circle) shape).getCenterY();

                    k1 = (cutPoint[1] - cpY) / (cutPoint[0] - cpX);
                    r = Math.sqrt((cutPoint[1] - cpY) * (cutPoint[1] - cpY) + (cutPoint[0] - cpX) * (cutPoint[0] - cpX));

                    k2 = k1 == 0 ? 1 : -1 / k1;
                } else {
                    Line line = (Line) shape;
                    double startX = line.getStartX();
                    double startY = line.getStartY();
                    double endX = line.getEndX();
                    double endY = line.getEndY();
                    double length =  Math.sqrt((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY));

                    double x2 = cutPoint[0] + Math.sqrt(length * length / (4 * (k2 * k2 + 1)));
//                double y = cp[1] + Math.sqrt(k2 * k2 * length * length / (4 * (k2 * k2 + 1)));
                    double y2 = (r * r + cutPoint[0] * cutPoint[0] + cutPoint[1] * cutPoint[1] - cpX * cpX - cpY * cpY - 2 * x * (cutPoint[0] - cpX)) / (2 * (cutPoint[1] - cpY));

                    double x1 = cutPoint[0] - Math.sqrt(length * length / (4 * (k2 * k2 + 1)));
//                double y1 = cp[1] - Math.sqrt(k2 * k2 * length * length / (4 * (k2 * k2 + 1)));
                    double y1 = (r * r + cutPoint[0] * cutPoint[0] + cutPoint[1] * cutPoint[1] - cpX * cpX - cpY * cpY - 2 * x1 * (cutPoint[0] - cpX)) / (2 * (cutPoint[1] - cpY));
                    Line line2 = new Line(cutPoint[0], cutPoint[1], x2, y2);
                    Line line3 = new Line(cutPoint[0], cutPoint[1], x1, y1);
                    pane.getChildren().remove(line);
                    pane.getChildren().addAll(line2, line3);
                }
            }
        }
        return false;
    }

}
