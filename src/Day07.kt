import kotlin.math.min

fun main() {
    class Node(
        val id: String,
        val parent: Node?,
        val children: MutableMap<String, Node>,
        val isDir: Boolean,
        var size: Int? = null
    ) {
        fun getSize(): Int {
            return size ?: fetchSize().also { this.size = it }
        }

        fun fetchSize(): Int =
            if (isDir) children.values.sumOf { it.getSize() }
            else size ?: throw RuntimeException("Something is wrong!: $this")

        fun makeChild(
            id: String,
            isDir: Boolean,
            size: Int? = null
        ) = Node(id, this, mutableMapOf(), isDir, size)
            .also { this.children[it.id] = it }

        fun parseLine(line: String): Node {
            val tokens = line.split(' ')
            return if (tokens[0].isNumeric()) makeChild(tokens[1], false, tokens[0].toInt())
            else if (tokens[0] == "dir") makeChild(tokens[1], true)
            else throw RuntimeException("Unknown command: $line")
        }

        fun calcTotalSize(limit: Int): Int {
            return if (!this.isDir) 0
            else if (this.getSize() < limit) this.getSize() + children.values.sumOf { it.calcTotalSize(limit) }
            else children.values.sumOf { it.calcTotalSize(limit) }
        }

        fun getMin(least: Int): Int {
            return if (!this.isDir) 100_000_000
            else if (this.getSize() < least) 100_000_000
            else min(getSize(), this.children.values.minOf { it.getMin(least) })
        }

        val isRoot = isDir && parent == null
    }

    fun parseTree(input: List<String>): Node {
        var current = Node("/", null, mutableMapOf(), true)
        input.forEach { line ->
//            println("current: [${current.hashCode()}], ${current.id}, parent: [${current.parent.hashCode()}] ${current.parent?.id}, line: $line")
            if (line.startsWith("$")) {
                if (line != "$ ls") {
                    if (line.endsWith("..")) current = current.parent
                        ?: throw RuntimeException("This is root: $current")
                    else current = current.children[line.split(' ').last()]
                        ?: throw RuntimeException("No children in ${current.children}, ${line.split(' ').last()}")
                }
            } else current.parseLine(line)
        }
        while (!current.isRoot) current = current.parent ?: throw RuntimeException("Already root")
        return current
    }


    fun part1(input: List<String>): Int {
        return parseTree(input.drop(2))
            .calcTotalSize(100000)
    }

    fun part2(input: List<String>): Int {
        return parseTree(input.drop(2))
            .let { it.getMin(it.getSize() - 40_000_000) }
    }

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
