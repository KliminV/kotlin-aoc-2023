fun main() {
    fun part1(input: List<String>): Long {
        val map = parseInput(input)
        val d = map.keys.map { k ->
            val v = map[k]
            val bot= calcBorder(k, v!!)
            val top= calcBorder(k, v, true)
            Pair(bot, top)
        }
            .onEach { println(it) }
            .map { it.second - it.first+1 }
            .toList()
        d.println()
        var res = 1.toLong()
        d.onEach { res *= it }
        return res
    }

    fun part2(input: List<String>): Long {
        val time = parseLineMerged(input[0])
        val distance = parseLineMerged(input[1])
        val bot= calcBorder(time,distance)
        val top= calcBorder(time,distance, true)
        return top-bot+1
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288.toLong())
    check(part2(testInput) == 71503.toLong())

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

fun parseInput(input: List<String>): Map<Long, Long> {
    val time = parseLine(input[0])
    val distance = parseLine(input[1])
    return time.mapIndexed { index, i -> Pair(i, distance[index]) }
        .toMap()
}

fun calcBorder(time: Long, distance: Long, top: Boolean = false) : Long{
    var left = 1.toLong()
    var right = time
    if (top) {
        while (calcDistance(time,right) <= distance) {
            right--
        }
    } else {
        while (calcDistance(time,left) <= distance) {
            left++
        }
    }
    return if (top) right else left
}

fun calcDistance(time: Long, hold: Long) : Long {
//    println("$time $hold ${(time - hold ) * hold}")
    return (time - hold ) * hold
}

fun parseLine(input: String): List<Long> {
    return input.split(":")[1]
        .split(" ")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .map { it.toLong() }
        .toList()
}

fun parseLineMerged(input: String): Long {
    return input.split(":")[1].replace(" ", "").toLong()
}
