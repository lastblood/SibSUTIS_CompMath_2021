import java.lang.IllegalArgumentException
import java.util.*
import kotlin.math.abs
import kotlin.math.absoluteValue

fun gauss(matrix: RectangleMatrix, jordan: Boolean = false): DoubleArray {
    with(matrix) {
        for (i in 0 .. y-2) {
            swapRows(i, (i until y).maxBy { abs(values[it][i]) } ?: i)
            for (j in i+1 until y) {
                if(values[j][i] != 0.0) {
                    subtractRowMultipliedBy(j, i, values[j][i] / values[i][i])
                }
            }
        }

        // В методе Йордана для каждой i-ой строки, при подъеме, будем обнулять только элементы в i-ом же столбце
        if(jordan) {
            for (i in y-1 downTo 0) {
                divideRow(i, values[i][i])
                for (j in i-1 downTo 0) {
                    subtractRowMultipliedBy(j, i, values[j][i])
                }
            }
        }

        val roots = DoubleArray(y)
        for (i in roots.indices.reversed())
            roots[i] = if(jordan) values[i].last()
                            else (values[i][x-1] - (i+1 until x-1).sumByDouble { values[i][it] * roots[it] }) / values[i][i]

        return roots
    }
}

@Throws(StackOverflowError::class)
fun iterative(matrix: RectangleMatrix, roots: DoubleArray = DoubleArray(matrix.y),
              seidel: Boolean = true, epsilon: Double = 1e-3, printLogs: Boolean = false): DoubleArray {

    if(roots.any { !it.isFinite() }) throw IllegalArgumentException("Не сходится")

    val newRoots = roots.clone()
    matrix.values.forEachIndexed { i, row ->
        val rSum = (0 until matrix.y).filter { it != i }.sumByDouble { row[it] * if (seidel) newRoots[it] else roots[it] }
        newRoots[i] = (row.last() - rSum) / row[i]
    }

    if(printLogs) println(newRoots.contentToString())
    val maxDelta = newRoots.zip(roots).map { (x, y) -> abs(x - y) }.max()!!
    return if(maxDelta > epsilon) iterative(matrix, newRoots, seidel, epsilon, printLogs) else newRoots
}

fun thomas(mx: RectangleMatrix): DoubleArray {
    val y = mx.y
    fun c(i: Int) = mx[i, i]
    fun f(i: Int) = mx[i, y]

    for (i in 1 until y) {
        val m = mx[i, i-1] / c(i-1)
        mx[i,i] = c(i) - m * mx[i-1,i]
        mx[i,y] = f(i) - f(i-1) * m
    }

    val roots = DoubleArray(y).also { it[y-1] = f(y-1)/c(y-1) }
    (y-2 downTo 0).forEach { i -> roots[i] = (f(i) - mx[i,i+1]*roots[i+1]) / c(i) }
    return roots
}

fun thomasArray(low: DoubleArray, mid: DoubleArray, high: DoubleArray, ans: DoubleArray): DoubleArray {
    val m = iterateToList(high[0]/mid[0], ans.size-1, { i, last -> high[i] / (mid[i] - low[i-1] * last) })
    val n = iterateToList(ans[0]/mid[0], ans.size, { i, last -> (ans[i] - low[i-1]*last) / (mid[i] - low[i-1]*m[i-1]) } )
    return iterateToList(n[ans.size-1], ans.size, { i, last -> n[i] - m[i]*last }, true).toDoubleArray()
}

fun getRandomMatrix(x: Int, y: Int, origin: Double = 0.0, bound: Double = 1.0, r: SplittableRandom = SplittableRandom(System.nanoTime())): RectangleMatrix {
    val values = Array<DoubleArray>(y) { r.doubles(x.toLong(), origin, bound).toArray() }
    return RectangleMatrix(values)
}

fun strengthenDiagonal(matrix: RectangleMatrix): RectangleMatrix {
    matrix.values.forEachIndexed { i,arr ->
        arr[i] = arr.sumOf { it.absoluteValue } * (Math.random() / 10 + 1) * (if(Math.random() > 0.5) 1 else -1)
    }
    return matrix
}

fun onlyDiagonals(matrix: RectangleMatrix, maxDeltaFromMain: Int = 1): RectangleMatrix {
    for (y in 0 until matrix.y)
        for(x in 0 until matrix.x-1)
            if(abs(x-y) > maxDeltaFromMain)
                matrix.values[y][x] = 0.0

    return matrix
}

fun fullMatrix(matrix: RectangleMatrix, vector: DoubleArray): RectangleMatrix =
    RectangleMatrix(matrix.y, matrix.x+1) { y,x -> if(x == matrix.x) vector[y] else matrix[y,x] }
