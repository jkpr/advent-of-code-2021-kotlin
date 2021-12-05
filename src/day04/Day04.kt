package day04

import readInput


class Board(private val rows: List<List<String>>) {
    private val bingos = rows.map { it.toSet() } + rows[0].indices.map { rows.map{ line -> line[it] }.toSet() }
    private val entries = rows.flatten().toSet()

    fun hasBingo(called: Set<String>) = bingos.any { called.containsAll(it) }

    fun getUncrossed(called: Set<String>) = entries - called
}

fun getBoards(lines: List<String>): Map<Int, Board> {
    val boardStrings = lines.joinToString("\n").trim().split("\n\n")
    return boardStrings.mapIndexed{ index, board ->
        val rows = board.split("\n").map{ it.trim().split("""\s+""".toRegex())}
        index to Board(rows)
    }.toMap()
}

fun playBingo(nums: List<String>, boards: Map<Int, Board>): List<Pair<Int, Int>> {
    val winners = mutableListOf<Pair<Int, Int>>()
    nums.indices.forEach {
        val called = nums.take(it + 1).toSet()
        boards.forEach { (index, board) ->
            if (index !in winners.map { pair -> pair.first } && board.hasBingo(called)) {
                winners.add(index to it)
            }
        }
    }
    return winners
}

fun getFinalScore(input: List<String>, first: Boolean): Int {
    val nums = input[0].split(",")
    val boards = getBoards(input.drop(2))
    val results = playBingo(nums, boards)
    val winner = if (first) results.first() else results.last()
    val uncrossed =  boards.getValue(winner.first).getUncrossed(nums.take(winner.second + 1).toSet())
    return uncrossed.map { it.toInt() }.sum() * nums[winner.second].toInt()
}


fun part1(input: List<String>): Int {
    return getFinalScore(input, first = true)
}

fun part2(input: List<String>): Int {
    return getFinalScore(input, first = false)
}

fun main() {
    val subpackage = "day04"
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