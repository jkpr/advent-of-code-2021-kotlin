package day23

import kotlin.system.measureTimeMillis
import utils.readInput
import java.util.*


/**
 * When treating an integer as an amphipod
 * A = 1
 * B = 2
 * C = 3
 * D = 4
 */
fun Int.homeRoom() = this - 1
fun Int.energy() = when (this) {
    1 -> 1
    2 -> 10
    3 -> 100
    4 -> 1000
    else -> throw Exception("This is not an amphipod!")
}

fun Int.toDiagram() = when (this) {
    0 -> '.'
    1 -> 'A'
    2 -> 'B'
    3 -> 'C'
    4 -> 'D'
    else -> throw Exception("This isn't a 0 or 1-4, so it shouldn't be here!")
}

fun Char.fromDiagram() = when (this) {
    'A' -> 1
    'B' -> 2
    'C' -> 3
    'D' -> 4
    else -> throw Exception("Only A-D can be an amphipod!")
}

/**
 * When treating an integer as a room
 */
fun Int.correctAmphipod() = this + 1

typealias Energy = Int
typealias Squares = List<Int>
typealias EnergyAndSquares = Pair<Energy, Squares>

//  --------------------------------------------------------
//  |  0 |  1 |    |  2 |    |  3 |    |  4 |    |  5 |  6 |
//  --------------------------------------------------------
//            |  7 |    | 11 |    | 15 |    | 19 |
//            ------    ------    ------    ------
//            |  8 |    | 12 |    | 16 |    | 20 |
//            ------    ------    ------    ------
//            |  9 |    | 13 |    | 17 |    | 21 |
//            ------    ------    ------    ------
//            | 10 |    | 14 |    | 18 |    | 22 |
//            ------    ------    ------    ------
//
//  ROOM:        0         1         2         3

const val LEN_HALL = 7
const val NUM_ROOMS = 4
const val EMPTY_SQUARE = 0

val Squares.hall get() = subList(0, LEN_HALL)
val Squares.roomDepth get() = (size - LEN_HALL) / NUM_ROOMS

fun Squares.getRoom(room: Int) = subList(LEN_HALL + room * roomDepth, LEN_HALL + (room + 1) * roomDepth)

fun Squares.roomIndexToSquare(room: Int, index: Int) = LEN_HALL + room * roomDepth + index

fun Squares.areSquaresClear(squares: List<Int>) = squares.all { get(it) == EMPTY_SQUARE }

fun Squares.move(index1: Int, value1: Int, index2: Int, value2: Int) = toMutableList().apply {
    set(index1, value1)
    set(index2, value2)
}.toList()

fun Squares.nextOpenRoomIndex(room: Int): Int {
    val roomSquares = getRoom(room)
    return if (roomSquares.all { it == EMPTY_SQUARE || it == room.correctAmphipod() })
        roomSquares.count { it == EMPTY_SQUARE } - 1
    else
        -1
}

fun Squares.roomMovers() = iterator {
    (0 until NUM_ROOMS).associateWith { room ->
        getRoom(room).withIndex().firstOrNull { it.value != EMPTY_SQUARE }
    }.forEach { (room, indexedValue) ->
        if (indexedValue != null) yield(Triple(room, indexedValue.index, indexedValue.value))
    }
}

fun Squares.enterRoomFromHall(): List<EnergyAndSquares>? {
    for ((square, mover) in hall.withIndex().filter { it.value != EMPTY_SQUARE }) {
        val hallToRoomData = hallToRoom.getValue(square to mover.homeRoom())
        val nextOpenRoomIndex = nextOpenRoomIndex(mover.homeRoom())
        if (areSquaresClear(hallToRoomData.squaresCrossed) && nextOpenRoomIndex >= 0) {
            val energy = mover.energy() * (hallToRoomData.distance + nextOpenRoomIndex)
            val nextSquares = move(
                square,
                EMPTY_SQUARE,
                roomIndexToSquare(mover.homeRoom(), nextOpenRoomIndex),
                mover
            )
            return listOf(energy to nextSquares)
        }
    }
    return null
}

fun Squares.enterRoomFromRoom(): List<EnergyAndSquares>? {
    for ((room, roomIndex, mover) in roomMovers()) {
        if (mover.homeRoom() != room) {
            val roomToRoomData = roomToRoom.getValue(room to mover.homeRoom())
            val nextOpenRoomIndex = nextOpenRoomIndex(mover.homeRoom())
            if (areSquaresClear(roomToRoomData.squaresCrossed) && nextOpenRoomIndex >= 0) {
                val energy = mover.energy() * (roomToRoomData.distance + roomIndex + nextOpenRoomIndex)
                val next = move(
                    roomIndexToSquare(room, roomIndex),
                    EMPTY_SQUARE,
                    roomIndexToSquare(mover.homeRoom(), nextOpenRoomIndex),
                    mover
                )
                return listOf(energy to next)
            }
        }
    }
    return null
}

fun Squares.enterHallFromRoom(): List<EnergyAndSquares> = buildList {
    for ((room, roomIndex, mover) in roomMovers()) for ((square, _) in hall.withIndex()
        .filter { it.value == EMPTY_SQUARE }) {
        val hallToRoomData = hallToRoom.getValue(square to room)
        if (areSquaresClear(hallToRoomData.squaresCrossed)) {
            val energy = mover.energy() * (hallToRoomData.distance + roomIndex)
            val next = move(roomIndexToSquare(room, roomIndex), EMPTY_SQUARE, square, mover)
            add(energy to next)
        }
    }
}

fun Squares.next() = enterRoomFromRoom() ?: enterRoomFromHall() ?: enterHallFromRoom()

fun Squares.endConfiguration() = List(size) {
    if (it in 0 until LEN_HALL) EMPTY_SQUARE else (it - LEN_HALL) / roomDepth + 1
}

/**
 * Dijkstra's search algorithm
 */
fun Squares.calculateEnergyToSolve(): Int {
    val start = this.toList()  // I'm not sure why, but adding .toList() speeds up the code by 15%
    val end = endConfiguration()

    val cameFrom = mutableMapOf(start to start)
    val costSoFar = mutableMapOf(start to 0)

    val queue = PriorityQueue<EnergyAndSquares>(compareBy { it.first }).apply { add(0 to start) }

    while (queue.isNotEmpty()) {
        val current = queue.poll().second
        if (current == end) {
            break
        }
        for ((energy, squares) in current.next()) {
            val newCost = costSoFar.getValue(current) + energy
            if (squares !in costSoFar || newCost < costSoFar.getValue(squares)) {
                costSoFar[squares] = newCost
                cameFrom[squares] = current
                queue.add(newCost to squares)
            }
        }
    }
    return costSoFar.getValue(end)
}

fun Squares.toFullDiagram() = StringBuilder().also { sb ->
    repeat(LEN_HALL + 6) { sb.append("#") }
    sb.append("\n")
    val h = hall.map { it.toDiagram() }
    sb.append("#${h[0]}${h[1]}.${h[2]}.${h[3]}.${h[4]}.${h[5]}${h[6]}#\n")
    for (roomIndex in 0 until roomDepth) {
        val roomLevel = (0 until NUM_ROOMS).map { get(roomIndexToSquare(it, roomIndex)).toDiagram() }.joinToString("#")
        if (roomIndex == 0) {
            sb.append("###$roomLevel###\n")
        } else {
            sb.append("  #$roomLevel#\n")
        }
    }
    sb.append("  #########\n")
}

data class HallToRoomData(val hall: Int, val room: Int, val distance: Int, val squaresCrossed: List<Int>)

data class RoomToRoomData(val startRoom: Int, val endRoom: Int, val distance: Int, val squaresCrossed: List<Int>)

val hallToRoom = listOf(
    HallToRoomData(0, 0, 3, listOf(1)),
    HallToRoomData(0, 1, 5, listOf(1, 2)),
    HallToRoomData(0, 2, 7, listOf(1, 2, 3)),
    HallToRoomData(0, 3, 9, listOf(1, 2, 3, 4)),
    HallToRoomData(1, 0, 2, listOf()),
    HallToRoomData(1, 1, 4, listOf(2)),
    HallToRoomData(1, 2, 6, listOf(2, 3)),
    HallToRoomData(1, 3, 8, listOf(2, 3, 4)),
    HallToRoomData(2, 0, 2, listOf()),
    HallToRoomData(2, 1, 2, listOf()),
    HallToRoomData(2, 2, 4, listOf(3)),
    HallToRoomData(2, 3, 6, listOf(3, 4)),
    HallToRoomData(3, 0, 4, listOf(2)),
    HallToRoomData(3, 1, 2, listOf()),
    HallToRoomData(3, 2, 2, listOf()),
    HallToRoomData(3, 3, 4, listOf(4)),
    HallToRoomData(4, 0, 6, listOf(2, 3)),
    HallToRoomData(4, 1, 4, listOf(3)),
    HallToRoomData(4, 2, 2, listOf()),
    HallToRoomData(4, 3, 2, listOf()),
    HallToRoomData(5, 0, 8, listOf(2, 3, 4)),
    HallToRoomData(5, 1, 6, listOf(3, 4)),
    HallToRoomData(5, 2, 4, listOf(4)),
    HallToRoomData(5, 3, 2, listOf()),
    HallToRoomData(6, 0, 9, listOf(2, 3, 4, 5)),
    HallToRoomData(6, 1, 7, listOf(3, 4, 5)),
    HallToRoomData(6, 2, 5, listOf(4, 5)),
    HallToRoomData(6, 3, 3, listOf(5)),
).associateBy { it.hall to it.room }

val roomToRoom = listOf(
    RoomToRoomData(0, 1, 4, listOf(2)),
    RoomToRoomData(0, 2, 6, listOf(2, 3)),
    RoomToRoomData(0, 3, 8, listOf(2, 3, 4)),
    RoomToRoomData(1, 0, 4, listOf(2)),
    RoomToRoomData(1, 2, 4, listOf(3)),
    RoomToRoomData(1, 3, 6, listOf(3, 4)),
    RoomToRoomData(2, 0, 6, listOf(2, 3)),
    RoomToRoomData(2, 1, 4, listOf(3)),
    RoomToRoomData(2, 3, 4, listOf(4)),
    RoomToRoomData(3, 0, 8, listOf(2, 3, 4)),
    RoomToRoomData(3, 1, 6, listOf(3, 4)),
    RoomToRoomData(3, 2, 4, listOf(4)),
).associateBy { it.startRoom to it.endRoom }

fun getInitialState(input: List<String>, insert: String? = null) = buildList {
    repeat(LEN_HALL) { add(EMPTY_SQUARE) }
    val start = mutableListOf(input[2])
    if (insert != null) {
        start.addAll(insert.trim().split("\n"))
    }
    start.add(input[3])
    val starters = start.map { line -> line.filter { it in 'A'..'D' } }
    for (i in 0 until NUM_ROOMS) starters.forEach { add(it[i].fromDiagram()) }
}

val insert = """
  #D#C#B#A#
  #D#B#A#C#
"""

fun part1(input: List<String>) = getInitialState(input).calculateEnergyToSolve()

fun part2(input: List<String>) = getInitialState(input, insert = insert).calculateEnergyToSolve()

fun main() {
    val subpackage = "day23"
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
