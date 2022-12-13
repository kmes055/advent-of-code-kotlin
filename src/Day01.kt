fun main() {
    fun List<String>.sumSplit(predicate: (String) -> Boolean): List<Int> {
        if (this.isEmpty()) return listOf()

        val left = this.takeWhile(predicate).sumOf { it.toInt() }
        val right = this.dropWhile(predicate).drop(1)
        return listOf(left) + right.sumSplit(predicate)
    }

    fun part1(input: List<String>): Int {
        return input.sumSplit { it.isNotBlank() }
            .sortedDescending()[0]
    }

    fun part2(input: List<String>): Int {
        return input.sumSplit { it.isNotBlank() }
            .sortedDescending()
            .subList(0, 3)
            .sum()
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
