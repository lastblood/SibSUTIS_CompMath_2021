import kotlin.math.sin

fun main() {
    lab6()
    lab7()
}

fun lab1() {
    val matrix = getMatrixDef()
    println(matrix)

    val matrix1 = matrix.clone()
    println(gauss(matrix1).contentToString())
    println(matrix1)

    val matrix2 = matrix.clone()
    println(gauss(matrix2, true).contentToString())
    println(matrix2)
}

fun lab2() {
    val matrix = strengthenDiagonal(getMatrixDef())
    println(matrix)

    println(gauss(matrix.clone()).contentToString())
    println(iterative(matrix.clone(), seidel = false, epsilon = 1e-6).contentToString())
    println(iterative(matrix.clone(), seidel = true, epsilon = 1e-6).contentToString())
}

//todo: протестировать
fun lab3() {
    val matrix = onlyDiagonals(getMatrixDef())
    println(matrix)

    println(gauss(matrix.clone()).contentToString())
    println(gauss(matrix.clone()).contentToString())
    println(thomas(matrix).contentToString())
}

// Нелинейные уравнения: бисекция, хорды, Ньютон
fun lab4() {
    val f = { x: Double -> x*x*x/50 - 8*x - 81 }
    val dfdx = { x: Double -> 3*x*x/50 - 8 }
//    performBisection(f, -10.0, 8.3).apply { println(this) }
    chords(f, -100.0, 100.0, 1e-7).apply { println(this) }
    newton(f, dfdx,-100.0, 1e-7).apply { println(this) }
}

fun lab5() {
    val f1: (DoubleArray) -> Double = { sin(2*it[0] - it[1]) - 1.2 * it[0] - 0.4 }
    val f2: (DoubleArray) -> Double = { 0.8 * it[0] * it[0] + 1.5 * it[1] * it[1] - 1 }
    val system = NLSystem(listOf(f1, f2), 2)
    val result = newtonSystem(system, doubleArrayOf(1.0, -1.0), 1e-5)
    println(result.contentToString())
}

fun lab6() {
    val f = lagrange(listOf(1.0 to 2.0, 2.0 to 4.0, 3.0 to 6.0, 5.0 to 10.0))
    println(f(1.1))
    println(f(4.6))
    println(f(12.0))
}

fun lab7() {
    val f = Aitken(listOf(1.0 to 2.0, 2.0 to 4.0, 3.0 to 6.0, 5.0 to 10.0))
    println(f(1.1))
    println(f(4.6))
    println(f(12.0))
}

fun getMatrixDef(): RectangleMatrix {
    return getRandomMatrix(6,5, -10.0, 10.0)
}