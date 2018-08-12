package analysis;

import java.util.ArrayList;

class Rectangle
{
    private double lowX;
    private double highX;
    private double lowY;
    private double highY;

    Rectangle(double _lowX, double _lowY, double _highX, double _highY)
    {
        this.lowX = _lowX;
        this.lowY = _lowY;
        this.highX = _highX;
        this.highY = _highY;
    }

    ArrayList<Double> getX()
    {
        ArrayList<Double> points = new ArrayList<>();
        points.add(lowX);
        points.add(highX);
        points.add(highX);
        points.add(lowX);
        points.add(lowX);
        return points;
    }

    ArrayList<Double> getY()
    {
        ArrayList<Double> points = new ArrayList<>();
        points.add(lowY);
        points.add(lowY);
        points.add(highY);
        points.add(highY);
        points.add(lowY);
        return points;
    }
}
