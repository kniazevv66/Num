package converter

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.system.exitProcess

fun main() {
    var command = ""
    var baseSrc = -1
    var baseTar = -1
    do {
        /*
         * In the next condition -1 means not assigned base,
         * which occurs only once when program starts
         */
        if (command == "/back" || baseSrc == -1) {
            println("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
            command = readLine()!!
            if (command != "/exit") {
                val tmp = command.split(' ')
                baseSrc = tmp[0].toInt()
                baseTar = tmp[1].toInt()
            } else exitProcess(0)
        }
        println("Enter number in base $baseSrc to convert to base $baseTar (To go back type /back) ")
        command = readLine()!!
        if (command != "/back") println("Conversion result: ${toTargetBase(command, baseSrc, baseTar)}")
    } while (true)
}
fun toDecimal(numSrc: String, baseSrc: Int): String {
    var integerPart = BigInteger.ZERO
    val integer = numSrc.substringBefore('.').lowercase().reversed()
    for (i in integer.indices) {
        integerPart += baseSrc.toBigInteger().pow(i) * if (integer[i] >= 'a') BigInteger.TEN + (integer[i] - 'a').toBigInteger() else integer[i].toString().toBigInteger()
    }
    var decimalPart = BigDecimal.ZERO.setScale(5)
    if ('.' in numSrc) {
        val decimal = numSrc.substringAfter('.').lowercase()
        for (i in decimal.indices) {
            decimalPart += BigDecimal.ONE.setScale(5) / baseSrc.toBigDecimal()
                .pow(i + 1) * if (decimal[i] >= 'a') BigDecimal.TEN + (decimal[i] - 'a').toBigDecimal() else decimal[i].toString()
                .toBigDecimal()
        }
    }
    return integerPart.toString() + if ('.' in numSrc) ".${decimalPart.toString().substringAfter('.')}" else ""
}
fun toTargetBase(numSrc: String, baseSrc: Int, baseTar: Int): String {
    var res = ""
    val num = toDecimal(numSrc, baseSrc)
    /**
     * The next line is totally optional.
     * Needed only for simplicity for the next calculations in order to
     * not call .toBigInteger() every time
     */
    val baseTarBig = baseTar.toBigInteger()
    var integer = num.substringBefore('.').toBigInteger()
    if (integer == BigInteger.ZERO) {
        res = "0"
    }
    var remainder: Int
    while (integer > BigInteger.ZERO) {
        remainder = (integer - integer / baseTarBig * baseTarBig).toInt()
        res += if (remainder >= 10) 'a' + (remainder - 10) else remainder
        integer /= baseTarBig
    }
    res = res.reversed()
    if ('.' in num) {
        /**
         * The next line is totally optional.
         * Needed only for simplicity for the next calculations in order to
         * not call .toBigDecimal() every time
         */
        val baseTarBigDec = baseTar.toBigDecimal()
        var decimal = ("0." + num.substringAfter('.')).toBigDecimal()
        res += '.'
        repeat(decimal.scale()) {
            decimal *= baseTarBigDec
            remainder = decimal.toString().substringBefore('.').toInt() // Taking integer part
            res += if (remainder >= 10) 'a' + (remainder - 10) else remainder
            decimal -= remainder.toBigDecimal()
        }
    }
    return res
}