fun main() {
    val limits = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14,
    )

    fun part1(input: List<String>): Int {
        return input.map {
            it.split(":")
        }.map {
            val gameNumber = it[0].split(" ")[1].toInt()
            Pair(gameNumber, it[1].split(";"))
        }.filter {
            val list = it.second
            list.all { hand ->
                hand.split(",")
                    .all { pair ->
                        val split = pair.trim().split(" ")
                        val amount = split[0]
                        val color = split[1]
                        limits[color]!! >= amount.toInt()
                    }
            }
        }
            .sumOf { it.first }
    }

    fun part2(input: List<String>): Int {
        return input.map {
            it.split(":")
        }.map {
            it[1].split(";")
        }.sumOf {
            var maxGreen = 0;
            var maxBlue = 0;
            var maxRed = 0;
            it.onEach { hand ->
                hand.split(",")
                    .onEach { pair ->
                        val split = pair.trim().split(" ")
                        val amount = split[0].toInt()
                        val color = split[1]
                        when (color) {
                            "red" -> maxRed = maxRed.coerceAtLeast(amount)
                            "green" -> maxGreen = maxGreen.coerceAtLeast(amount)
                            "blue" -> maxBlue = maxBlue.coerceAtLeast(amount)
                        }
                    }
            }
            maxGreen * maxRed * maxBlue
        }
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
