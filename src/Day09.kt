import kotlin.math.abs

fun main() {
    class Node(
        var x: Int = 0,
        var y: Int = 0
    ) {
        override fun toString(): String {
            return "($x, $y)"
        }

        fun move(direction: String) {
            when (direction) {
                "L" -> this.x -= 1
                "R" -> this.x += 1
                "D" -> this.y -= 1
                "U" -> this.y += 1
            }
        }

        infix fun adjust(other: Node): Boolean = abs(x - other.x) <= 1 && abs(y - other.y) <= 1

        fun follow(other: Node) {
            if (this adjust other) return

            if (x != other.x) {
                this.x += (other.x - x) / abs(other.x - x)
            }
            if (y != other.y) {
                this.y += (other.y - y) / abs(other.y - y)
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Node

            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x
            result = 100000 * result + y
            return result
        }
    }

    fun proc(input: List<String>, nodeNum: Int): Int {
        val posSet = mutableSetOf<Node>()
        val nodes = mutableListOf<Node>()
        repeat(nodeNum) { nodes.add(Node(0, 0)) }

        posSet.add(nodes.last())
        input.forEach { line ->
            val direction = line.split(' ')[0]
            val distance = line.split(' ')[1].toInt()

            repeat(distance) {
                nodes.first().move(direction)
                nodes.drop(1).forEachIndexed { i, node -> node.follow(nodes[i]) }

                posSet.add(nodes.last())
            }
        }
        return posSet.size
    }

    fun part1(input: List<String>): Int {
        return proc(input, 2)
    }

    fun part2(input: List<String>): Int {
        return proc(input, 10)
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
