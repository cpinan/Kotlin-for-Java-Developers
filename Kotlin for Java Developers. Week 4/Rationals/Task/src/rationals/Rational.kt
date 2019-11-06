package rationals

import java.lang.StringBuilder
import java.math.BigInteger

data class RationalRange(
        val left: Rational,
        val right: Rational
)

data class Rational(
        val n: BigInteger,
        val d: BigInteger
) {

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(n)
        if (d.toString() != "1") {
            builder.append("/")
            builder.append(d)
        }
        return builder.toString()
    }

    fun getValue(): Double {
        return n.toDouble() / d.toDouble()
    }
}

private fun getGCD(n: BigInteger, d: BigInteger): BigInteger {
    if (n.toString() != "0") {
        return n.gcd(d)
    }
    return BigInteger.ONE
}

private fun getValues(r1: Rational, r2: Rational): List<BigInteger> {
    val denominator = r1.d.multiply(r2.d)
    val numerator1 = r1.n.multiply(denominator.divide(r1.d))
    val numerator2 = r2.n.multiply(denominator.divide(r2.d))
    return listOf(denominator, numerator1, numerator2)
}

operator fun Rational.plus(rational: Rational): Rational {
    val l = getValues(this, rational)
    val numerator = l[1].plus(l[2])
    val gcd = getGCD(numerator, l[0])
    return Rational(numerator.divide(gcd), l[0].divide(gcd))
}

operator fun Rational.minus(rational: Rational): Rational {
    val l = getValues(this, rational)
    val numerator = l[1].minus(l[2])
    val gcd = getGCD(numerator, l[0])
    return Rational(numerator.divide(gcd), l[0].divide(gcd))
}

operator fun Rational.times(rational: Rational): Rational {
    val numerator = this.n.multiply(rational.n)
    val denominator = this.d.multiply(rational.d)
    val gcd = getGCD(numerator, denominator)
    return Rational(numerator.divide(gcd), denominator.divide(gcd))
}

operator fun Rational.div(rational: Rational): Rational {
    val numerator = this.n.multiply(rational.d)
    val denominator = this.d.multiply(rational.n)
    val gcd = getGCD(numerator, denominator)
    return Rational(numerator.divide(gcd), denominator.divide(gcd))
}

operator fun Rational.unaryMinus(): Rational {
    return Rational(this.n.negate(), this.d)
}

operator fun Rational.compareTo(rational: Rational): Int {
    return this.getValue().compareTo(rational.getValue())
}

operator fun RationalRange.contains(rational: Rational): Boolean {
    val leftValue = this.left.getValue()
    val rightValue = this.right.getValue()
    val currentValue = rational.getValue()
    return currentValue in leftValue..rightValue
}

operator fun Rational.rangeTo(rational: Rational): RationalRange {
    return RationalRange(this, rational)
}

fun String.toRational(): Rational {
    val split = this.split("/")
    var numerator = BigInteger(split[0])
    var denominator = BigInteger.ONE
    if (split.size > 1) {
        denominator = BigInteger(split[1])
        var hasMinus = false
        if (split[1][0] == '-') {
            hasMinus = true
            denominator = BigInteger(split[1].substring(1))
        }
        if (hasMinus) {
            numerator = if (split[0][0] == '-') {
                BigInteger(split[0].substring(1))
            } else {
                numerator.multiply(-BigInteger.ONE)
            }
        }
    }
    return numerator divBy denominator
}

infix fun Long.divBy(d: Long): Rational {
    return BigInteger(this.toString()).divBy(BigInteger(d.toString()))
}

infix fun Int.divBy(d: Int): Rational {
    return this.toLong().divBy(d.toLong())
}

infix fun BigInteger.divBy(big: BigInteger): Rational {
    if (big.toString() == "0") {
        throw IllegalArgumentException("Denominator must not be zero.")
    }
    val gcd = getGCD(this, big)
    return Rational(this.divide(gcd), big.divide(gcd))
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)
    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}