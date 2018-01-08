package mathcontainers;

/**
 * Quick container for polynomial coefficients -- not generic is any way.
 */
public class PolyCoeffs
{
    public double a;
    public double b;
    public double c;
    public double d;

    public PolyCoeffs(VectorND vec)
    {
        this.a = vec.val[0];
        this.b = vec.val[1];
        this.c = vec.val[2];
        this.d = vec.val[3];
    }
}
