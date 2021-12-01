import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readInput(subpackage: String, name: String) = File("src/$subpackage", "$name.txt").readLines()