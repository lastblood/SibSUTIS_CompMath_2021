import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class RectangleMatrix {
    public double[][] values;
    private final int x, y;


    public RectangleMatrix(double[][] values) {
        this.values = values;
        if(Arrays.stream(values).map(x -> x.length).anyMatch(x -> x != values[0].length)) {
            throw new IllegalArgumentException("Не прямоугольная матрица");
        }
        y = values.length;
        x = values[0].length;
    }

    public RectangleMatrix(int y, int x) {
        this.y = y;
        this.x = x;
        values = new double[y][x];
    }

    public RectangleMatrix(int y, int x, BiFunction<Integer, Integer, Double> initializer) {
        this(y, x);
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                values[i][j] = initializer.apply(i, j);
            }
        }
    }

    public void swapRows(int rowIndex1, int rowIndex2) {
        if(rowIndex1 != rowIndex2) {
            double[] temp = values[rowIndex1];
            values[rowIndex1] = values[rowIndex2];
            values[rowIndex2] = temp;
        }
    }

    public void divideRow(int index, double by) {
        for (int i = 0; i < x; i++) {
            values[index][i] /= by;
        }
    }

    public void subtractRowMultipliedBy(int index, int otherIndex, double coeff) {
        for (int i = 0; i < x; i++) {
            values[index][i] -= values[otherIndex][i] * coeff;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return Arrays.stream(values)
                .map(x -> Arrays.stream(x)
                        .mapToObj(y -> String.format("%5.5f ", y))
                        .collect(Collectors.joining("\t"))
                ).collect(Collectors.joining("\n")) + "\n";
    }

    @Override
    protected RectangleMatrix clone() throws CloneNotSupportedException {
        double[][] newValues = new double[y][x];
        for (int i = 0; i < y; i++) {
            newValues[i] = Arrays.copyOf(values[i], x);
        }
        return new RectangleMatrix(newValues);
    }
}
