import java.lang.IllegalArgumentException
import java.util.Collections.shuffle

fun main() {
    fun calcScore(round: String): Int {
        val opponent = round.first() - 'A'
        val player = round.last() - 'A'

        val outcomeScore = when ((opponent - player).mod(3)) {
            0 -> 3
            1 -> 0
            2 -> 6
            else -> 0
        }
        val shapeScore = when (player) {
            0 -> 1
            1 -> 2
            2 -> 3
            else -> 0
        }

        return outcomeScore + shapeScore
    }

    fun Char.win() = when(this) {
        'A', 'B' -> this + 1
        'C' -> 'A'
        else -> 'A'
    }

    fun Char.lose() = when(this) {
        'B', 'C' -> this - 1
        'A' -> 'C'
        else -> throw IllegalArgumentException("ABC Only")
    }

    fun part1(input: List<String>): Int {
        val permutations = listOf("XYZ", "XZY", "YXZ", "YZX", "ZXY", "ZYX")
        return permutations.maxOf { permutation ->
            input.groupBy { it }
                .mapKeys {
                    it.key.replace(permutation[0], 'A')
                        .replace(permutation[1], 'B')
                        .replace(permutation[2], 'C')
                }
                .mapValues { calcScore(it.key) * it.value.size }
                .values
                .sum()
        }
    }

    fun part2(input: List<String>): Int {
        val permutations = listOf("XYZ", "XZY", "YXZ", "YZX", "ZXY", "ZYX")
        return permutations.maxOf { permutation ->
            input.groupBy { it }
                .mapKeys {
                    val opponent = it.key.first()
                    it.key.replace('X', opponent.lose())
                        .replace('Y', opponent)
                        .replace('Z', opponent.win())
                }
                .mapValues { calcScore(it.key) * it.value.size }
                .values
                .sum()
        }
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
