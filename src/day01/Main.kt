package day01

import readInput

fun part1(input: List<String>) = input.map { it.toInt() }.windowed(size=2, step=1).map { it[1] > it[0] }.count { it }

fun part2(input: List<String>) = input.map { it.toInt() }.windowed(size=4, step=1).map { it.takeLast(3).sum() > it.take(3).sum() }.count { it }

fun main() {
    val input = readInput("day01", "input")
    println(part1(input))
    println(part2(input))
}