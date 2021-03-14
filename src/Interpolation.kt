import java.util.stream.Stream

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

    override operator fun invoke(x: Double) = step(0, pairs.lastIndex, x)

    private fun step(i: Int, j: Int, x: Double): Double =
            if (i == j) pairs[i].second
            else ((x - pairs[i].first) * step(i+1, j, x) - step(i, j-1, x) * (x - pairs[j].first)) / (pairs[j].first - pairs[i].first)
}