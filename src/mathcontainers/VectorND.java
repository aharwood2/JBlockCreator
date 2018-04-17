package mathcontainers;

/**
 * Generic vector class -- needs a whole load of run-time checking adding.
 */
public class VectorND
{

    int size;
    protected double[] val;

    public VectorND(int size)
    {
        this.size = size;
        val = new double[size];
    }

    public VectorND(int size, double[] data)
    {
        this(size);
        val = new double[size];
        for (int i = 0; i < size; i++)
            val[i] = data[i];
    }

    public VectorND(VectorND otherVec)
    {
        this(otherVec.size);
        for (int i = 0; i < size; i++)
            val[i] = otherVec.val[i];
    }

    public double norm()
    {
        double sum = 0.0;
        for (int i = 0; i < size; i++)
            sum += val[i] * val[i];
        return Math.sqrt(sum);
    }

    public double dot(VectorND otherVec)
    {
        double sum = 0.0;
        for (int i = 0; i < size; i++)
            sum += val[i] * otherVec.val[i];
        return Math.sqrt(sum);
    }

    public void addTo(double num)
    {
        for (int i = 0; i < size; i++)
            val[i] += num;
    }

    public void addTo(VectorND otherVec)
    {
        for (int i = 0; i < size; i++)
            val[i] += otherVec.val[i];
    }

    public void subtractThis(double num)
    {
        for (int i = 0; i < size; i++)
            val[i] -= num;
    }

    public void subtractThis(VectorND otherVec)
    {
        for (int i = 0; i < size; i++)
            val[i] -= otherVec.val[i];
    }

    public void multiplyBy(double num)
    {
        for (int i = 0; i < size; i++)
            val[i] *= num;
    }

    public void multiplyBy(VectorND otherVec)
    {
        for (int i = 0; i < size; i++)
            val[i] *= otherVec.val[i];
    }

    public void divideBy(double num)
    {
        for (int i = 0; i < size; i++)
            val[i] /= num;
    }

    public void divideBy(VectorND otherVec)
    {
        for (int i = 0; i < size; i++)
            val[i] /= otherVec.val[i];
    }

    public VectorND add(double num)
    {
        VectorND tmp = new VectorND(size);
        for (int i = 0; i < size; i++)
            tmp.val[i] = val[i] + num;
        return tmp;
    }

    public VectorND add(VectorND otherVec)
    {
        VectorND tmp = new VectorND(size);
        for (int i = 0; i < size; i++)
            tmp.val[i] = val[i] + otherVec.val[i];
        return tmp;
    }

    public VectorND subtract(double num)
    {
        VectorND tmp = new VectorND(size);
        for (int i = 0; i < size; i++)
            tmp.val[i] = val[i] - num;
        return tmp;
    }

    public VectorND subtract(VectorND otherVec)
    {
        VectorND tmp = new VectorND(size);
        for (int i = 0; i < size; i++)
            tmp.val[i] = val[i] - otherVec.val[i];
        return tmp;
    }

    public VectorND multiply(double num)
    {
        VectorND tmp = new VectorND(size);
        for (int i = 0; i < size; i++)
            tmp.val[i] = val[i] * num;
        return tmp;
    }

    public VectorND multiply(VectorND otherVec)
    {
        VectorND tmp = new VectorND(size);
        for (int i = 0; i < size; i++)
            tmp.val[i] = val[i] * otherVec.val[i];
        return tmp;
    }

    public VectorND divide(double num)
    {
        VectorND tmp = new VectorND(size);
        for (int i = 0; i < size; i++)
            tmp.val[i] = val[i] / num;
        return tmp;
    }

    public VectorND divide(VectorND otherVec)
    {
        VectorND tmp = new VectorND(size);
        for (int i = 0; i < size; i++)
            tmp.val[i] = val[i] / otherVec.val[i];
        return tmp;
    }

    public int size()
    {
        return size;
    }

    public double get(int i) throws IndexOutOfBoundsException
    {
        if (i >= size) throw new IndexOutOfBoundsException();
        else return val[i];
    }
}
