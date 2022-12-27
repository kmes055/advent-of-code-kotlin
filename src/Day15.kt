import kotlin.math.abs
import kotlin.math.max

class Sensor(
    val location: Pair<Int, Int>,
    val beacon: Pair<Int, Int>
) {
    val dist = location between beacon

    override fun toString(): String {
        return "location=$location, beacon=$beacon, dist=$dist"
    }
}

fun main() {
    fun parseLine(line: String): Sensor {
        val sensor = line.split(':')[0].let {
            val x = it.dropWhile { it != '=' }
                .dropLastWhile { it != ',' }
                .drop(1)
                .dropLast(1)
                .toInt()
            val y = it.takeLastWhile { it != '=' }
                .toInt()
            Pair(x, y)
        }
        val beacon = line.split(':')[1].let {
            val x = it.dropWhile { it != '=' }
                .dropLastWhile { it != ',' }
                .drop(1)
                .dropLast(1)
                .toInt()
            val y = it.takeLastWhile { it != '=' }
                .toInt()
            Pair(x, y)
        }

        return Sensor(sensor, beacon)
    }


    fun part1(input: List<String>, y: Int): Int {
        val positions = mutableSetOf<Int>()
        val sensors = input.map { parseLine(it) }

        sensors.map { getXRange(it, y) }
            .forEach { it.forEach { x -> positions.add(x) } }

        sensors.flatMap { listOf(it.location, it.beacon) }
            .filter { it.second == y }
            .toSet()
            .map { it.first }
            .forEach { positions.remove(it) }
        return positions.size
    }

    fun findPosition(sensors: List<Sensor>, edge: Int): Pair<Int, Int> {
        var x = 0
        var y = 0
        do {
            if (y > edge) throw IllegalStateException("Should not reach here")
            var step: Int? = null
            sensors.forEach { sensor ->
                if (Pair(x, y) between sensor.location <= sensor.dist) {
                    step = max(step ?: -1, getXRange(sensor, y).last + 1)
                }
            }
            if (step == null) {
                return Pair(x, y)
            }

            x = step!!
            if (x > edge) {
                y += 1
                x = 0
            }
        } while (true)
    }

    fun part2(input: List<String>, edge: Int): Long {
        val sensors = input.map { parseLine(it) }
        val searched: Pair<Int, Int> = findPosition(sensors, edge)
            .also { println("Searched: $it") }

        return searched.frequency
    }

    val day = 15
    val dayString = String.format("%02d", day)

    //
    // -----------------------------
    //
    val testInput = readInput("Day${dayString}_test")
    checkEquals(part1(testInput, 10), 26)
    checkEquals(part2(testInput, 20), 56000011L)

    val input = readInput("Day$dayString")
    part1(input, 2_000_000).println()
    part2(input, 4_000_000).println()
}

infix fun Pair<Int, Int>.between(to: Pair<Int, Int>) = abs(first - to.first) + abs(second - to.second)
fun getXRange(sensor: Sensor, y: Int): IntRange {
    val x = sensor.location.first
    val xDist = sensor.dist - (sensor.location between Pair(x, y))
    return (x - xDist)..(x + xDist)
}

val Pair<Int, Int>.frequency: Long
    get() = this.first * 4_000_000L + this.second