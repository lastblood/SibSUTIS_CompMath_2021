import java.util.Arrays;
import java.util.SplittableRandom;
import java.util.stream.Collectors;

public class RectangleMatrix {
    public double[][] values;
    private final int x, y;

    public void swapRows(int rowIndex1, int rowIndex2) {
        if(rowIndex1 != rowIndex2) {
            double[] temp = values[rowIndex1];
            values[rowIndex1] = values[rowIndex2];
            values[rowIndex2] = temp;
        }
    }

    public void subtractRows(int from, int what) {
        for (int i = 0; i < x; i++) {
            values[from][i] -= values[what][i];
        }
    }

    public void divideRow(int index, double by) {
        for (int i = 0; i < x; i++) {
            values[index][i] /= by;
        }
    }

    public RectangleMatrix(double[][] values) {
        this.values = values;
        if(Arrays.stream(values).map(x -> x.length).anyMatch(x -> x != values[0].length)) {
            throw new IllegalArgumentException("Не прямоугольная матрица");
        }
        y = values.length;
        x = values[0].length;
    }

    public RectangleMatrix(int x, int y) {
        this.x = x;
        this.y = y;
        values = new double[y][x];
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
                        .mapToObj(y -> String.format("%5.2f ", y))
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
