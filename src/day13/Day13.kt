package day13

import readInput


data class Point(val x: Int, val y: Int)

data class Fold(val orientation: String, val value: Int) {
    fun makeFold(point: Point): Point {
        val pointValue = if (orientation == "x") point.x else point.y
        if (pointValue < value) return point.copy()
        val newValue = 2 * value - pointValue
        return if (orientation == "x") point.copy(x=newValue) else point.copy(y=newValue)
    }
}

data class Origami(val points: Set<Point>) {
    fun doFold(fold: Fold) = Origami(points.map { fold.makeFold(it) }.toSet())

    fun display() = buildString {
        append('\n')
        for (y in 0..points.maxOf { it.y }) {
            for (x in 0..points.maxOf { it.x }) {
                append(if (Point(x, y) in points) '#' else ' ')
            }
            append('\n')
        }
    }
}

fun setup(input: List<String>): Pair<Origami, List<Fold>> {
    val points = input.filter { ',' in it }.map { it.split(",") }.map { Point(it[0].toInt(), it[1].toInt()) }.toSet()
    val folds = input.filter { '=' in it }.map { it.split(" ").last().split("=") }.map { Fold(it[0], it[1].toInt()) }
    return Origami(points) to folds
}

fun part1(input: List<String>) = setup(input).let { (origami, folds) ->
    origami.doFold(folds[0]).points.size
}

fun part2(input: List<String>) = setup(input).let { (origami, folds) ->
    folds.fold(origami) { next, fold -> next.doFold(fold) }.display()
}

fun main() {
    val subpackage = "day13"
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
