package mathcontainers;

/**
 * Quick Matrix class -- need to add run-time checking for sizes and things.
 */
public class Matrix2D
{
    protected int numRows;
    protected int numCols;
    protected double[][] val;

    public Matrix2D(int numRows, int numCols, double[][] data)
    {
        this.numRows = numRows;
        this.numCols = numCols;

        // Copy in data
        val = new double[numRows][numCols];
        for (int i = 0; i < data.length; i++)
        {
            System.arraycopy(data[i], 0, val[i], 0, numCols);
        }
    }

    /**
     * Constructor assuming a 2 x 2 matrix
     *
     * @param a00 1st row, 1st column
     * @param a01 1st row, 2nd column
     * @param a10 2nd row, 1st column
     * @param a11 2nd row, 2nd column
     */
    public Matrix2D(double a00, double a01, double a10, double a11)
    {
        numRows = 2;
        numCols = 2;

        // Set data
        val = new double[numRows][numCols];
        val[0][0] = a00;
        val[0][1] = a01;
        val[1][0] = a10;
        val[1][1] = a11;
    }

    /**
     * Matrix inversion code taken from SanFoundry.com -- unmodified.
     */
    private static double[][] invert(double a[][])
    {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i = 0; i < n; ++i)
            b[i][i] = 1;

        // Transform the matrix into an upper triangle
        gaussian(a, index);

        // Update the matrix b[i][j] with the ratios stored
        for (int i = 0; i < n - 1; ++i)
            for (int j = i + 1; j < n; ++j)
                for (int k = 0; k < n; ++k)
                    b[index[j]][k]
                            -= a[index[j]][i] * b[index[i]][k];

        // Perform backward substitutions
        for (int i = 0; i < n; ++i)
        {
            x[n - 1][i] = b[index[n - 1]][i] / a[index[n - 1]][n - 1];
            for (int j = n - 2; j >= 0; --j)
            {
                x[j][i] = b[index[j]][i];
                for (int k = j + 1; k < n; ++k)
                {
                    x[j][i] -= a[index[j]][k] * x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }

    // Method to carry out the partial-pivoting Gaussian elimination.  Here index[] stores pivoting order.
    private static void gaussian(double a[][], int index[])
    {
        int n = index.length;
        double c[] = new double[n];

        // Initialize the index
        for (int i = 0; i < n; ++i)
            index[i] = i;

        // Find the rescaling factors, one from each row
        for (int i = 0; i < n; ++i)
        {
            double c1 = 0;
            for (int j = 0; j < n; ++j)
            {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }

        // Search the pivoting element from each column
        int k = 0;
        for (int j = 0; j < n - 1; ++j)
        {
            double pi1 = 0;
            for (int i = j; i < n; ++i)
            {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1)
                {
                    pi1 = pi0;
                    k = i;
                }
            }

            // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i = j + 1; i < n; ++i)
            {
                double pj = a[index[i]][j] / a[index[j]][j];

                // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;

                // Modify other elements accordingly
                for (int l = j + 1; l < n; ++l)
                    a[index[i]][l] -= pj * a[index[j]][l];
            }
        }
    }

    public void transpose()
    {
        double[][] tmp = new double[numRows][numCols];
        for (int i = 0; i < numRows; i++)
        {
            for (int j = 0; j < numCols; j++)
            {
                tmp[j][i] = val[i][j];
            }
        }
        val = tmp;
    }

    public VectorND postMultiply(VectorND columnVector)
    {
        VectorND tmp = new VectorND(numCols);
        for (int i = 0; i < numRows; i++)
        {
            tmp.val[i] = 0.0;
            for (int j = 0; j < numCols; j++)
            {
                tmp.val[i] += val[i][j] * columnVector.val[j];
            }
        }
        return tmp;
    }

    // Wrapper for the inversion code
    public Matrix2D invert()
    {
        return new Matrix2D(numRows, numCols, invert(val));
    }

}
