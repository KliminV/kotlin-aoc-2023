fun main() {

    fun part1(input: List<String>): Long {
        return getSumOfDistances(input)
    }

    fun part2(input: List<String>, emptyGalaxyDistance: Int = 1000000): Long {
        return getSumOfDistances(input, emptyGalaxyDistance)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374.toLong())
    check(part2(testInput, 10) == 1030.toLong())
    check(part2(testInput, 100) == 8410.toLong())

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

fun getSumOfDistances(input: List<String>, emptyGalaxyDistance: Int = 2): Long {
    val galaxies = input
        .mapIndexed { index, s ->
            s.mapIndexed { col, c -> if (c == '#') Coordinate(index, col) else null }
                .toList()
        }
        .flatten()
        .filterNotNull()
        .toList()
    val cols = galaxies.map { it.col }
        .groupBy { it }
        .keys
        .sorted()
        .toList()
    val rows = galaxies.map { it.row }
        .groupBy { it }
        .keys
        .sorted()
        .toList()
    val pairs = galaxies.mapIndexed { index, s ->
        galaxies.slice(index + 1 until galaxies.size).map { Pair(s, it) }
    }
        .flatten()
    return pairs.sumOf { (a, b) ->
        var result = 0.toLong()
        for (i in a.row + 1..b.row) {
            result += if (i in rows) 1 else emptyGalaxyDistance
        }
        for (i in b.row + 1..a.row) {
            result += if (i in rows) 1 else emptyGalaxyDistance
        }
        for (i in a.col + 1..b.col) {
            result += if (i in cols) 1 else emptyGalaxyDistance
        }
        for (i in b.col + 1..a.col) {
            result += if (i in cols) 1 else emptyGalaxyDistance
        }
        result
    }
}

data class Coordinate(val row: Int, val col: Int)