fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            var first = 10
            var last = 10
            for (char: Char in it) {
                val status = char.isDigit();
                if (status) {
                    val current = char.digitToInt()
                    first = if (first == 10) current else first
                    last = current
                }
            }
            first * 10 + last
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            var first = 10
            var last = 0
            for ((index, value) in it.withIndex()) {
                val status = value.isDigit();
                if (status) {
                    val current = value.digitToInt()
                    first = if (first == 10) current else first
                    last = current
                } else {
                    var current = -1
                    if (it.startsWith("one", index)) {
                        current = 1
                    } else if (it.startsWith("two", index)) {
                        current = 2
                    } else if (it.startsWith("three", index)) {
                        current = 3
                    } else if (it.startsWith("four", index)) {
                        current = 4
                    } else if (it.startsWith("five", index)) {
                        current = 5
                    } else if (it.startsWith("six", index)) {
                        current = 6
                    } else if (it.startsWith("seven", index)) {
                        current = 7
                    } else if (it.startsWith("eight", index)) {
                        current = 8
                    } else if (it.startsWith("nine", index)) {
                        current = 9
                    }
                    if (current!=-1) {
                        first = if (first == 10) current else first
                        last = current
                    }
                }
            }
            first * 10 + last
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 142)
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
