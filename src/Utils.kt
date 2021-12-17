import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readInput(subpackage: String, name: String) = File("src/$subpackage", "$name.txt").readLines()

/**
 * Return the cross product of two integer ranges
 */
operator fun IntRange.times(other: IntRange) = this.flatMap { i -> other.map { j -> i to j }}