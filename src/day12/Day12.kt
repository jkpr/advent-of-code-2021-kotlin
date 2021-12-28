package day12

import utils.readInput


typealias Path = List<String>

class Passage(input: List<String>, val allowRevisit: Boolean) {
    val edges = mutableMapOf<String, Set<String>>().withDefault { setOf() }.apply {
        input.map{ it.split("-") }.forEach { (a, b) ->
            put(a, getValue(a) + b)
            put(b, getValue(b) + a)
        }
    }

    val allPaths = search(listOf("start"))

    /**
     * Recursive function that returns a list of all paths starting at the tip of the current
     * path and ending at "end".
     */
    fun search(path: Path, revisited: Boolean = false): List<Path> {
        val curr = path.last()
        if (curr == "end") return listOf(path)
        return edges.getValue(curr).filterNot {
            it == "start" || it.isLower() && it in path && if (allowRevisit) revisited else true
        }.flatMap {
            search(path + it, revisited || it.isLower() && it in path)
        }
    }
}

fun String.isLower() = all { it.isLowerCase() }

fun part1(input: List<String>) = Passage(input, allowRevisit = false).allPaths.size

fun part2(input: List<String>) = Passage(input, allowRevisit = true).allPaths.size

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
