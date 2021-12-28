package day18

import kotlin.system.measureTimeMillis
import utils.readInput


fun String.addToLastInt(int: Int): String {
    val match = """\d+""".toRegex().findAll(this).lastOrNull()
    return if (match == null) this else {
        replaceRange(match.range, (match.value.toInt() + int).toString())
    }
}

fun String.addToFirstInt(int: Int): String {
    val match = """\d+""".toRegex().findAll(this).firstOrNull()
    return if (match == null) this else {
        replaceRange(match.range, (match.value.toInt() + int).toString())
    }
}

fun String.explode(): String {
    val stack = mutableListOf<Int>()
    for ((index, char) in withIndex()) {
        when (char) {
            '[' -> stack.add(index)
            ']' -> {
                val start = stack.removeLast()
                if (stack.size >= 4) {
                    val (int1, int2) = substring(start + 1, index).split(',').map { it.toInt() }
                    val left = substring(0, start).addToLastInt(int1)
                    val right = substring(index + 1).addToFirstInt(int2)
                    return "${left}0$right"
                }
            }
        }
    }
    return this
}

fun String.split(): String {
    val match = """\d{2,}""".toRegex().findAll(this).firstOrNull()
    return if (match == null) this else {
        val left = match.value.toInt() / 2
        val right = match.value.toInt() / 2 + match.value.toInt() % 2
        replaceRange(match.range, "[$left,$right]")
    }
}

fun String.reduce(): String {
    var start = this
    while (true) {
        val exploded = start.explode()
        if (exploded != start) {
            start = exploded
            continue
        }
        val split = start.split()
        if (split != start) {
            start = split
            continue
        }
        return start
    }
}

fun snailAdd(str1: String, str2: String) = "[$str1,$str2]".reduce()

val String.magnitude get() = generateSequence(this) { s ->
    s.replace("""\[(\d+),(\d+)]""".toRegex()) { match ->
        val (left, right) = match.groupValues.drop(1).map { it.toInt() }
        (3 * left + 2 * right).toString()
    }
}.first { """\d+""".toRegex() matches it }.toInt()

fun part1(input: List<String>) = input.reduce(::snailAdd).magnitude

fun part2(input: List<String>) = input.flatMap { i ->
    input.map { j -> i to j }
}.filter { it.first != it.second }.maxOf { snailAdd(it.first, it.second).magnitude }

fun main() {
    val subpackage = "day18"
    val input = readInput(subpackage, "input")
//    val input = readInput(subpackage, "test_input")
    val elapsed = measureTimeMillis {
        val result1 = part1(input)
        val result2 = part2(input)
        println("Part 1: $result1")
        println("Part 2: $result2")
    }
    println("Time elapsed: ${elapsed / 1000.0} seconds")
}
