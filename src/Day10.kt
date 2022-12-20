fun main() {
    fun parseSignals(input: List<String>): Map<Int, Int> {
        val registers = mutableMapOf<Int, Int>()

        var cycle = 0
        var register = 1
        input.forEach { line ->
            cycle += 1
            if (line == "noop") {
                registers[cycle] = register
                return@forEach
            }

            val amount = line.split(' ')[1].toInt()
            registers[cycle] = register
            cycle += 1
            registers[cycle] = register
            register += amount
        }

        return registers
    }

    fun part1(input: List<String>): Int {
        val registers = parseSignals(input)

        return (20..220 step 40).sumOf { c ->
            val value = registers[c] ?: 0
            value * c
        }
    }

    fun part2(input: List<String>): Int {
        val registers = parseSignals(input)

        (1 .. 240).map {
            val pos = ((it - 1) % 40) + 1
            val register = registers[it] ?: 0
            val c = if (pos in (register .. register + 2)) '#' else '.'
            println("cur: $it, $pos, $register: $c")
            c
        }.windowed(40, 40)
            .forEach { it.joinToString("").println() }
        return input.size
    }

    val testInput = readInput("Day10_test")
    part2(testInput)
    checkEquals(part1(testInput), 13140)

    val input = readInput("Day10")
    part1(input).println()
    checkEquals(part1(input), 14060)
    part2(input).println()
}
