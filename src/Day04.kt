fun main() {
    fun String.toRange(): Pair<IntRange, IntRange> =
        this.split(',')
            .map { it.takeWhile { c -> c.isDigit() }.toInt()..it.takeLastWhile { c -> c.isDigit() }.toInt() }
            .let { Pair(it[0], it[1]) }

    fun part1(input: List<String>): Int {
        return input.map { it.toRange() }
            .filter { (r1, r2) ->
                (r1.contains(r2.first) && r1.contains(r2.last)) ||
                        (r2.contains(r1.first) && r2.contains(r1.last))
            }
            .size
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toRange() }
            .filter { (r1, r2) -> (r1.first <= r2.last && r1.last >= r2.first) }
            .size
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
