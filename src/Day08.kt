fun main() {
    fun part1(input: List<String>): Int {
        val sequence = input[0]
        val elements = input.filterIndexed { index, s -> index != 0 && s.isNotEmpty() }
            .associate { parseNodes(it) }
        var flag = true
        var steps = 0
        var cur = "AAA"
        while (flag) {
            val node = elements[cur]
            val char = sequence[steps % sequence.length]
            val st = ("$cur $char -> ")
            cur = if (char == 'L') {
                node!!.left
            } else {
                node!!.right
            }
            println("$st $cur")
            steps++
            if (cur == "ZZZ") flag = false
        }
        return steps
    }

    fun part2(input: List<String>): Long {
        val sequence = input[0]
        val elements = input.filterIndexed { index, s -> index != 0 && s.isNotEmpty() }
            .associate { parseNodes(it) }
        var flag = true
        var steps = 0
        var cur = elements.keys.filter { it.endsWith("A") }.toList()
        cur.println()
        val result = mutableListOf<Int>()
        while (flag) {
            val nextCur = mutableListOf<String>()
            for (node in cur) {
                val nodeInstance = elements[node]
                val char = sequence[steps % sequence.length]
                val next = if (char == 'L') {
                    nodeInstance!!.left
                } else {
                    nodeInstance!!.right
                }
                if (next.endsWith("Z")) { result.add(steps+1)}
                else {
                    nextCur.add(next)
                }
            }
            steps++
            flag = nextCur.any { !it.endsWith("Z") }
            cur = nextCur
        }
        val lcm = findLCMOfListOfNumbers(result)
        result.println()
        return lcm
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 6)
    val testInput2 = readInput("Day08_test_2")
    check(part2(testInput2) == 6.toLong())

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}


fun parseNodes(input: String): Pair<String, Node> {
    val sp = input.split("=")
    val name = sp[0].trim()
    val node = sp[1].split(",")
    val left = node[0].trim().substringAfter("(")
    val right = node[1].trim().substringBefore(")").trim()
    return Pair(name, Node(left, right, name))
}
fun findLCMOfListOfNumbers(numbers: List<Int>): Long {
    var result = numbers[0].toLong()
    for (i in 1 until numbers.size) {
        result = findLCM(result, numbers[i].toLong())
    }
    return result
}

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0.toLong() && lcm % b == 0.toLong()) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

data class Node(val left: String, val right: String, val name: String)