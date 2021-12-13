package day12

import readInput


class Passage(input: List<String>, val allowExtraCave: Boolean) {
    val edges = mutableMapOf<String, Set<String>>().withDefault { setOf() }.apply {
        input.map{ it.split("-") }.forEach { (a, b) ->
            put(a, getValue(a) + b)
            put(b, getValue(b) + a)
        }
    }

    val allPaths = search(listOf("start"))

    fun search(path: List<String>): List<List<String>> {
        val curr = path.last()
        if (curr == "end") return listOf(path)
        return edges.getValue(curr).filterNot { next ->
            next == "start" ||
            next.isLower() && next in path &&
            if (allowExtraCave) {
                path.filter { it.isLower() }.groupingBy { it }.eachCount().values.any { it > 1 }
            } else true
        }.flatMap { search(path + it) }
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
