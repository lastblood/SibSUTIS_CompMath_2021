import java.util.stream.Stream
import kotlin.streams.toList


fun euler(funcXY: (Double, Double) -> Double, startXY: Pair<Double, Double>, step: Double, count: Int, iters: Int = 1): List<Pair<Double, Double>> =
        Stream.iterate(startXY) { (x,y) -> x+step to eulerCore(funcXY, x, y, step, iters) }.limit(count+1L).toList()

private fun eulerCore(funcXY: (Double, Double) -> Double, x: Double, y: Double, step: Double, iter: Int = 1, cache: Double = funcXY(x,y)): Double =
        y + step/2 * (cache + if(iter<1) cache else funcXY(x+step, eulerCore(funcXY, x, y, step, iter-1, cache)))

fun ralston(funcXY: (Double, Double) -> Double, startXY: Pair<Double, Double>, step: Double, count: Int): List<Pair<Double, Double>> =
        Stream.iterate(startXY) {
            (x,y) -> x+step to y + step*funcXY(x + step/2, y + step/2 * funcXY(x,y))
        }.limit(count+1L).toList()

fun rungeKutta4(funcXY: (Double, Double) -> Double, startXY: Pair<Double, Double>, step: Double, count: Int): List<Pair<Double, Double>> =
    Stream.iterate(startXY) { (x,y) ->
        val k1 = funcXY(x,y)
        val k2 = funcXY(x+step/2, y+step/2*k1)
        val k3 = funcXY(x+step/2, y+step/2*k2)
        val k4 = funcXY(x+step, y+step*k3)
        x+step to y + step/6 * (k1 + 2*k2 + 2*k3 + k4)
    }.limit(count+1L).toList()
