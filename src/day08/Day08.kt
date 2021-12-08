package day08

import readInput


fun getPatternMap(patterns: List<Set<Char>>) = patterns.associateBy { it.size }.mapKeys {
    when(it.key) {
        2 -> 1
        3 -> 7
        4 -> 4
        7 -> 8
        else -> -1
    }
}.filterKeys { it != -1 }.toMutableMap().also {
    val bd = it.getValue(4) - it.getValue(1)
    patterns.forEach { pattern ->
        when(pattern.size) {
            5 -> when {
                pattern.intersect(it.getValue(1)).size == 2 -> it[3] = pattern
                pattern.intersect(bd).size == 2 -> it[5] = pattern
                else -> it[2] = pattern
            }
            6 -> when {
                pattern.intersect(bd).size == 1 -> it[0] = pattern
                pattern.intersect(it.getValue(1)).size == 1 -> it[6] = pattern
                else -> it[9] = pattern
            }
        }
    }
}

fun part1(input: List<String>) = input.flatMap { line ->
    """\w+""".toRegex().findAll(line, line.indexOf('|')).map { it.value }
}.count { it.length in setOf(2, 3, 4, 7) }

fun part2(input: List<String>) = input.sumOf { line ->
    """\w+""".toRegex().findAll(line).map {
        it.value.toSet()
    }.toList().let { signals ->
        val patternToDigit = getPatternMap(signals.take(10)).entries.associate { (k, v) -> v to k }
        signals.takeLast(4).map {
            patternToDigit.getValue(it)
        }.joinToString("").toInt()
    }
}

fun main() {
    val subpackage = "day08"
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
