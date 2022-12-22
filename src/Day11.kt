fun main() {

    class Monkey(
        val holding: MutableList<Long>,
        val operation: (Long) -> Long,
        val test: Long,
        val target: Pair<Long, Long>,
        var throwCount: Long = 0
    ) {
        fun inspect(): List<Pair<Long, Long>> = holding
            .map(operation)
            .map { it / 3 }
            .map {
                it.takeIf { it % test == 0L }
                    ?.let { Pair(target.first, it) }
                    ?: Pair(target.second, it)
            }.also {
                throwCount += it.size
                holding.clear()
            }

        fun inspect(lcm: Long, items: List<Long>): List<Pair<Long, Long>> = items
            .map(operation)
            .map { it.mod(lcm) }
            .map {
                it.takeIf { it.mod(test) == 0L }
                    ?.let { Pair(target.first, it) }
                    ?: Pair(target.second, it)
            }.also {
                throwCount += it.size
                holding.clear()
            }

        fun take(item: Long) {
            holding.add(item)
        }

        override fun toString(): String {
            return "Monkey(holding=$holding, test=$test, target=$target, throwCount=$throwCount)"
        }
    }

    fun parseOperation(formula: String): (Long) -> Long {
        val tokens = formula.split(" ")
        val operator = tokens[1]
        val operand = if (tokens[2] == "old") 0 else tokens[2].toLong()
        return if (operand == 0L) when (operator) {
            "+" -> { i -> i + i }
            "*" -> { i -> i * i }
            else -> { i -> i }
        } else when (operator) {
            "+" -> { i -> i + operand }
            "-" -> { i -> i - operand }
            "*" -> { i -> i * operand }
            "/" -> { i -> i / operand }
            else -> { i -> i }
        }
    }

    fun parseMonkey(lines: List<String>): Monkey {
        val items = lines[0].split(": ")[1].split(", ").map { it.toLong() }
        val operation = parseOperation(lines[1].split("= ")[1])
        val test = lines[2].split(" ")
            .last()
            .toLong()
        val targets = lines[3].split(" ").last().toLong() to lines[4].split(" ").last().toLong()

        return Monkey(items.toMutableList(), operation, test, targets)
    }

    fun parseMonkeys(input: List<String>): Map<Long, Monkey> {
        val monkeys = mutableMapOf<Long, Monkey>()
        input.forEachIndexed { index, line ->
            if (line.startsWith("Monkey")) {
                val id = line.split(" ")[1][0].digitToInt().toLong()
                monkeys[id] = parseMonkey(input.drop(index + 1).takeWhile { it.isNotBlank() })
            }
        }

        return monkeys.toMap()
    }

    fun part1(input: List<String>): Long {
        val monkeys: Map<Long, Monkey> = parseMonkeys(input)

        repeat(20) {
            monkeys.forEach { (_, v) ->
                val thrown = v.inspect()
                thrown.forEach { (i, value) -> monkeys[i]?.take(value) }
            }
        }
        return monkeys.map { it.value.throwCount }.sortedDescending().let { it[0] * it[1] }
    }

    fun part2(input: List<String>): Long {
        val monkeys: Map<Long, Monkey> = parseMonkeys(input)
        val lcm = monkeys.values.map { it.test }
            .reduce { acc, i -> lcm(acc, i) }
        val equalRound = mutableListOf<Pair<Int, Long>>()

        repeat(10000) { round ->
            monkeys.values.forEach {
                val items = it.holding.toList()
                it.inspect(lcm, items)
                    .forEach { (i, value) ->
                        monkeys[i]?.take(value)
                    }
            }
            if (monkeys[1]!!.throwCount == monkeys[2]!!.throwCount && round > 70) {
                equalRound.add((round + 1) to (monkeys[1]?.throwCount ?: 0L))
            }
        }
        return monkeys.map { it.value.throwCount }.sortedDescending().let { it[0] * it[1] }
    }

    val testInput = readInput("Day11_test")
    checkEquals(part1(testInput), 10605L)
    checkEquals(part2(testInput), 2_713_310_158L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
