fun main() {
    fun move(cargos: MutableList<MutableList<Char>>, move: Int, from: Int, to: Int, reverse: Boolean) {
        val target = cargos[from].takeLast(move).let { if (reverse) it.reversed() else it }
        (1..move).forEach { _ -> cargos[from].removeLast() }
        cargos[to].addAll(target)
    }

    fun parseCargos(input: List<String>): MutableList<MutableList<Char>> =
        input.takeWhile { it.isNotBlank() }
            .reversed()
            .drop(1)
            .map { it.slice(1 until it.length step 4) }
            .let { matrix ->
                matrix.first().mapIndexed { i, _ -> matrix.mapNotNull { it.getOrNull(i) } }
                    .map { it.filter { c -> c.isUpperCase() } }
                    .map { it.toMutableList() }
                    .toMutableList()
            }

    fun part1(input: List<String>): Int {
        val cargos = parseCargos(input)

        input.takeLastWhile { it.isNotBlank() }
            .map { line -> line.split(' ').slice(1 until 6 step 2) }
            .map { it.map { n -> n.toInt() } }
            .forEach { params -> move(cargos, params[0], params[1] - 1, params[2] - 1, true) }

        cargos.map { it.last() }.joinToString("").println()
        return input.size
    }

    fun part2(input: List<String>): Int {
        val cargos = parseCargos(input)

        input.takeLastWhile { it.isNotBlank() }
            .map { line -> line.split(' ').slice(1 until 6 step 2) }
            .map { it.map { n -> n.toInt() } }
            .forEach { params -> move(cargos, params[0], params[1] - 1, params[2] - 1, false) }

        cargos.map { it.last() }.joinToString("").println()
        return input.size
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
