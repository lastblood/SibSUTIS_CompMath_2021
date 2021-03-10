fun lagrange(pairs: List<Pair<Double, Double>>): (Double) -> Double = fun(cx: Double): Double =
        pairs.mapIndexed { i, (xi,yi) -> yi * pairs.mapIndexed { j, (xj,_) -> if(j==i) 1.0 else (cx-xj)/(xi-xj) }.reduce{ a,b -> a*b } }.sum()


class Aitken(private var pairs: List<Pair<Double, Double>>) {

    operator fun invoke(x: Double) = step(0, pairs.lastIndex, x)

    private fun step(i: Int, j: Int, x: Double): Double =
            if (i == j) pairs[i].second
            else ((x - pairs[i].first) * step(i+1, j, x) - step(i, j-1, x) * (x - pairs[j].first)) / (pairs[j].first - pairs[i].first)

    fun add(pair: Pair<Double, Double>) {
        pairs = pairs + listOf(pair)
    }
}