import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { it.split(":")[1] }
            .map { it.toPairOfSet() }
            .map { p -> p.first.intersect(p.second).size }
            .sumOf { v -> 2.0.pow(v.toDouble() - 1).toInt() }
    }


    fun part2(input: List<String>): Int {
        val map = mutableMapOf<Int, Int>()
        input
            .map {
                val split = it.split(":")
                Pair(split[0], split[1])
            }
            .onEach {
                val ticket = it.first.trim().split(" ")
                    .filter { r -> r.isNotEmpty() }[1].toInt()
                val pairs = it.second.toPairOfSet()
                val cost = pairs.first.intersect(pairs.second).size
                map[ticket] = cost
            }

        val list = mutableListOf(0)
        for (i in 1..map.keys.max()) {
            list.add(1)
        }
        map.keys
            .onEach {
            val wins = map[it]
            for (i in 1..wins!!) {
                list[i + it] = list[i + it] + list[it]
            }
        }
        return list.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    part2(testInput).println()
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

fun String.toSetOfNumbers(): Set<Int> {
    return this.split(" ").map { num -> num.trim() }
        .filter { el -> el.isNotEmpty() }
        .map { num -> num.toInt() }
        .toSet()
}

typealias PairSet = Pair<Set<Int>, Set<Int>>

fun String.toPairOfSet(): PairSet {
    val split = this.split("|")
    val win = split[0].toSetOfNumbers()
    val my = split[1].toSetOfNumbers()
    return PairSet(win, my)
}