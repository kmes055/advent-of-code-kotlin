class Signal private constructor(type: String, private val number: Int?, val list: List<Signal>?): Comparable<Signal> {
    private val isNumber = type == "NUMBER"
    private val isList = type == "LIST"

    fun box() = ofList(listOf(this))

    override fun toString(): String {
        return if (isList) list.toString() else number.toString()
    }

    override operator fun compareTo(other: Signal): Int {
        return when {
            isNumber and other.isNumber -> number?.compareTo(other.number ?:0) ?: throw IllegalStateException("Should not reach here")
            isNumber and other.isList -> this.box().compareLists(other)
            isList and other.isNumber -> compareLists(other.box())
            isList and other.isList -> compareLists(other)
            else -> 0
        }
    }

    private fun compareLists(sigB: Signal): Int {
        (list!! zip sigB.list!!).forEach {
            if (it.first < it.second) return -1
            if (it.first > it.second) return 1
        }
        return list.size.compareTo(sigB.list.size)
    }

    companion object {
        fun ofEmptyList() = ofList(listOf())
        fun ofNumber(number: Int) = Signal("NUMBER", number, null)
        fun ofList(list: List<Signal>) = Signal("LIST", null, list)
    }
}

fun main() {
    fun parsePureList(line: String, children: MutableList<Signal>): Signal {
        return line.split(',')
            .map {
                if (it == "parsed") children.removeLast()
                else Signal.ofNumber(it.toInt())
            }
            .let { Signal.ofList(it) }
    }

    fun parseLine(line: String): Signal {
        val signals = mutableListOf<Signal>()
        var current = line
        while (true) {
            val start = current.lastIndexOf('[')
            if (start == -1 && current != "parsed") return Signal.ofEmptyList()
            val end = current.drop(start).indexOf(']') + start

            if (start > end) throw IllegalStateException("Should not reach here: $start, $end, $line")
            val nextElement = if (start + 1 == end) Signal.ofEmptyList()
            else {
                val middle = current.substring(start + 1 until end)
                parsePureList(middle, signals)
            }
            signals.add(nextElement)
            current = current.take(start) + "parsed" + current.drop(end + 1)

            if (current == "parsed") return signals.first()
        }
    }

    fun parseLines(input: List<String>) = input.asSequence()
            .filter { it.isNotBlank() }
            .map { parseLine(it) }

    fun part1(input: List<String>): Int {
        return parseLines(input)
            .windowed(2, 2)
            .map { Pair(it[0], it[1]) }
            .mapIndexed { index, pair -> if (pair.first < pair.second) index + 1 else 0 }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return parseLines(listOf(input, listOf("[[2]]", "[[6]]")).flatten())
            .toList()
            .sorted()
            .map { it.toString() }
            .let { (it.indexOf("[[2]]") + 1) * (it.indexOf("[[6]]") + 1) }
    }

    val testInput = readInput("Day13_test")
    checkEquals(part1(testInput), 13)
    checkEquals(part2(testInput), 140)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
