package day15

import kotlin.math.abs
import readInput
import java.util.PriorityQueue


typealias Node = Pair<Int, Int>

data class Tile(val riskMap: Map<Node, Int>) {
    val start = riskMap.minOf { it.key.first } to riskMap.minOf { it.key.second }
    val end = riskMap.maxOf { it.key.first } to riskMap.maxOf { it.key.second }

    fun neighbors(node: Node) = ((-1..1) * (-1..1)).filter { (abs(it.first) + abs(it.second)) == 1 }.map {
        (it.first + node.first) to (it.second + node.second)
    }.filter { it in riskMap }

    fun translate(i: Int, j: Int): Tile {
        val di = end.first - start.first + 1
        val dj = end.second - start.second + 1
        return riskMap.map { (node, risk) ->
            val newNode = (node.first + i * di) to (node.second + j * dj)
            val newRisk = (risk + i + j - 1) % 9 + 1
            newNode to newRisk
        }.toMap().let { Tile(it) }
    }

    operator fun plus(other: Tile) = Tile(riskMap + other.riskMap)
}

fun buildTile(input: List<String>, extended: Boolean = false): Tile {
    val rows = input.indices
    val cols = input[0].indices
    val riskMap = (rows * cols).associateWith { input[it.first][it.second].toString().toInt() }
    val tile = Tile(riskMap)
    return if (extended) {
        ((0..4) * (0..4)).map { tile.translate(it.first, it.second) }.reduce { t1, t2 -> t1 + t2 }
    } else {
        tile
    }
}

class WeightedDiGraph<E> {
    val nodes = mutableSetOf<E>()
    val edges = mutableMapOf<E, MutableMap<E, Int>>().withDefault { mutableMapOf() }

    fun addEdge(start: E, end: E, weight: Int) {
        val theseEdges = edges.getValue(start)
        theseEdges[end] = weight
        edges[start] = theseEdges
        nodes.add(start)
        nodes.add(end)
    }

    operator fun get(item: E) = edges.getValue(item).keys
    operator fun get(a: E, b: E) = edges.getValue(a).getValue(b)
}

fun buildGraph(tile: Tile) = WeightedDiGraph<Node>().apply {
    tile.riskMap.forEach { (node, risk) ->
        tile.neighbors(node).forEach {
            addEdge(it, node, risk)
        }
    }
}

fun dijkstraPathWeight(graph: WeightedDiGraph<Node>, start: Node, end: Node): Int {
    val cameFrom = mutableMapOf(start to start)
    val costSoFar = mutableMapOf(start to 0)
    val queue = PriorityQueue<Pair<Int, Node>>(compareBy { -it.first }).apply { add(0 to start) }
    while (queue.isNotEmpty()) {
        val current = queue.poll().second
        if (current == end) {
            break
        }
        for (next in graph[current]) {
            val newCost = costSoFar.getValue(current) + graph[current, next]
            if (next !in costSoFar || newCost < costSoFar.getValue(next)) {
                costSoFar[next] = newCost
                cameFrom[next] = current
                queue.add(-newCost to next)
            }
        }
    }
    return costSoFar.getValue(end)
}

operator fun IntRange.times(other: IntRange) = this.flatMap { i -> other.map { j -> i to j }}

fun part1(input: List<String>) = buildTile(input).let {
    val graph = buildGraph(it)
    dijkstraPathWeight(graph, it.start, it.end)
}

fun part2(input: List<String>) = buildTile(input, extended = true).let {
    val graph = buildGraph(it)
    dijkstraPathWeight(graph, it.start, it.end)
}

fun main() {
    val subpackage = "day15"
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
