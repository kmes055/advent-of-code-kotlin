import kotlin.math.max

fun main() {
    fun List<Int>.determine(): List<Boolean> {
        var curMax = -1
        val asc = this.map { (it > curMax).apply { curMax = max(it, curMax) } }
        curMax = -1
        val desc = this.reversed().map { (it > curMax).apply { curMax = max(it, curMax) } }.reversed()

        return (asc zip desc).map { (a, b) -> a or b }
    }

    fun part1(input: List<String>): Int {
        return input.map { it.map { c -> c.digitToInt() } }
            .let { matrix ->
                val horizonal = matrix.map { it.determine() }
                val vertical = matrix.transposed().map { it.determine() }.transposed()
                (horizonal zip vertical).map {
                    (la, lb) -> (la zip lb).map { (a, b) -> a or b }
                }
            }.sumOf { it.count { b -> b } }

    }

    fun List<Int>.countTrees(value: Int): Int {
        return if (this.isEmpty()) 0
        else this.takeWhile { it < value }.size.let { if (it == this.size) it else it + 1 }

    }

    fun calcDistance(line: List<Int>, position: Int): Int =
        (line.take(position).reversed().countTrees(line[position])) * (line.drop(position + 1).countTrees(line[position]))

    fun part2(input: List<String>): Int {
        val matrix = input.map { it.map { c ->c.digitToInt() } }
        val transposed = matrix.transposed()

        return (matrix.indices).maxOf { i ->
            (matrix[0].indices).maxOf { j ->
                calcDistance(matrix[i], j) * calcDistance(transposed[j], i)
            }
        }
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
