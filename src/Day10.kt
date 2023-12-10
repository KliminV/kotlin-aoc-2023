import kotlin.io.path.Path
import kotlin.io.path.writeLines

fun main() {

    fun part1(input: List<String>): Int {
        val start = input.mapIndexed { index, s ->
            val result = if (s.contains("S")) {
                s.indexOf('S')
            } else -1
            Point(result, index, 'S')
        }
            .first { it.x != -1 }
        val queue = ArrayDeque<Point>()
        var distance = 0
        // north
        println(start)
        if (start.y > 0) {
            when (input[start.y - 1][start.x]) {
                '|' -> queue.add(Point(start.x, start.y - 1, '|'))
                'L' -> queue.add(Point(start.x, start.y - 1, 'L'))
                'J' -> queue.add(Point(start.x, start.y - 1, 'J'))
            }
        }
        // south
        if (start.y < input.size - 1) {
            when (input[start.y + 1][start.x]) {
                '|' -> queue.add(Point(start.x, start.y + 1, '|'))
                '7' -> queue.add(Point(start.x, start.y + 1, '7'))
                'F' -> queue.add(Point(start.x, start.y + 1, 'F'))
            }
        }
        // west
        if (start.x > 0) {
            when (input[start.y][start.x - 1]) {
                '-' -> queue.add(Point(start.x - 1, start.y, '-'))
                '7' -> queue.add(Point(start.x - 1, start.y, '7'))
                'J' -> queue.add(Point(start.x - 1, start.y, 'J'))
            }
        }
        // right
        if (start.x < input[0].length - 1) {
            when (input[start.y][start.x + 1]) {
                '-' -> queue.add(Point(start.x + 1, start.y, '-'))
                'L' -> queue.add(Point(start.x + 1, start.y, 'L'))
                'F' -> queue.add(Point(start.x + 1, start.y, 'F'))
            }
        }
        distance++
        val visited = mutableSetOf(start)
        while (queue.isNotEmpty()) {
            val point = queue.removeFirst()
            visited.add(point)
            var one: Point
            var two: Point
            when (point.char) {
                '-' -> {
                    one = Point(point.x - 1, point.y, input[point.y][point.x - 1])
                    two = Point(point.x + 1, point.y, input[point.y][point.x + 1])
                }

                '|' -> {
                    one = Point(point.x, point.y - 1, input[point.y - 1][point.x])
                    two = Point(point.x, point.y + 1, input[point.y + 1][point.x])
                }

                'L' -> {
                    one = Point(point.x, point.y - 1, input[point.y - 1][point.x])
                    two = Point(point.x + 1, point.y, input[point.y][point.x + 1])
                }

                'J' -> {
                    one = Point(point.x, point.y - 1, input[point.y - 1][point.x])
                    two = Point(point.x - 1, point.y, input[point.y][point.x - 1])
                }

                'F' -> {
                    one = Point(point.x, point.y + 1, input[point.y + 1][point.x])
                    two = Point(point.x + 1, point.y, input[point.y][point.x + 1])
                }

                '7' -> {
                    one = Point(point.x, point.y + 1, input[point.y + 1][point.x])
                    two = Point(point.x - 1, point.y, input[point.y][point.x - 1])
                }

                else -> {
                    one = start
                    two = start
                }
            }
            if (visited.add(one)) {
                queue.addLast(one)
            }
            if (visited.add(two)) {
                queue.addLast(two)
            }
            distance++

        }
        println(distance)
        return distance / 2
    }

    fun part2(input: List<String>): Int {
        val start = input.mapIndexed { index, s ->
            val result = if (s.contains("S")) {
                s.indexOf('S')
            } else -1
            Point(result, index, 'S')
        }
            .first { it.x != -1 }
        val mirror = input.map { StringBuilder(" ".repeat(it.length)) }
            .toList()
        val queue = ArrayDeque<Point>()
        var distance = 0
        // north
        println(start)
        if (start.y > 0) {
            when (input[start.y - 1][start.x]) {
                '|' -> queue.add(Point(start.x, start.y - 1, '|'))
                'L' -> queue.add(Point(start.x, start.y - 1, 'L'))
                'J' -> queue.add(Point(start.x, start.y - 1, 'J'))
            }
        }
        // south
        if (start.y < input.size - 1) {
            when (input[start.y + 1][start.x]) {
                '|' -> queue.add(Point(start.x, start.y + 1, '|'))
                '7' -> queue.add(Point(start.x, start.y + 1, '7'))
                'F' -> queue.add(Point(start.x, start.y + 1, 'F'))
            }
        }
        // west
        if (start.x > 0) {
            when (input[start.y][start.x - 1]) {
                '-' -> queue.add(Point(start.x - 1, start.y, '-'))
                '7' -> queue.add(Point(start.x - 1, start.y, '7'))
                'J' -> queue.add(Point(start.x - 1, start.y, 'J'))
            }
        }
        // right
        if (start.x < input[0].length - 1) {
            when (input[start.y][start.x + 1]) {
                '-' -> queue.add(Point(start.x + 1, start.y, '-'))
                'L' -> queue.add(Point(start.x + 1, start.y, 'L'))
                'F' -> queue.add(Point(start.x + 1, start.y, 'F'))
            }
        }
        distance++
        val visited = mutableSetOf(start)
        while (queue.isNotEmpty()) {
            val point = queue.removeFirst()
            mirror[point.y][point.x] = when (point.char) {
                '-' -> '─'
                'F' -> '┌'
                'L' -> '└'
                '7' -> '┐'
                'J' -> '┘'
                'S' -> '█'
                else -> '|'
            }
            visited.add(point)
            var one: Point
            var two: Point
            when (point.char) {
                '-' -> {
                    one = Point(point.x - 1, point.y, input[point.y][point.x - 1])
                    two = Point(point.x + 1, point.y, input[point.y][point.x + 1])
                }

                '|' -> {
                    one = Point(point.x, point.y - 1, input[point.y - 1][point.x])
                    two = Point(point.x, point.y + 1, input[point.y + 1][point.x])
                }

                'L' -> {
                    one = Point(point.x, point.y - 1, input[point.y - 1][point.x])
                    two = Point(point.x + 1, point.y, input[point.y][point.x + 1])
                }

                'J' -> {
                    one = Point(point.x, point.y - 1, input[point.y - 1][point.x])
                    two = Point(point.x - 1, point.y, input[point.y][point.x - 1])
                }

                'F' -> {
                    one = Point(point.x, point.y + 1, input[point.y + 1][point.x])
                    two = Point(point.x + 1, point.y, input[point.y][point.x + 1])
                }

                '7' -> {
                    one = Point(point.x, point.y + 1, input[point.y + 1][point.x])
                    two = Point(point.x - 1, point.y, input[point.y][point.x - 1])
                }

                else -> {
                    one = start
                    two = start
                }
            }
            if (visited.add(one)) {
                queue.addLast(one)
            }
            if (visited.add(two)) {
                queue.addLast(two)
            }
            distance++

        }
        var n = 0

        for (i in input.indices) {
            val line = input[i]
            var isInside = false
            var pending: Char? = null
            for (column in line.indices) {
                val ch = line[column]
                if (Point(column, i, ch) in visited) {
                    when (ch) {
                        '|' -> isInside = !isInside
                        'F', 'L' -> pending = ch
                        'J' -> if (pending == 'F') isInside = !isInside
                        '7' -> if (pending == 'L') isInside = !isInside
                    }
                } else if (isInside) {
                    n++
                }
            }
        }
        return n
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 0)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

data class Point(val x: Int, val y: Int, val char: Char)