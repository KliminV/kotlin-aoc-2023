fun main() {

    fun part1(input: List<String>): Int {
        return input
            .map { parseSequence(it) }
            .map { SequencePredict(it) }
            .onEach { it.initCurve() }
            .onEach { it.calculateNext() }
            .sumOf { it.predictionCurve[0].last() }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { parseSequence(it) }
            .map { SequencePredict(it) }
            .onEach { it.initCurve() }
            .onEach { it.initCurve() }
            .onEach { it.calculatePrev() }
            .sumOf { it.predictionCurve[0].last() }
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)
//    check(part2(testInput) == 2286)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

fun parseSequence(input: String): MutableList<Int> {
    return input.split(" ")
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
        .toMutableList()
}

data class SequencePredict(val initialList: MutableList<Int>) {
    lateinit var predictionCurve: List<MutableList<Int>>
    fun initCurve() {
        this.predictionCurve = this.initialList.createPredictionCurveForList()
    }

    fun calculateNext() {
        var prev = 0
        predictionCurve
            .reversed()
            .onEachIndexed { index, ints ->
                val newPrev = ints.last() + prev
                prev = newPrev
                ints.add(newPrev)
            }
    }

    fun calculatePrev() {
        var prev = 0
        predictionCurve
            .reversed()
            .onEachIndexed { index, ints ->
                val newPrev = ints.first() - prev
                prev = newPrev
                ints.add(newPrev)
            }
    }
}

fun MutableList<Int>.createPredictionCurveForList(): List<MutableList<Int>> {
    val result = mutableListOf(this)
    var flag = false
    while (!flag) {
        val last = result[result.size - 1]
        var prev = last[0]
        val list = mutableListOf<Int>()
        for (el in 1..<last.size) {
            val diff = last[el] - prev
            list.add(diff)
            prev = last[el]
        }
        result.add(list)
        if (list.all {
                it == 0
            }) flag = true
    }
    return result
}
