import kotlin.math.exp

fun main() {
    lab4()
}

fun lab1() {
    val matrix = getMatrixDef()
    println(matrix)

    println(performGauss(matrix).contentToString())
    println(matrix)
}

fun lab2() {
    val matrix = strengthenDiagonal(getMatrixDef())
    println(matrix)

    performIterative(matrix.clone(), seidel = false, epsilon = 1e-6, printLogs = true)
    performIterative(matrix.clone(), seidel = true, epsilon = 1e-6, printLogs = true)
}

//todo: протестировать
fun lab3() {
    val matrix = onlyDiagonals(getMatrixDef())
    println(matrix)

    println(performThomas(matrix).contentToString())
}

// Нелинейные уравнения: бисекция, хорды, Ньютон
fun lab4() {
    val f = { x: Double -> x / (x + exp(-x)) - 0.5 }
//    performBisection(f, -10.0, 8.3).apply { println(this) }
    performChords(f).apply { println(this) }
}


fun getMatrixDef(): RectangleMatrix {
    return getRandomMatrix(6,5, -10.0, 10.0)
}