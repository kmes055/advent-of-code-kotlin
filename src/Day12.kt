const val BIG = 100_000_000_000

fun main() {

    fun parsed(lines: List<String>): List<List<Int>> =
        lines.map { line ->
            line.map {
                when (it) {
                    'S' -> -1
                    'E' -> 26
                    else -> it - 'a'
                }
            }
        }

    fun List<List<Long>>.printMap() {
        this.forEach { line ->
            line.map { if (it == BIG) 0 else it }
                .joinToString(",")
                .println()
        }
        print('\n')
    }

    infix fun Pair<Int, Int>.inside(maxIndices: Pair<Int, Int>): Boolean =
        (first in (0 until maxIndices.first)) and (second in (0 until maxIndices.second))

    fun <T> MutableList<MutableList<T>>.replace(point: Pair<Int, Int>, item: T) {
        this[point.first].removeAt(point.second)
        this[point.first].add(point.second, item)
    }

    fun MutableList<MutableList<Long>>.dfs(heightBoard: List<List<Int>>, point: Pair<Int, Int>, dist: Long, endValue: Int = -1) {
//        println("cur: $point, dist: $dist, here: ${this[point]}")
//        this.printMap()
        val here = this[point]
        if (dist < here) {
            replace(point, dist)
        } else return
        if (heightBoard[point] == endValue) return

        listOf(
            Pair(point.first - 1, point.second),
            Pair(point.first + 1, point.second),
            Pair(point.first, point.second - 1),
            Pair(point.first, point.second + 1),
        ).filter { it inside (this.size to this[0].size) }
            .filter { heightBoard[it] + 1 >= heightBoard[point] }
            .filter { dist + 1 < this[it] }
            .forEach { dfs(heightBoard, it, dist + 1, endValue) }
    }

    fun List<List<Number>>.findPoint(number: Number): Pair<Int, Int> {
        val i = indexOfFirst { line -> line.contains(number) }
        val j = this[i].indexOf(number)
        return Pair(i, j)
    }

    fun getDists(board: List<List<Int>>, endValue: Int = -1): List<List<Long>> {
        val end = board.findPoint(26)
        val dists = (board.indices).map {
            (board[0].indices).map { BIG }.toMutableList()
        }.toMutableList()

        dists.dfs(board, end, 0, endValue)
        return dists
    }

    fun part1(input: List<String>): Int {
        val board = parsed(input)
        val start = board.findPoint(-1)
        val dists = getDists(board)

        return dists[start].toInt()
    }

    fun part2(input: List<String>): Int {
        val board = parsed(input)
        val dists = getDists(board, 0)
        var minDist = BIG
        dists.indices.forEach { i ->
            dists[0].indices.forEach { j ->
                run {
                    Pair(i, j).let {
                        if (board[it] == 0 && dists[it] < minDist) {
                            minDist = dists[it]
                        }
                    }
                }
            }
        }

        return minDist.toInt()
    }

    val testInput = readInput("Day12_test")
    checkEquals(part1(testInput), 31)
    checkEquals(part2(testInput), 29)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
