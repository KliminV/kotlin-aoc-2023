import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .asSequence()
            .map { it.parseInput() }
            .sortedWith(Combination::compare)
            .mapIndexed { index, combination ->
                (index + 1).times(combination.bid)
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input
            .asSequence()
            .map { it.parseInput(true) }
            .sortedWith(Combination::compare)
            .mapIndexed { index, combination ->
                (index + 1).times(combination.bid)
            }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

fun Char.cardToPower(withJokers: Boolean = false): Int {
    if (this.isDigit()) {
        return this.digitToInt()
    }
    return when (this) {
        'T' -> 10
        'J' -> if (withJokers) 1 else 11
        'Q' -> 12
        'K' -> 13
        'A' -> 14
        else -> 0
    }
}

fun String.parseInput(withJokers: Boolean = false): Combination {
    val list = this.split(" ")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .toList()

    return Combination(list[0], list[0].toCombinationArray(withJokers), list[1].toInt(), withJokers)
}

fun String.toCombinationArray(withJokers: Boolean = false): IntArray {
    val st = IntArray(15)
    this.onEach { st[it.cardToPower(withJokers)]++ }
    return st
}


data class Combination(
    val combination: String,
    val cArray: IntArray,
    val bid: Int,
    val withJokers: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Combination
        return cArray.contentEquals(other.cArray)
    }

    private val combinationPower = mapOf(
        Pair("1", 1),
        Pair("2", 2),
        Pair("22", 3),
        Pair("3", 4),
        Pair("32", 5),
        Pair("4", 6),
        Pair("5", 7),
    )

    fun compare(another: Combination): Int {
        if (this.powerOfCombination() > another.powerOfCombination()) {
            return 1
        } else if (this.powerOfCombination() < another.powerOfCombination()) {
            return -1
        } else {
            for (i in 0..4) {
                if (this.combination.elementAt(i).cardToPower(withJokers)
                    > another.combination.elementAt(i).cardToPower(withJokers)
                ) {
                    return 1
                } else if (this.combination.elementAt(i).cardToPower(withJokers)
                    < another.combination.elementAt(i).cardToPower(withJokers)
                ) {
                    return -1
                }
            }
            return 0
        }
    }

    private fun powerOfCombination(): Int {
        var max = 0
        var sec = 0
        val jokers = cArray[1]
        for (iter in cArray.indices) {
            if (iter == 1) continue;
            if (cArray[iter] > max) {
                sec = sec.coerceAtLeast(max)
                max = cArray[iter]
            } else {
                sec = sec.coerceAtLeast(cArray[iter])
            }
        }
        val key = "${max + jokers}${if (sec > 1) sec else ""}"
        return combinationPower[key]!!
    }

    override fun hashCode(): Int {
        return cArray.contentHashCode()
    }
}
