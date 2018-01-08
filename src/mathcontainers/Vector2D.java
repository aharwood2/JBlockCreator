package mathcontainers;

/**
 * Vector container for 2D only with intuitive getters/setters
 */
public class Vector2D extends VectorND
{
    public Vector2D(double x, double y)
    {
        super(2, new double[] {x, y});
    }

    public Vector2D(VectorND otherVec)
    {
        super(otherVec);
    }

    public double getX()
    {
        return val[0];
    }

    public double getY()
    {
        return val[1];
    }
}
