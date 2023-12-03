import kotlin.math.abs
import kotlin.math.pow

fun main() {

    fun part1(input: List<String>): Int {
        var result = 0;
        val setLocations = mutableSetOf<NumbersLocation>()
        input.mapIndexed { rowNum, row ->
            val numbers = mutableListOf<Int>()
            val symbols = mutableListOf<Int>()
            row.forEachIndexed { index, c ->
                when (c) {
                    in '0'..'9' -> numbers.add(index)
                    '.' -> Unit
                    else -> symbols.add(index)
                }
            }
            val numbersLocation = mutableListOf<NumbersLocation>()
            var i = 0;
            while (i < numbers.size) {
                val digits = mutableListOf<Int>()
                digits.add(row[numbers[i]].digitToInt())
                val start = numbers[i];

                while (i + 1 < numbers.size && numbers[i + 1] == numbers[i] + 1) {
                    i++
                    digits.add(row[numbers[i]].digitToInt())
                }
                var number = "";
                digits.forEach {
                    number = "$number$it"
                }
                numbersLocation.add(NumbersLocation(number.toInt(), start, numbers[i], rowNum))
                i++

            }
            Pair(numbersLocation, symbols)
        }
            .windowed(2, 1, true) {
                if (it.size == 1) {
                    setLocations.addAll(elementsNearIndexes(it[0].first, it[0].second))
                } else {
                    setLocations.addAll(elementsNearIndexes(it[0].first, it[0].second))
                    setLocations.addAll(elementsNearIndexes(it[1].first, it[0].second))
                    setLocations.addAll(elementsNearIndexes(it[0].first, it[1].second))
                    setLocations.addAll(elementsNearIndexes(it[1].first, it[1].second))
                }
            }
        return setLocations.sumOf { it.number }
    }

    fun part2(input: List<String>): Int {
        val resultedLocations = mutableSetOf<Pair<NumbersLocation, NumbersLocation>>()
        input.mapIndexed { rowNum, row ->
            val numbers = mutableListOf<Int>()
            val symbols = mutableListOf<Int>()
            row.forEachIndexed { index, c ->
                when (c) {
                    in '0'..'9' -> numbers.add(index)
                    '*' -> symbols.add(index)
                }
            }
            val numbersLocation = mutableListOf<NumbersLocation>()
            var i = 0;
            while (i < numbers.size) {
                val digits = mutableListOf<Int>()
                digits.add(row[numbers[i]].digitToInt())
                val start = numbers[i];

                while (i + 1 < numbers.size && numbers[i + 1] == numbers[i] + 1) {
                    i++
                    digits.add(row[numbers[i]].digitToInt())
                }
                var number = "";
                digits.forEach {
                    number = "$number$it"
                }
                numbersLocation.add(NumbersLocation(number.toInt(), start, numbers[i], rowNum))
                i++

            }
            Pair(numbersLocation, symbols.map { Gear(it, rowNum) }.toList())
        }
            .windowed(3, 1, true) { it ->
                val locations = mutableListOf<NumbersLocation>()
                val gears = mutableListOf<Gear>()
                it.onEach { pair ->
                    locations.addAll(pair.first)
                    gears.addAll(pair.second)
                }
                resultedLocations.addAll(gears(locations, gears, input.size))
            }
        return resultedLocations
            .onEach { p ->
                println("${p.first.number} ${p.first.rowId} -> ${p.second.number} ${p.second.rowId}")
            }.sumOf {
            it.second.number.times(it.first.number)
        } / 2
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

data class NumbersLocation(val number: Int, val start: Int, val end: Int, val rowId: Int) {

}

fun elementsNearIndexes(locations: List<NumbersLocation>, symbolIndexes: List<Int>): List<NumbersLocation> {
    var symbolIterator = 0
    val result = mutableListOf<NumbersLocation>()
    locations.forEach {
        while (symbolIterator < symbolIndexes.size && !(symbolIndexes[symbolIterator] >= it.start - 1 && symbolIndexes[symbolIterator] <= it.end + 1) && symbolIndexes[symbolIterator] < it.start - 1) {
            symbolIterator++
        }
        if (symbolIterator < symbolIndexes.size && symbolIndexes[symbolIterator] >= it.start - 1 && symbolIndexes[symbolIterator] <= it.end + 1) {
            result.add(it)
        }
    }
    return result
}

data class Gear(val idx: Int, val rowId: Int)

fun gears(locations: List<NumbersLocation>, gears: List<Gear>, cap: Int): List<Pair<NumbersLocation, NumbersLocation>> {
    return locations.map { i -> locations.map { i to it } }
        .flatten()
        .filter { (left, right) -> left != right }
        .filter { (left, right) ->
            gears.intersect(suitableGears(left, right, cap).toSet()).isNotEmpty()
        }.toList()
}

fun suitableGears(first: NumbersLocation, second: NumbersLocation, cap: Int): List<Gear> {
    val fSet = mutableSetOf(first.start - 1, first.end + 1)
    (first.start..first.end).forEach { fSet.add(it) }
    val sSet = mutableSetOf(second.start - 1, second.end + 1)
    (second.start..second.end).forEach { sSet.add(it) }
    val intersection = fSet.intersect(sSet)
    val rows = mutableListOf<Int>();
    if (first.rowId == second.rowId) {
        rows.add(first.rowId)
        if (first.rowId > 0) rows.add(first.rowId-1)
        if (first.rowId < cap) rows.add(first.rowId+1)
    } else if (abs(first.rowId - second.rowId) == 1) {
        rows.add(first.rowId)
        rows.add(second.rowId)
    } else {
        rows.add(
            if (second.rowId > first.rowId) second.rowId - 1 else first.rowId - 1
        )
    }
    return intersection.map {
        rows.map { rid -> Gear(it, rid) }.toList()
    }
        .flatten()
}