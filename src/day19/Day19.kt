package day19

import kotlin.math.abs
import kotlin.system.measureTimeMillis
import utils.readInput


data class XYZ(val x: Int, val y: Int, val z: Int) {
    operator fun plus(other: XYZ) = XYZ(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: XYZ) = XYZ(x - other.x, y - other.y, z - other.z)
    operator fun unaryMinus() = XYZ(-x, -y, -z)
    fun xRotate() = XYZ(x, -z, y)
    fun yRotate() = XYZ(-z, y, x)
    fun zRotate() = XYZ(-y, x, z)
    infix fun manhattan(other: XYZ) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
}

fun List<XYZ>.diffs() = zipWithNext { a, b -> b - a }

data class BeaconsAndScanners(val beacons: Set<XYZ>, val scanners: Set<XYZ>) {
    val maxScannerManhattanDistance get() = scanners.maxOf { a -> scanners.maxOf { b -> a manhattan b} }
}

fun allRotations() = iterator {
    val reference = listOf(
        XYZ(1, 0 ,0),
        XYZ(0, 1 ,0),
        XYZ(0, 0 ,1),
    )
    val seen = mutableSetOf<List<XYZ>>()
    for (i in 0..3) for (j in 0..3) for (k in 0..3) {
        val rotate = { point: XYZ ->
            var xyz = point
            repeat(i) { xyz = xyz.xRotate() }
            repeat(j) { xyz = xyz.yRotate() }
            repeat(k) { xyz = xyz.zRotate() }
            xyz
        }
        val rotatedReference = reference.map{ rotate(it) }
        if (rotatedReference !in seen) {
            seen.add(rotatedReference)
            yield(rotate)
        }
    }
}

fun attemptAlignment(knownBeacons: Set<XYZ>, rotatedBeacons: List<XYZ>): BeaconsAndScanners? {
    for (field in listOf(compareBy<XYZ> { it.x }, compareBy { it.y }, compareBy { it.z })) {
        val knownSorted = knownBeacons.sortedWith(field)
        val unknownSorted = rotatedBeacons.sortedWith(field)
        val knownDiffs = knownSorted.diffs()
        val unknownDiffs = unknownSorted.diffs()
        for (item in knownDiffs.toSet().intersect(unknownDiffs.toSet())) {
            val knownPoint = knownSorted[knownDiffs.indexOf(item)]
            val unknownPoint = unknownSorted[unknownDiffs.indexOf(item)]
            val diff = unknownPoint - knownPoint
            val alignedBeacons = rotatedBeacons.map { it - diff }.toSet()
            if (alignedBeacons.intersect(knownBeacons).size >= 12) {
                val scanner = -diff
                return BeaconsAndScanners(alignedBeacons, setOf(scanner))
            }
        }
    }
    return null
}

fun findAlignedBeacons(knownBeacons: Set<XYZ>, allUnknownScanners: List<List<XYZ>>): IndexedValue<BeaconsAndScanners> {
    for ((index, unknownScanners) in allUnknownScanners.withIndex()) for (rotate in allRotations()) {
        val rotatedBeacons = unknownScanners.map { point -> rotate(point) }
        attemptAlignment(knownBeacons, rotatedBeacons)?.let {
            return@findAlignedBeacons IndexedValue(index, it)
        }
    }
    throw Exception("Unable to find contiguous scanners")
}

fun getAllScannerReadings(input: List<String>) = input.joinToString("\n").split("\n\n").map { chunk ->
    chunk.split("\n").drop(1).map {
        val (x, y, z) = """-?\d+""".toRegex().findAll(it).map { m -> m.value.toInt() }.toList()
        XYZ(x, y, z)
    }
}

fun alignBeaconsAndScanners(input: List<String>): BeaconsAndScanners {
    val allScannerReadings = getAllScannerReadings(input)
    val knownBeacons = allScannerReadings.first().toMutableSet()
    val unknownScannerReadings = allScannerReadings.drop(1).toMutableList()
    val knownScanners = mutableSetOf(XYZ(0, 0, 0))
    repeat(unknownScannerReadings.size) {
        val (index, beaconsAndScanners) = findAlignedBeacons(knownBeacons, unknownScannerReadings)
        unknownScannerReadings.removeAt(index)
        knownBeacons.addAll(beaconsAndScanners.beacons)
        knownScanners.addAll(beaconsAndScanners.scanners)
    }
    return BeaconsAndScanners(knownBeacons, knownScanners)
}

fun part1(input: List<String>) = alignBeaconsAndScanners(input).beacons.size

fun part2(input: List<String>) = alignBeaconsAndScanners(input).maxScannerManhattanDistance

fun main() {
    val subpackage = "day19"
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
