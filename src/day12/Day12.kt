package day12

import readInput


class Passage(input: List<String>, val allowExtraCave: Boolean) {
    val edges = mutableMapOf<String, Set<String>>().withDefault { setOf() }.apply {
        input.map{ it.split("-") }.forEach { (a, b) ->
            put(a, getValue(a) + b)
            put(b, getValue(b) + a)
        }
    }

    val allPaths = search("start", listOf())

    fun search(curr: String, path: List<String>): List<List<String>> {
        val updatedPath = path + curr
        if (curr == "end") return listOf(updatedPath)
        return edges.getValue(curr).filterNot { next ->
            next == "start" ||
            next.isLower() && next in updatedPath &&
            if (allowExtraCave) {
                updatedPath.filter { it.isLower() }.groupingBy { it }.eachCount().values.any { it >= 2 }
            } else true
        }.flatMap { search(it, updatedPath) }
    }
}

fun String.isLower() = this.all { it.isLowerCase() }

fun part1(input: List<String>) = Passage(input, allowExtraCave = false).allPaths.size

fun part2(input: List<String>) = Passage(input, allowExtraCave = true).allPaths.size

fun main() {
    val subpackage = "day12"
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
