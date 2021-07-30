import java.util.Scanner

fun main() {
    val scan = Scanner(System.`in`)
    var maxNum = Int.MIN_VALUE
    var maxNumIndex = 1
    var currIndex = 1
    while (scan.hasNextInt()) {
        val currNum = scan.nextInt()
        if (maxNum < currNum) {
            maxNum = currNum
            maxNumIndex = currIndex
        }
        ++currIndex
    }
    println("$maxNum $maxNumIndex")
}
