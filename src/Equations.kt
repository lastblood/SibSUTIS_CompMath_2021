import java.lang.IllegalArgumentException
import java.util.function.Function
import kotlin.math.abs
import kotlin.math.sign

fun performBisection(f: Function<Double, Double>, left: Double = -Double.MAX_VALUE, right: Double = Double.MAX_VALUE,
                     eps: Double = 1e-3, lSign: Double = f.apply(left).sign, rSign: Double = f.apply(right).sign): Double {
    if(lSign == rSign) throw IllegalArgumentException()
    val mid = left/2 + right/2
    val mSign = f.apply(mid).sign
    println("$left $mid $right ${right-left}")

    return when {
        right-left < eps -> mid
        mSign == lSign -> performBisection(f, mid, right, eps, mSign, rSign)
        mSign == rSign -> performBisection(f, left, mid, eps, lSign, mSign)
        else -> mid
    }
}

fun performChords(f: Function<Double, Double>, x0: Double = -10.0, x: Double = 10.0, eps: Double = 1e-3): Double {
    val next = x - f.apply(x)*(x-x0) / (f.apply(x)-f.apply(x0))
    println("$x0 $x $next")
    return if(abs(next - x) < eps) next else performChords(f, x0, next, eps)
}