import java.lang.IllegalArgumentException
import kotlin.math.abs
import kotlin.math.sign

fun bisection(f: (Double) -> Double, left: Double = -Double.MAX_VALUE, right: Double = Double.MAX_VALUE,
              eps: Double = 1e-3, lSign: Double = f(left).sign, rSign: Double = f(right).sign): Double {
    if(lSign == rSign) throw IllegalArgumentException()
    val mid = left/2 + right/2
    val mSign = f(mid).sign

    return when {
        right-left < eps -> mid
        mSign == lSign -> bisection(f, mid, right, eps, mSign, rSign)
        mSign == rSign -> bisection(f, left, mid, eps, lSign, mSign)
        else -> mid
    }
}

fun chords(f: (Double) -> Double, x0: Double = -10.0, x: Double = 10.0, eps: Double = 1e-3): Double {
    val next = x - f(x)*(x-x0) / (f(x)-f(x0))
    return if(abs(next - x) < eps) next else chords(f, x0, next, eps)
}

fun newton(f: (Double) -> Double, dfdx: (Double) -> Double, x0: Double, eps: Double = 1e-3): Double {
    val x = x0 - f(x0) / dfdx(x0)
    return if(abs(x-x0) < eps) x else newton(f, dfdx, x, eps)
}