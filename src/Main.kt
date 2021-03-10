import kotlin.math.sin

fun main() {
    lab5()
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


fun getMatrixDef(): RectangleMatrix {
    return getRandomMatrix(6,5, -10.0, 10.0)
}