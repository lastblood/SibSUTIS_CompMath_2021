import kotlin.math.abs

infix fun RectangleMatrix.apply(vector: DoubleArray): DoubleArray = values.map { row -> row.mapIndexed { i, x -> x * vector[i] }.sum() }.toDoubleArray()

operator fun RectangleMatrix.get(row: Int, column: Int): Double = values[row][column]
operator fun RectangleMatrix.set(row: Int, column: Int, value: Double) { values[row][column] = value }

operator fun DoubleArray.minus(other: DoubleArray): DoubleArray = DoubleArray(kotlin.math.min(size, other.size)) { index -> this[index] - other[index] }
operator fun DoubleArray.plus(other: DoubleArray): DoubleArray =  DoubleArray(kotlin.math.min(size, other.size)) { index -> this[index] + other[index] }

infix fun DoubleArray.maxDelta(other: DoubleArray): Double = (0 until kotlin.math.min(size, other.size)).maxOfOrNull { abs(this[it] - other[it]) }!!

fun derivativeBy(f: (Double) -> Double, at: Double, epsilon: Double, nth: Int = 1): Double =
    if (nth == 0) f(at) else ((derivativeBy(f, at + epsilon, epsilon, nth-1) - derivativeBy(f, at - epsilon, epsilon, nth-1)) / 2 / epsilon)

fun derivativeBy(f: (DoubleArray) -> Double, indexOf: Int, at: DoubleArray, epsilon: Double): Double {
    val temp = at[indexOf]
    at[indexOf] = temp - epsilon
    val y0 = f(at)
    at[indexOf] = temp + epsilon
    val y2 = f(at)
    at[indexOf] = temp
    return (y2 - y0) / 2 / epsilon
}

fun invertMatrix(matrix: RectangleMatrix): RectangleMatrix {
    assert(matrix.x == matrix.y)
    val e = RectangleMatrix(matrix.y, matrix.x) { y,x -> if(y==x) 1.0 else 0.0 }
    val result = SneakyPeakyRectangleMatrix(matrix, e)
    gauss(result, true)
    return result.companion
}