fun main() {
    fun part1(input: String): Long {
        val task = parseInput(input)
        return task.calcMinForSeed(task.seeds)
    }

    fun part2(input: String): Long {
        var min = Long.MAX_VALUE
        val task = parseInput(input, false)
        var iter = 0
        while (iter < task.seeds.size) {
            val start = task.seeds[iter]
            val end = start + task.seeds[iter + 1] - 1
            println("${task.seeds.size} $iter")
            for (i in start..end) {
                min = min.coerceAtMost(task.calcMinForSeed(listOf(i)))
            }
            iter += 2
        }
        return min
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readAllInput("Day05_test")
    check(part1(testInput) == 35.toLong())
    check(part2(testInput) == 46.toLong())

    val input = readAllInput("Day05")
    part1(input).println()
    part2(input).println()
}

data class ShortMapRow(val destination: Long, val source: Long, val range: Long)

fun TaskInput.calcMinForSeed(seeds: List<Long>): Long {
    val task = this
    return seeds
        .map { seed ->
            task.seedToSoil[seed]
        }
        .map { s ->
            task.soilToFertilizer[s]
        }
        .map { f ->
            task.fertilizerToWater[f]
        }
        .map { w ->
            task.waterToLight[w]
        }
        .map { l ->
            task.lightToTemperature[l]
        }
        .map { t ->
            task.temperatureToHumidity[t]
        }.minOfOrNull { h ->
            task.humidityToLocation[h]
        }!!
}

data class TaskInput(
    val seeds: List<Long>,
    val seedToSoil: ShortMap,
    val soilToFertilizer: ShortMap,
    val fertilizerToWater: ShortMap,
    val waterToLight: ShortMap,
    val lightToTemperature: ShortMap,
    val temperatureToHumidity: ShortMap,
    val humidityToLocation: ShortMap,
)

fun parseSeeds(input: String, range: Boolean): List<Long> {
    val list = input.split(":")[1]
        .split(" ")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .map { it.toLong() }
        .toList()
    return if (!range) list
    else list.windowed(2, 2) {
        it[0]..<it[0] + it[1]
    }.flatten()
        .toList()
}

fun parseInput(input: String, seedRange: Boolean = false): TaskInput {
    val sp = input.split("\r\n\r\n")
    return TaskInput(
        parseSeeds(sp[0], seedRange),
        parseShortMap(sp[1]),
        parseShortMap(sp[2]),
        parseShortMap(sp[3]),
        parseShortMap(sp[4]),
        parseShortMap(sp[5]),
        parseShortMap(sp[6]),
        parseShortMap(sp[7]),
    )
}

fun parseShortMap(input: String): ShortMap {
    val ip = input.split(":")[1]
    val list = ip
        .split("\r\n")
        .filter { it.isNotEmpty() }
        .map { st ->
            val t = st.split(" ")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
            ShortMapRow(t[0].toLong(), t[1].toLong(), t[2].toLong())
        }.toList()
    return list.sortedBy { it.source }
}
typealias ShortMap = List<ShortMapRow>

operator fun ShortMap.get(key: Long): Long {
    val res = this.find {
        it.source <= key && (it.source + it.range) > key
    }
    val r = if (res != null) res.destination - res.source + key else key
    return r
}
