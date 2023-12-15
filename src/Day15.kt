fun main() {

    fun part1(input: List<String>): Int {
        val ip = input[0]
        return ip.split(",")
            .sumOf { s ->
                s.toHash()
            }
    }

    fun part2(input: List<String>): Int {
        val map = mutableMapOf<Int, MutableMap<String, Int>>()
        input[0]
            .split(",")
            .onEach { s ->
                val sp = s.split("=", "-")
                val box = sp[0].toHash()
                if (sp[1].isEmpty()) {
                    map[box]?.remove(sp[0])
                } else {
                    val nlm = map[box] ?: mutableMapOf()
                    nlm[sp[0]] = sp[1].toInt()
                    map[box] = nlm
                }
            }
        return map.keys.sumOf { k ->
            var r = 0
            map[k]!!.onEachIndexed { index, entry ->
                r += (index + 1).times(entry.value).times(k + 1)
            }
            r
        }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}

fun String.toHash(): Int {
    return this.map { c -> c.code }.fold(0) { acc, i ->
        (acc + i).times(17) % 256
    }
}
