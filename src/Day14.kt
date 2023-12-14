import kotlin.io.path.Path
import kotlin.io.path.writeLines

fun main() {

    fun part1(ip: List<String>): Int {
        val input = ip.map { StringBuilder(it) }.toList()
        input.rotate('N')
        return sum(input)
    }

    fun part2(ip: List<String>): Int {
        var input = ip.map { StringBuilder(it) }.toList()
        var cur = 0
        val map = mutableMapOf<Int, Int>()
        var run = true
        (1..1_000_000).chunked(10000) {
            if (run) {
                cur += 10_000
                it.onEach {
                    input = input.rotate('N')
                    input = input.rotate('W')
                    input = input.rotate('S')
                    input = input.rotate('E')
                }
                val su = sum(input)
                println("step $cur: $su")
                map.put(cur, su)
                val repeat = map.keys.groupingBy { m -> map[m] }
                    .aggregate { _, accumulator: MutableList<Int>?, element: Int, first: Boolean ->
                        if (first)
                            mutableListOf(element)
                        else {
                            accumulator!!.add(element)
                            accumulator
                        }
                    }.filter { it.value.size > 1 }
                    .size
                if (repeat >= 5) {
                    run = false
                }
            }
        }
        val count = map.keys.groupingBy { m -> map[m] }
            .aggregate { _, accumulator: MutableList<Int>?, element: Int, first: Boolean ->
                if (first)
                    mutableListOf(element)
                else {
                    accumulator!!.add(element)
                    accumulator
                }
            }.filter {  it.value.size == 2 }
            .map{ it.value[1]-it.value[0] }
            .groupingBy { it }
            .eachCount()
        val diff = count.maxBy { it.value }.key
        val rem = 1_000_000_000 % diff
        return map[rem]!!
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136)
    check(part2(testInput) == 64)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}

fun List<StringBuilder>.rotate(d: Char): List<StringBuilder> {
    val input = this
    var set = mutableSetOf<Int>()
    when (d) {
        'N' -> {
            for (i in input[0].indices) {
                var list = mutableListOf<Int>()
                for (j in input.size - 1 downTo 0) {
                    when (input[j][i]) {
                        'O' -> list.add(j)
                        '#' -> {
                            for (n in 0 until list.size) {
                                val id = j + 1 + n
                                if (!set.contains(list[n])) {
                                    input[list[n]][i] = '.'
                                }
                                input[id][i] = 'O'
                                set.add(id)
                            }
                            list = mutableListOf()
                            set = mutableSetOf()
                        }
                        else -> {}
                    }
                }
                for (n in list.indices) {
                        input[list[n]][i] = '.'
                }
                for (n in list.indices) {
                    input[n][i]='O'
                }
            }
        }

        'S' -> {
            for (i in input[0].indices) {
                var list = mutableListOf<Int>()
                for (j in input.indices) {
                    when (input[j][i]) {
                        'O' -> list.add(j)
                        '#' -> {
                            for (n in 0 until list.size) {
                                val id = j - 1 - n
                                if (!set.contains(list[n]))
                                    input[list[n]][i] = '.'
                                input[id][i] = 'O'
                                set.add(id)

                            }
                            set = mutableSetOf()
                            list = mutableListOf()
                        }
                        else -> {}
                    }
                }
                for (n in 0 until list.size) {
                    val id = input.size - 1 - n
                    if (!set.contains(list[n])) {
                        input[list[n]][i] = '.'
                    }
                    set.add(id)
                    input[id][i] = 'O'
                }
            }
        }

        'W' -> {
            for (i in input.indices) {
                var list = mutableListOf<Int>()
                for (j in input[i].length - 1 downTo 0) {
                    when (input[i][j]) {
                        'O' -> list.add(j)
                        '#' -> {
                            for (n in 0 until list.size) {
                                val id = j + 1 + n
                                if (!set.contains(list[n])) {
                                    input[i][list[n]] = '.'
                                }
                                set.add(id)
                                input[i][id] = 'O'
                            }
                            list = mutableListOf()
                            set = mutableSetOf()
                        }
                        else -> {}
                    }
                }
                for (n in 0 until list.size) {
                    if (!set.contains(list[n])) {
                        input[i][list[n]] = '.'
                    }
                    set.add(n)
                    input[i][n] = 'O'
                }
            }
        }

        'E' -> {
            for (i in input.indices) {
                var list = mutableListOf<Int>()
                for (j in input[i].indices) {
                    when (input[i][j]) {
                        'O' -> list.add(j)
                        '#' -> {
                            for (n in 0 until list.size) {
                                val id = j - 1 - n
                                if (!set.contains(list[n])) {
                                    input[i][list[n]] = '.'
                                }
                                set.add(id)
                                input[i][id] = 'O'
                            }
                            list = mutableListOf()
                            set = mutableSetOf()
                        }
                        else -> {}
                    }
                }
                for (n in 0 until list.size) {
                    val id = input[i].length - 1 - n
                    if (!set.contains(list[n])) {
                        input[i][list[n]] = '.'
                    }
                    set.add(id)
                    input[i][id] = 'O'
                }
            }
        }
    }
    return input
}

fun sum (input: List<StringBuilder> ) : Int{
    var res = 0
    var tot = 0
    for (i in input[0].indices) {
        for (j in input.indices) {
            when (input[j][i]) {
                'O' -> {
                    res += input.size - j
                    tot++
                }
                else -> {}
            }
        }
    }
    return res
}