import java.util.*
import java.util.function.Function
import kotlin.math.*
import kotlin.streams.toList

infix fun RectangleMatrix.apply(vector: DoubleArray): DoubleArray = values.map { row -> row.mapIndexed { i, x -> x * vector[i] }.sum() }.toDoubleArray()

operator fun RectangleMatrix.get(row: Int, column: Int): Double = values[row][column]
operator fun RectangleMatrix.set(row: Int, column: Int, value: Double) { values[row][column] = value }

operator fun DoubleArray.minus(other: DoubleArray): DoubleArray = DoubleArray(min(size, other.size)) { index -> this[index] - other[index] }
operator fun DoubleArray.plus(other: DoubleArray): DoubleArray =  DoubleArray(min(size, other.size)) { index -> this[index] + other[index] }

infix fun DoubleArray.maxDelta(other: DoubleArray): Double = (0 until min(size, other.size)).maxOfOrNull { abs(this[it] - other[it]) }!!

val <A> Pair<A,*>.x get() = first
val <B> Pair<*,B>.y get() = second

fun derivativeBy(f: (Double) -> Double, at: Double, epsilon: Double, nth: Int = 1): Double =
    if (nth == 0) f(at) else ((derivativeBy(f, at + epsilon, epsilon, nth-1) - derivativeBy(f, at - epsilon, epsilon, nth-1)) / 2 / epsilon)

fun <A,B> MutableMap<A,B>.computeIfNeeded(key: A, mappingFunction: Function<in A, out B>): B {
    if(!containsKey(key)) this[key] = mappingFunction.apply(key)
    return this[key]!!
}

fun <A> iterateToList(seed: A, count: Int, generator: (Int, A) -> A, reversed: Boolean = false): List<A> {
    val result = arrayListOf(seed)
    (1 until count).forEach { result.add(generator(if(reversed) count - it - 1 else it, result.last())) }
    return if(reversed) result.reversed() else result
}

fun generateListAt(func: (Double) -> Double, range: Pair<Double, Double>, count: Int) =
        SplittableRandom().doubles(range.first, range.second)
                .limit(count.toLong()).sorted()
                .mapToObj { it to func(it) }.toList()

infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
    return generateSequence(start) { previous ->
        if (previous == Double.POSITIVE_INFINITY || previous + step > endInclusive) null
        else previous + step
    }.asIterable()
}

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