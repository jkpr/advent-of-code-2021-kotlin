package day05

import readInput


fun countVents(input: List<String>, diagonals: Boolean): Int {
    return input.flatMap {
        val (x1, y1, x2, y2) = """\d+""".toRegex().findAll(it).map { found -> found.value.toInt() }.toList()
        if (x1 == x2) {
            val (start, end) = listOf(y1, y2).sorted()
            (start..end).map { y -> x1 to y }
        } else if (y1 == y2) {
            val (start, end) = listOf(x1, x2).sorted()
            (start..end).map { x -> x to y1 }
        } else if (diagonals) {
            val dx = if (x1 < x2) 1 else -1
            val dy = if (y1 < y2) 1 else -1
            (0..dx * (x2 - x1)).map { t ->
                (x1 + dx * t) to (y1 + dy * t)
            }
        } else {
            listOf()
        }
    }.groupingBy { it }.eachCount().count { it.value > 1 }
}

fun part1(input: List<String>): Int {
    return countVents(input, diagonals = false)
}

fun part2(input: List<String>): Int {
    return countVents(input, diagonals = true)
}

fun main() {
    val subpackage = "day05"
    val input = readInput(subpackage, "input")
//    val input = readInput(subpackage, "test_input")
    val start = System.currentTimeMillis()
    val result1 = part1(input)
    val result2 = part2(input)
    println("Part 1: $result1")
    println("Part 2: $result2")
    val end = System.currentTimeMillis()
    val delta = (end - start) / 1000.0
    println("Time elapsed: $delta seconds")
}
