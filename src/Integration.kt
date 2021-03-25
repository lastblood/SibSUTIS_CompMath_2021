import java.util.*
import kotlin.math.abs

const val STEP_COUNT_REC = 16
const val STEP_COUNT_DAR = 4

var state = (0.0 to 0)
fun integrate(func: (Double)->Double, from: Double, to: Double, epsilon: Double, stepCount: Int = STEP_COUNT_REC, last: Double = 0.0): Double {
    val delta = (to-from)/stepCount
    val sum = (0 until stepCount).sumByDouble { func(from + delta*it) * delta }
    state = sum to stepCount
//    println("$stepCount $sum")
    return if(abs(last-sum) < 3*epsilon) sum else integrate(func, from, to, epsilon, stepCount*2, sum)
}

var stateTrapezoid = (0.0 to 0)
fun integrateTrapezoid(func: (Double)->Double, from: Double, to: Double, epsilon: Double, stepCount: Int = STEP_COUNT_REC, last: Double = 0.0): Double {
    val delta = (to-from)/stepCount
    val sum = delta * ((1 until stepCount).sumByDouble { func(from + delta*it) } + func(from)/2 + func(to)/2)
    stateTrapezoid = sum to stepCount
//    println("$stepCount $sum")
    return if(abs(last-sum) < 3*epsilon) sum else integrateTrapezoid(func, from, to, epsilon, stepCount*2, sum)
}

var count1 = 0
fun integrateDaR(func: (Double)->Double, from: Double, to: Double, epsilon: Double, stepCount: Int = STEP_COUNT_DAR, last: Double = 0.0): Double {
    count1 += stepCount
    val delta = (to-from)/stepCount
    val ints = (0 until stepCount).map { func(from + delta*it) * delta }
    val sum = ints.sum()
    return if(abs(last-sum) < 3*epsilon) sum else
        ints.mapIndexed { i: Int, x: Double -> integrateDaR(func, from + i*delta, from + (i+1)*delta, epsilon, stepCount, x) }.sum()
}

var count2 = 0
fun integrateTrapezoidDaR(func: (Double)->Double, from: Double, to: Double, epsilon: Double, stepCount: Int = STEP_COUNT_DAR, last: Double = 0.0): Double {
    count2 += stepCount
    val delta = (to-from)/stepCount
    var firstValue = func(from)
    val ints = (1..stepCount).map {
        val secondValue = func(from + it*delta)
        val temp = (firstValue + secondValue) / 2 * delta
        firstValue = secondValue
        temp
    }
    val sum = ints.sum()
    return if(abs(last-sum) < 3*epsilon) sum else
        ints.mapIndexed { i: Int, x: Double -> integrateTrapezoidDaR(func, from + i*delta, from + (i+1)*delta, epsilon, stepCount, x) }.sum()
}

private val r = SplittableRandom()
fun integrateMonteCarlo(func: (Double)->Double, from: Double, to: Double, max: Double, count: Long): Double =
        r.doubles(count, from, to).filter { r.nextDouble(max) < func(it) }.count() * (to-from) * max / count