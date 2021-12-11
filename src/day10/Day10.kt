package day10

import readInput


val matches = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>'
)

val illegalCharPoints = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137,
)

val completionCharPoints = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4,
)

fun String.firstIllegalCharOrNull(): Char? {
    val stack = mutableListOf<Char>()
    this.forEach {
        if (it in "([{<") stack.add(it)
        else if (stack.isNotEmpty() && matches.getValue(stack.removeLast()) != it) return it
    }
    return null
}

fun String.completionString(): String {
    val stack = mutableListOf<Char>()
    this.forEach {
        if (it in "([{<") stack.add(it)
        else if (stack.isNotEmpty()) stack.removeLast()
    }
    return buildString {
        while( stack.isNotEmpty() ) {
            append(matches.getValue(stack.removeLast()))
        }
    }
}

fun String.completionScore() = this.fold(0L) { score, char -> score * 5 + completionCharPoints.getValue(char) }

fun <E> List<E>.middle(): E = this[size.div(2)]

fun part1(input: List<String>) = input.mapNotNull { it.firstIllegalCharOrNull() }.sumOf { illegalCharPoints.getValue(it) }

fun part2(input: List<String>) = input.filter { it.firstIllegalCharOrNull() == null }.map { it.completionString().completionScore() }.sorted().middle()

fun main() {
    val subpackage = "day10"
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
