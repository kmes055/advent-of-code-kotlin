fun main() {
    fun calc(line: String, len: Int): Int {
        fun String.isUnique(): Boolean = this.toSet().size == this.length

        var i = 0
        var j = 1
        while (true) {
            if (i > j) {
                j = i
                continue
            }
            j += 1
            if (i + len == j) return j
            else if (line.substring(i..j).isUnique()) continue
            else i = line.indexOf(line[j], i) + 1
        }
    }

    fun part1(input: List<String>): Int {
        return calc(input[0], 4)
    }

    fun part2(input: List<String>): Int {
        return calc(input[0], 14)
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}