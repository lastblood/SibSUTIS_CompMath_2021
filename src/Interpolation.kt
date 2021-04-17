import java.util.stream.Stream
import kotlin.collections.HashMap
import kotlin.math.*

abstract class Interpolation(pairsList: List<Pair<Double, Double>>) {
    val pairs = pairsList.toMutableList()

    abstract operator fun invoke(x: Double): Double

    operator fun plus(dot: Pair<Double, Double>) = pairs.add(dot)
}

class Lagrange(pairs: List<Pair<Double, Double>>): Interpolation(pairs) {
    override operator fun invoke(x: Double): Double =
            pairs.mapIndexed { i, (xi,yi) -> yi * pairs.mapIndexed { j, (xj,_) -> if(j==i) 1.0 else (x-xj)/(xi-xj) }.reduce{ a, b -> a*b } }.sum()
}

class Aitken(pairs: List<Pair<Double, Double>>): Interpolation(pairs) {
    override operator fun invoke(x: Double) =
            step(0, pairs.lastIndex, x, pairs.mapIndexed { i, pair -> (i to i) to pair.second }.toMap(HashMap()))

    private fun step(i: Int, j: Int, x: Double, m: HashMap<Pair<Int, Int>, Double>): Double = m.computeIfNeeded(i to j) {
        ((x - pairs[i].first)*step(i+1, j, x, m) - step(i, j-1, x, m)*(x - pairs[j].first)) / (pairs[j].first - pairs[i].first)
    }
}

class Newton(pairs: List<Pair<Double, Double>>): Interpolation(pairs) {
    private val m = HashMap<Pair<Int, Int>, Double>(pairs.mapIndexed { i, pair -> (i to i) to pair.second }.toMap())

    private fun diff(a: Int, b: Int): Double = m.computeIfNeeded(a to b) {
        (diff(a+1, b) - diff(a, b-1)) / (pairs[b].first - pairs[a].first)
    }

    override operator fun invoke(x: Double): Double =
            Stream.iterate(1.0 to 0) { (v,i) -> v * (x-pairs[i].first) to i+1 }
                    .mapToDouble { (v,i) -> v * diff(0,i) }.limit(pairs.size.toLong()).sum()
}

class CubicSpline(pairs: List<Pair<Double, Double>>, d1: Double = 0.0, d2: Double = 0.0, isSecond: Boolean = true): Interpolation(pairs) {

    private val n = pairs.size-1
    private val h = pairs.zipWithNext { a, b -> b.x - a.x }.toDoubleArray()
    private val m: DoubleArray

    val t = pairs.map { it.x }
    val p = pairs.map { it.y }
    private fun dp(i: Int) = (p[i+1]-p[i])/h[i] - (p[i]-p[i-1])/h[i-1]

    init {
        val midTemp = (0 .. n-2).map { 2 * (h[it] + h[it+1]) }
        val ansTemp = (1 .. n-1).map { 6 * dp(it) }

        if(isSecond) {
            val low = h.clone().also { it[it.lastIndex] = 0.0 }
            val mid = listOf(1.0) + midTemp + listOf(1.0)
            val high = h.clone().also { it[0] = 0.0 }
            val ans = listOf(d1) + ansTemp + listOf(d2)
            m = thomasArray(low, mid.toDoubleArray(), high, ans.toDoubleArray())
        } else {
            val mid = listOf(2 * h[0]) + midTemp + listOf(2 * h[n-1])
            val ans = listOf(6 * (d(0) - d1)) + ansTemp + listOf(6 * (d2 - d(n-1)))
            m = thomasArray(h, mid.toDoubleArray(), h, ans.toDoubleArray())
        }
    }

    private fun d(a: Int): Double = (pairs[a+1].y - pairs[a].y) / (pairs[a+1].x - pairs[a].x)

    override fun invoke(x: Double): Double {
        var i = pairs.withIndex().filter { (_, pair) -> pair.x < x }.maxOfOrNull { (i,_) -> i } ?: 0

        return (m[i] * ((t[i+1] - x).pow(3.0)) / 6 +
                m[i+1] * ((x - t[i]).pow(3.0)) / 6 +
                (p[i] - m[i] * h[i]*h[i] / 6) * (t[i+1] - x) +
                (p[i+1] - m[i+1] * h[i]*h[i] / 6) * (x - t[i])) / h[i]
    }

}

fun testInterpolation(func: (Double) -> Double, interpolatedFunc: (Double) -> Double, from: Double, to: Double, count: Int): Double {
    val step = (to-from) / count
    val s = (0 .. count).map { from + it*step }
//            .onEach { println("$it\t${interpolatedFunc(it)}")  }
            .map { abs(func(it) - interpolatedFunc(it)) }
    println(s.average())
    println(s.max()!!)
    return s.average()
}