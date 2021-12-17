package day16

import java.util.LinkedList
import utils.readInput



data class Literal(val start: Int, val end: Int, val value: Long)

fun literalFromTape(tape: String, start: Int): Literal {
    val bits = mutableListOf<String>()
    var i = start
    while (i <= tape.length) {
        val frame = tape.substring(i, i + 5)
        bits.add(frame.substring(1))
        i += 5
        if (frame[0] == '0') break
    }
    return Literal(start, i, bits.joinToString("").toLong(2))
}

data class Header(val start: Int, val end: Int, val packetVersion: Int, val packetType: Int, val lengthType: Char?, val lengthValue: Int?)

fun headerFromTape(tape: String, start: Int): Header {
    var i = start
    val packetVersion = tape.substring(i, i + 3).toInt(2)
    val packetType = tape.substring(i + 3, i + 6).toInt(2)
    i += 6
    var lengthType: Char? = null
    var lengthValue: Int? = null
    if (packetType != 4) {
        lengthType = tape[i]
        i += 1
        if (lengthType == '0') {
            lengthValue = tape.substring(i, i + 15).toInt(2)
            i += 15
        } else {
            lengthValue = tape.substring(i, i + 11).toInt(2)
            i += 11
        }
    }
    return Header(start, i, packetVersion, packetType, lengthType, lengthValue)
}

data class Packet(val start: Int, val end: Int, val header: Header, val literal: Literal?, val subpackets: List<Packet>)

fun Packet.eval(): Long {
    if (literal != null) return literal.value
    return when(header.packetType) {
        0 -> subpackets.sumOf { it.eval() }
        1 -> subpackets.fold(1) { acc, b -> acc * b.eval() }
        2 -> subpackets.minOf { it.eval() }
        3 -> subpackets.maxOf { it.eval() }
        5 -> if (subpackets[0].eval() > subpackets[1].eval()) 1 else 0
        6 -> if (subpackets[0].eval() < subpackets[1].eval()) 1 else 0
        7 -> if (subpackets[0].eval() == subpackets[1].eval()) 1 else 0
        else -> throw Exception()
    }
}

fun packetFromTape(tape: String, start: Int): Packet {
    var i = start
    val header = headerFromTape(tape, i)
    i = header.end
    var literal: Literal? = null
    var subpackets: List<Packet> = emptyList()
    if (header.packetType == 4) {
        literal = literalFromTape(tape, i)
        i = literal.end
    } else if (header.lengthType == '0' && header.lengthValue != null) {
        val until = i + header.lengthValue
        subpackets = packetFromTapeUntil(tape, i, until)
        i = until
    } else if (header.lengthType == '1' && header.lengthValue != null) {
        val count = header.lengthValue
        subpackets = packetFromTapeCount(tape, i, count)
        i = subpackets.last().end
    }
    return Packet(start, i, header, literal, subpackets)
}

fun packetFromTapeUntil(tape: String, start: Int, until: Int) = mutableListOf<Packet>().apply {
    var i = start
    while (i < until) {
        val packet = packetFromTape(tape, i)
        add(packet)
        i = packet.end
    }
}

fun packetFromTapeCount(tape: String, start: Int, count: Int) = mutableListOf<Packet>().apply {
    var i = start
    repeat(count) {
        val packet = packetFromTape(tape, i)
        add(packet)
        i = packet.end
    }
}

fun String.hexToBin() = this.map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")

fun part1(input: List<String>) = input[0].hexToBin().let { tape ->
    val packet = packetFromTape(tape, start=0)
    val to_sum = LinkedList<Packet>()
    to_sum.add(packet)
    var sum = 0
    while (to_sum.isNotEmpty()) {
        val next = to_sum.pop()
        sum += next.header.packetVersion
        to_sum.addAll(next.subpackets)
    }
    sum
}

fun part2(input: List<String>) = input[0].hexToBin().let { tape ->
    val packet = packetFromTape(tape, start=0)
    packet.eval()
}

fun main() {
    val subpackage = "day16"
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
