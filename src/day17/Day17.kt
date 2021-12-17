package day17

import utils.readInput
import utils.times


typealias Point = Pair<Int, Int>
typealias Velocity = Pair<Int, Int>
typealias Trajectory = List<Point>

val Point.x get() = first
val Point.y get() = second

data class Target(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int) {
    val velocitySearchSpace = run {
        val dxMin = generateSequence(1) { it + 1 }.first { it.sumN() >= xMin }
        val dxMax = xMax
        val dyMin = yMin
        val dyMax = -yMin
        (dxMin..dxMax) * (dyMin..dyMax)
    }

    val trajectoryHits = velocitySearchSpace.map { velocity ->
        trajectorySequenceFrom(velocity).takeWhile { point -> point notBeyond this }.toList()
    }.filter { trajectory -> trajectory endsIn this }

    operator fun contains(point: Point) = point.x in xMin..xMax && point.y in yMin..yMax
}


fun List<Int>.toTarget() = Target(get(0), get(1), get(2), get(3))

fun getTarget(line: String) = """-?\d+""".toRegex().findAll(line).map { it.value.toInt() }.toList().toTarget()

infix fun Point.notBeyond(target: Target) = x <= target.xMax && y >= target.yMin

infix fun Trajectory.endsIn(target: Target) = last() in target

/**
 * Sum of 1, 2, ... , N=this
 */
fun Int.sumN() = this * (this + 1) / 2

/**
 * Sum of x=this, x-1, x-2, ... , x-n
 */
fun Int.sumDesc(n: Int) = n * this - (n - 1).sumN()

fun trajectorySequenceFrom(vel: Velocity) = generateSequence(1) { it + 1 }.map { n ->
    val x = when {
        n < vel.x -> vel.x.sumDesc(n)
        else -> vel.x.sumN()
    }
    val y = vel.y.sumDesc(n)
    x to y
}

fun Trajectory.maxY() = maxOf { it.y }

fun part1(input: List<String>) = (-getTarget(input[0]).yMin - 1).sumN()

fun part2(input: List<String>) = getTarget(input[0]).trajectoryHits.size

fun main() {
    val subpackage = "day17"
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
