import kotlin.math.pow

class OLS(pairs: List<Pair<Double, Double>>, pwr: Int) {
    val ans: DoubleArray

    init {
        val sumX = (0 .. 2*pwr+1).map { p -> pairs.sumByDouble { (x,_) -> x.pow(p) } }
        val sumY = (0 .. pwr+1).map { p -> pairs.sumByDouble { (x,y) -> y*x.pow(p) } }
        ans = gauss(fullMatrix(RectangleMatrix(pwr+1, pwr+1) { y, x -> sumX[y + x] }, sumY.toDoubleArray()))
    }

    operator fun invoke(x: Double): Double = ans.mapIndexed { i, el -> el * x.pow(i) }.sum()
}
