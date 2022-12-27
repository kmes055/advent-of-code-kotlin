class Move(
    val type: String,
    val point: Int,
    val range: IntRange
) {
    private val isHorizon = type == "HORIZON"
    val points: List<Pair<Int, Int>>
        get() {
            return if (this.isHorizon) range.map { Pair(it, point) }
            else range.map { Pair(point, it) }
        }

    companion object {
        fun ofHorizon(horizon: IntRange, depth: Int) = Move("HORIZON", depth, horizon)
        fun ofVertical(vertical: IntRange, left: Int) = Move("VERTICAL", left, vertical)
    }

    override fun toString(): String {
        return "Move(type='$type', point=$point, range=$range)"
    }
}

enum class BoardContent {
    ROCK, SAND, AIR
    ;

    fun isBlocked() = (this == ROCK) or (this == SAND)
    fun isEmpty() = this == AIR

}

fun BoardContent?.isNullOrEmpty() = this == null || this == BoardContent.AIR

fun main() {
    fun parseLine(line: String) =
        line.split(" -> ")
            .map {
                it.split(",")
                    .map { point -> point.toInt() }
            }.map { points -> Pair(points[0], points[1]) }
            .windowed(2, 1)
            .map { it.first() moveTo it.last() }

    fun part1(input: List<String>): Int {
        val boards = mutableMapOf<Pair<Int, Int>, BoardContent>()
        val maxY = input.map { parseLine(it) }
            .flatMap { it.flatMap { move -> move.points } }
            .onEach { boards[it] = BoardContent.ROCK }
            .maxOf { it.second }

        var sandCount = 0
        val source = Pair(500, 0)

        fun findNextDropPoint(): Pair<Int, Int> {
            var current = source

            while (true) {
                if (current.second > maxY) return 0 to -1

                current = if (boards[current.down()].isNullOrEmpty()) {
                    current.down()
                } else if (boards[current.leftDiag()].isNullOrEmpty()) {
                    current.leftDiag()
                } else if (boards[current.rightDiag()].isNullOrEmpty()) {
                    current.rightDiag()
                } else return current
            }
        }

        while (true) {
            val nextPoint = findNextDropPoint()
            if (nextPoint.second == -1 || nextPoint == source) return sandCount
            sandCount += 1
            boards[nextPoint] = BoardContent.SAND
        }
    }

    fun part2(input: List<String>): Int {
        val boards = mutableMapOf<Pair<Int, Int>, BoardContent>()
        val maxY = input.map { parseLine(it) }
            .flatMap { it.flatMap { move -> move.points } }
            .onEach { boards[it] = BoardContent.ROCK }
            .maxOf { it.second }
            .let { it + 2 }

        var sandCount = 0
        val source = Pair(500, 0)

        fun findNextDropPoint(): Pair<Int, Int> {
            var current = source

            while (true) {
                if (current.second + 1 ==  maxY) return current

                current = if (boards[current.down()].isNullOrEmpty()) {
                    current.down()
                } else if (boards[current.leftDiag()].isNullOrEmpty()) {
                    current.leftDiag()
                } else if (boards[current.rightDiag()].isNullOrEmpty()) {
                    current.rightDiag()
                } else return current
            }
        }

        while (true) {
            val nextPoint = findNextDropPoint()
            if (nextPoint == source) break
            sandCount += 1
            boards[nextPoint] = BoardContent.SAND
        }

        return sandCount + 1
    }

    val day = 14
    val dayString = String.format("%02d", day)

    val testInput = readInput("Day${dayString}_test")
    checkEquals(part1(testInput), 24)
    checkEquals(part2(testInput), 93)

    val input = readInput("Day$dayString")
    part1(input).println()
    part2(input).println()
}

infix fun Int.rangeTo(to: Int) = if (this > to) (to..this) else (this..to)

infix fun Pair<Int, Int>.moveTo(to: Pair<Int, Int>) =
    if (first == to.first) Move.ofVertical(this.second rangeTo to.second, first)
    else if (second == to.second) Move.ofHorizon(this.first rangeTo to.first, second)
    else throw IllegalArgumentException("Should not reach here: $this, $to")

fun Pair<Int, Int>.down() = Pair(first, second + 1)
fun Pair<Int, Int>.leftDiag() = Pair(first - 1, second + 1)
fun Pair<Int, Int>.rightDiag() = Pair(first + 1, second + 1)
operator fun Map<Pair<Int, Int>, BoardContent>.get(key: Pair<Int, Int>): BoardContent = get(key) ?: BoardContent.AIR