import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Locale

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun String.isNumeric() = this.filter { it.isDigit() }.length == this.length

fun <T> List<List<T>>.transposed(): List<List<T>> = List(this.first().size) { i -> this.mapNotNull { it.getOrNull(i) } }

fun checkEquals(actual: Any, expected: Any) {
    if (actual != expected) throw IllegalStateException("Check failed. expedted: $expected, actual: $actual")
}
fun gcd(a: Long, b: Long): Long = if (a.mod(b) == 0L) b else gcd(b, a.mod(b))

fun lcm(a: Long, b: Long): Long = a * (b / gcd(a, b))

operator fun <E> List<List<E>>.get(point: Pair<Int, Int>): E = this[point.first][point.second]
