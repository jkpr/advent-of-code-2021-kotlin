package day22

import kotlin.math.max
import kotlin.math.min
import utils.readInput


data class Cuboid(val x0: Long, val x1: Long, val y0: Long, val y1: Long, val z0: Long, val z1: Long) {
    val volume get() = (x1 - x0 + 1) * (y1 - y0 + 1) * (z1 - z0 + 1)

    fun isProper() = x0 <= x1 && y0 <= y1 && z0 <= z1

    infix fun intersect(obj: Cuboid): Cuboid? {
        val c = Cuboid(max(x0, obj.x0), min(x1, obj.x1), max(y0, obj.y0), min(y1, obj.y1), max(z0, obj.z0), min(z1, obj.z1))
        return if (c.isProper()) c else null
    }

    operator fun minus(obj: Cuboid): List<Cuboid> {
        val i = this intersect obj
        if (i == null) return listOf(this)
        else if (i == this) return emptyList()
        val toReturn = mutableListOf<Cuboid>()
        if (x0 < i.x0) {
            toReturn.add(this.copy(x1 = i.x0 - 1))
        }
        if (x1 > i.x1) {
            toReturn.add(this.copy(x0 = i.x1 + 1))
        }
        if (y0 < i.y0) {
            toReturn.add(this.copy(x0 = i.x0, x1 = i.x1, y1 = i.y0 - 1))
        }
        if (y1 > i.y1) {
            toReturn.add(this.copy(x0 = i.x0, x1 = i.x1, y0 = i.y1 + 1))
        }
        if (z0 < i.z0) {
            toReturn.add(i.copy(z0 = z0, z1 = i.z0 - 1))
        }
        if (z1 > i.z0) {
            toReturn.add(i.copy(z0 = i.z1 + 1, z1 = z1))
        }
        return toReturn.toList()
    }
}

fun makeCuboid(line: String): Cuboid {
    val values = """-?\d+""".toRegex().findAll(line).map { it.value.toLong() }.toList()
    return Cuboid(values[0], values[1], values[2], values[3], values[4], values[5])
}

val List<Cuboid>.volume get() = sumOf { it.volume }
operator fun List<Cuboid>.minus(cuboid: Cuboid) = flatMap { it - cuboid }
fun List<Cuboid>.intersect(cuboid: Cuboid) = mapNotNull { it intersect cuboid }

fun getCuboids(input: List<String>): List<Cuboid> {
    var cuboids = listOf<Cuboid>()
    input.forEach { line ->
        val cuboid = makeCuboid(line)
        cuboids = cuboids - cuboid
        if (line.startsWith("on")) cuboids += cuboid
    }
    return cuboids
}

val initializationProcedureCuboid = Cuboid(-50, 50, -50, 50, -50, 50)

fun part1(input: List<String>) = getCuboids(input).intersect(initializationProcedureCuboid).volume

fun part2(input: List<String>) = getCuboids(input).volume

fun main() {
    val subpackage = "day22"
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
