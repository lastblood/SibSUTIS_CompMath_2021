import org.jfree.chart.ChartPanel
import java.awt.Color
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import kotlin.math.*

fun main() {
    lab12()
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

fun lab3() {
    val matrix = onlyDiagonals(getMatrixDef())
    println(matrix)

    println(gauss(matrix.clone()).contentToString())
    println(gauss(matrix.clone()).contentToString())
    println(thomas(matrix).contentToString())
}

fun getMatrixDef(): RectangleMatrix {
    return getRandomMatrix(6,5, -10.0, 10.0)
}

// Нелинейные уравнения: бисекция, хорды, Ньютон
fun lab4() {
    val f = { x: Double -> x*x*x/50 - 8*x - 81 }
    val dfdx = { x: Double -> 3*x*x/50 - 8 }
    bisection(f, -100.0, 100.0).apply { println(this) }
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

val il = listOf(1.0 to 2.0, 2.0 to 4.0, 3.0 to 6.0, 5.0 to 10.0)

fun lab6() {
    val f = Lagrange(il)
    testInterpolation({ 2 * it}, {f(it)}, 0.0, 20.0, 100)
}

fun lab7() {
    val f = Aitken(il)
    testInterpolation({ 2 * it}, {f(it)}, 0.0, 20.0, 100)
}

fun lab8() {
    val f = Newton(il)
    testInterpolation({ 2 * it}, {f(it)}, 0.0, 20.0, 100)
}

fun interpolation() {
    val lower = -5.0
    val upper = 5.0
    val testDotsCount = 100
    val func: (Double) -> Double = { sin(it) }
    val interpolateDotsCount = 5
    val list = listOf(lower to func(lower)) + generateListAt(func, lower to upper, interpolateDotsCount) + listOf(upper to func(upper))

    val original: (Double) -> Double = func

    println("lagrange")
    testInterpolation(original, {Lagrange(list)(it)}, lower, upper, testDotsCount)
    println("aitken")
    testInterpolation(original, {Aitken(list)(it)}, lower, upper, testDotsCount)
    println("newton")
    testInterpolation(original, {Newton(list)(it)}, lower, upper, testDotsCount)
    println("cubic")
    testInterpolation(original, {CubicSpline(list)(it)}, lower, upper, testDotsCount)
}

val f1 = { x: Double -> ln(x+1) / x }
val f2 = { x: Double -> x*x*x / exp(x) * sin(x + 2) }
val f3 = { x: Double -> x*x*x / exp(x) * atan(x-2) * sin(x+1) }

fun lab10() {
    integrate(f1, 0.1, 1.0, 1e-7)
    println(state)
    integrateTrapezoid(f1, 0.1, 1.0, 1e-7)
    println(stateTrapezoid)
    println("(${integrateDaR(f1, 0.1, 1.0, 1e-7)}, $count1)")
    println("(${integrateTrapezoidDaR(f1, 0.1, 1.0, 1e-7)}, $count2)")

    count1 = 0; count2 = 0
    println("")

    integrate(f3, -1.0, 12.0, 1e-5)
    println(state)
    integrateTrapezoid(f3, -1.0, 12.0, 1e-5)
    println(stateTrapezoid)
    println("(${integrateDaR(f3, -1.0, 12.0, 1e-5)}, $count1)")
    println("(${integrateTrapezoidDaR(f3, -1.0, 12.0, 1e-5)}, $count2)")
}

fun lab11() {
    val function: (Double, Double) -> Double = { x, y -> x * x - 2 * y }
    val idealResult: (Double) -> Double = { 1/4*(2*it*it-2*it+3*exp(-2*it)+1) }

    val startXY = 0.0 to 1.0
    val step = 0.1
    val count = 10

    val l = listOf(
        euler(function, startXY, step, count, 0),
        euler(function, startXY, step, count, 2),
        euler(function, startXY, step, count, 4),
        euler(function, startXY, step, count, 6),
        ralston(function, startXY, step, count),
        rungeKutta4(function, startXY, step, count)
    )

    l[0].indices.forEach { ind ->
        l.map { String.format(java.util.Locale.ROOT, "%.7f\t", it[ind].second) }.forEach { print(it) }
        println()
    }
}

fun lab12() {
    val sinUp = { x: Double -> 0.6 * x + 0.5 * sin(2 * x) }
    var currentPower = 3
    val range = -10.0..10.0 step 0.1

    val dotsCount = 50
    var dots = generateListAt(sinUp, -10.0 to 10.0, dotsCount)
    var approximationFunction = OLS(dots, currentPower)
    var approximatedDots = range.map { it to approximationFunction(it) }
    val fullDots = range.map { it to sinUp(it) }

    val plotter = Plotter()

    fun initPlotter() {
        plotter.removeAll()
                .addDots(dots)
                .addCustom(fullDots) { renderer, index ->
                    renderer.setSeriesShapesVisible(index, false)
                    renderer.setSeriesPaint(index, Color.GREEN)
                }
                .addLine(approximatedDots)
    }

    initPlotter()

    var chartPanel = ChartPanel(plotter.getChart())
    val frame = PlotterFrame(currentPower.toString(), chartPanel)

    frame.addKeyListener(object : KeyAdapter() {
        override fun keyReleased(e: KeyEvent?) {
            synchronized(frame) {
                when(e!!.keyChar) {
                    '-' -> currentPower = max(1, currentPower-1)
                    '=' -> currentPower++
                    ' ' -> { dots = generateListAt(sinUp, -10.0 to 10.0, dotsCount) }
                    else -> return@keyReleased
                }

                approximationFunction = OLS(dots, currentPower)
                approximatedDots = range.map { it to approximationFunction(it) }

                initPlotter()

                chartPanel = ChartPanel(plotter.getChart())
                frame.title = currentPower.toString()
                frame.rerender(chartPanel)
            }
        }
    })

    frame.isVisible = true
}