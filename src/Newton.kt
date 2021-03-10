fun newtonSystem(nlSystem: NLSystem, start: DoubleArray = DoubleArray(nlSystem.columns), epsilon: Double = 1e-3): DoubleArray {
    var lastRoots: DoubleArray
    var roots = start

    do {
        lastRoots = roots
        val jacobi = RectangleMatrix(nlSystem.columns, nlSystem.rows) { y, x -> derivativeBy(nlSystem[y], x, lastRoots, epsilon) }
        roots = lastRoots - (invertMatrix(jacobi) apply nlSystem(lastRoots))
    } while(lastRoots maxDelta roots > epsilon)

    return roots
}


fun derivativeBy(func: (DoubleArray) -> Double, indexOf: Int, at: DoubleArray, epsilon: Double): Double {
    val temp = at[indexOf]
    at[indexOf] = temp - epsilon
    val y0 = func(at)
    at[indexOf] = temp + epsilon
    val y2 = func(at)
    at[indexOf] = temp

    return (y2 - y0) / 2 / epsilon
}

fun invertMatrix(matrix: RectangleMatrix): RectangleMatrix {
    with(matrix) {
        assert(matrix.x == matrix.y)
        val e = RectangleMatrix(matrix.y, matrix.x) { y,x -> if(y==x) 1.0 else 0.0 }
        val result = SneakyPeakyRectangleMatrix(matrix, e)
        gauss(result, true)
        return result.companion
    }
}


class NLSystem(private val system: List<(DoubleArray) -> Double>, val columns: Int) {
    val rows: Int = system.size

    operator fun get(index: Int): (DoubleArray) -> Double = system[index]

    operator fun invoke(values: DoubleArray): DoubleArray = system.map { f -> f(values) }.toDoubleArray()
}


class SneakyPeakyRectangleMatrix(main: RectangleMatrix, val companion: RectangleMatrix) : RectangleMatrix(main.values) {
    override fun subtractRowMultipliedBy(index: Int, otherIndex: Int, coeff: Double) {
        super.subtractRowMultipliedBy(index, otherIndex, coeff)
        companion.subtractRowMultipliedBy(index, otherIndex, coeff)
    }

    override fun divideRow(index: Int, by: Double) {
        super.divideRow(index, by)
        companion.divideRow(index, by)
    }

    override fun swapRows(rowIndex1: Int, rowIndex2: Int) {
        super.swapRows(rowIndex1, rowIndex2)
        companion.swapRows(rowIndex1, rowIndex2)
    }
}