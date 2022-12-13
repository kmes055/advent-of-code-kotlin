fun main() {
    fun calcPriority(c: Char) = when {
        c.isUpperCase() -> c - 'A' + 27
        c.isLowerCase() -> c - 'a' + 1
        else -> 0
    }


    fun part1(input: List<String>): Int {
        return input.sumOf {
            val size = it.length / 2
            it.take(size)
                .toSet()
                .let { leftDict -> it.substring(size).first { c -> c in leftDict } }
                .let { c -> calcPriority(c) }
        }
    }

    fun part2(input: List<String>): Int {
        return input.windowed(3, 3)
            .sumOf{ lines ->
                lines[0].filter { lines[1].toSet().contains(it) }
                    .first { lines[2].toSet().contains(it) }
                    .let { c -> calcPriority(c) }
            }
    }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
