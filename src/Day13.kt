fun main() {
    fun part1(input: String): Int {
        return sumReflections(input, 0)
    }

    fun part2(input: String): Int {
       return sumReflections(input, 1)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readAllInput("Day13_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readAllInput("Day13")
    part1(input).println()
    part2(input).println()
}

fun sumReflections(input: String, targetDiff: Int): Int {
    return input.split("\r\n\r\n")
        .map {
            val s = it.split("\n")
            s.map { l -> l.trim() }
        }
        .map { findReflection(it, targetDiff) }
        .sumOf { el ->
            val v = el["v"]?.sum() ?: 0
            val h = el["h"]?.sum()?.times(100) ?: 0
            v + h
        }
}

fun findReflection(s: List<String>, targetDiff: Int = 0): Map<String, List<Int>> {
    val h = List(s.size) { index ->
        if (index == s.size - 1) -1
        else {
            var diff = 0
            for (i in 0..index) {
                val top = index + i + 1
                val bot = index - i
                if (top < s.size && bot > -1) {
                    for (j in 0 until s[0].length)
                        diff += if (s[index - i][j] == s[index + i + 1][j]) 0 else 1
                }
            }
            if (diff == targetDiff) index + 1 else -1
        }
    }.filter { it != -1 }.toList()
    val v = mutableListOf<Int>()
    for (ver in 0 until s[0].length - 1) {
        var diff = 0
        for (i in 0..ver) {
            val left = ver - i
            val right = ver + i + 1
            if (right < s[0].length) {
                val lLeft = s.map { it[left] }.toList()
                val lRight = s.map { it[right] }.toList()
                for (j in lLeft.indices) {
                    diff += if (lLeft[j] == lRight[j]) 0 else 1
                }
            }
        }
        if (diff == targetDiff) v.add(ver + 1)
    }
    return mapOf(Pair("h", h), Pair("v", v.toList()))
}
