import java.lang.IllegalArgumentException
import java.util.*
import kotlin.math.abs
import kotlin.math.absoluteValue

fun gauss(matrix: RectangleMatrix): DoubleArray {
    with(matrix) {
        for (i in 0 .. y-2) {
            swapRows(i, (i until y).maxBy { abs(values[it][i]) } ?: i)
            for (j in i+1 until y) {
                if(values[j][i] != 0.0) {
                    divideRow(j, values[j][i] / values[i][i])
                    subtractRows(j, i)
                }
            }
        }

        val roots = DoubleArray(y)
        for (i in roots.indices.reversed()) {
            val knownRootsSum = (i+1 until x-1).sumByDouble { values[i][it] * roots[it] }
            roots[i] = (values[i][x-1] - knownRootsSum) / values[i][i]
        }
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

    if(printLogs) println(roots.contentToString())
    val maxDelta = newRoots.zip(roots).map { (x, y) -> abs(x - y) }.max()!!
    return if(maxDelta > epsilon) iterative(matrix, newRoots, seidel, epsilon) else newRoots
}

fun thomas(matrix: RectangleMatrix): DoubleArray {
    with(matrix) {
        val v = DoubleArray(y).also { it[0] = values[0][1] / values[0][0] }
        val u = DoubleArray(y).also { it[0] = values[0][y-1] / values[0][0] }

        for (i in 1 until y) {
            val a = values[i]
            v[i] = (if(i != y-1) a[i+1] else 0.0) / (-a[i] - a[i-1] * v[i-1])
            u[i] = (a[i-1] * u[i-1] - a.last()) / (-a[i] - a[i-1] * v[i-1])
        }

        val roots = DoubleArray(y+1).also { it[y] = u.last() }
        (y-1..0).forEach { i -> roots[i] = v[i] * roots[i+1] + u[i] }
        return roots.dropLast(1).toDoubleArray()
    }
}

fun getRandomMatrix(x: Int, y: Int, origin: Double = 0.0, bound: Double = 1.0, r: SplittableRandom = SplittableRandom(System.nanoTime())): RectangleMatrix {
    val rectangleMatrix = RectangleMatrix(x, y)
    for (i in 0 until y) {
        rectangleMatrix.values[i] = r.doubles(x.toLong(), origin, bound).toArray()
    }
    return rectangleMatrix
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