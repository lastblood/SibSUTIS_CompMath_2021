import kotlin.math.abs

infix fun RectangleMatrix.apply(vector: DoubleArray): DoubleArray = values.map { row -> row.mapIndexed { i, x -> x * vector[i] }.sum() }.toDoubleArray()

operator fun RectangleMatrix.get(row: Int, column: Int): Double = values[row][column]
operator fun RectangleMatrix.set(row: Int, column: Int, value: Double) { values[row][column] = value }

operator fun DoubleArray.minus(other: DoubleArray): DoubleArray = DoubleArray(kotlin.math.min(size, other.size)) { index -> this[index] - other[index] }
operator fun DoubleArray.plus(other: DoubleArray): DoubleArray =  DoubleArray(kotlin.math.min(size, other.size)) { index -> this[index] + other[index] }

infix fun DoubleArray.maxDelta(other: DoubleArray): Double = (0 until kotlin.math.min(size, other.size)).maxOfOrNull { abs(this[it] - other[it]) }!!