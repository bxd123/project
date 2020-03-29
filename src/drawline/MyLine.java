package drawline;

import callout.Callout;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class MyLine extends Line implements Callout {

    private Point2D start = new Point2D(0,0);
    private Point2D end = new Point2D(0, 0);

    public void setStart(double x, double y) {
        start.add(x, y);
    }

    public void setEnd(double x, double y) {
        end.add(x, y);
    }

    public Line drawLine(double ... x) {
        Line line = new Line();

        if (x.length == 2) {
            line.setStartX(x[0]);
            line.setStartY(x[1]);
        }

        if (x.length == 4) {
            line.setStartX(x[0]);
            line.setStartY(x[1]);
            line.setEndY(x[3]);
            line.setEndX(x[2]);
        }
        return line;
    }

    public Circle createPoint(double x, double y, Color color) {
        Circle circle1 = new Circle();
        circle1.setCenterX(x);
        circle1.setCenterY(y);
        circle1.setRadius(2);
        circle1.setFill(color == null ? Color.RED : color);
        return circle1;
    }

    @Override
    public boolean verticalCall() {
        return false;
    }

    @Override
    public boolean parallelCall() {
        return false;
    }


}
