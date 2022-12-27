fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val day = 1
    val dayString = String.format("%02d", day)


    //
    // -----------------------------
    //
    val testInput = readInput("Day${dayString}_test")
//    checkEquals(part1(testInput), TODO())
//    checkEquals(part2(testInput), TODO())

    val input = readInput("Day$dayString")
    part1(input).println()
    part2(input).println()
}
