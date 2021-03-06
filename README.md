# Advent of Code 2021 in Kotlin

These are my solutions to Advent of Code 2021 using the Kotlin language. Each day has its own subpackage.

Try the problems yourself at [https://adventofcode.com/2021/](https://adventofcode.com/2021/).

Feel free to create a Github issue if you want to discuss anything!

# Usage

1. Clone this repo: `git clone https://github.com/jkpr/advent-of-code-2021-kotlin`
2. Open in IntelliJ IDEA
3. Copy your input for `dayN` to `input.txt` inside of `dayN` package (`src/dayN/input.txt`).

# Table of contents

| `Day % 5 == 0`                                | `Day % 5 == 1`                                | `Day % 5 == 2`                                | `Day % 5 == 3`                                | `Day % 5 == 4`                                |
|-----------------------------------------------|-----------------------------------------------|-----------------------------------------------|-----------------------------------------------|-----------------------------------------------|
|                                               | 1 · [_notes_](#day-1) · [_code_](src/day01)   | 2 · [_notes_](#day-2) · [_code_](src/day02)   | 3 · [_notes_](#day-3) · [_code_](src/day03)   | 4 · [_notes_](#day-4) · [_code_](src/day04)   |
| 5 · [_notes_](#day-5) · [_code_](src/day05)   | 6 · [_notes_](#day-6) · [_code_](src/day06)   | 7 · [_notes_](#day-7) · [_code_](src/day07)   | 8 · [_notes_](#day-8) · [_code_](src/day08)   | 9 · [_notes_](#day-9) · [_code_](src/day09)   |
| 10 · [_notes_](#day-10) · [_code_](src/day10) | 11 · [_notes_](#day-11) · [_code_](src/day11) | 12 · [_notes_](#day-12) · [_code_](src/day12) | 13 · [_notes_](#day-13) · [_code_](src/day13) | 14 · [_notes_](#day-14) · [_code_](src/day14) |
| 15 · [_notes_](#day-15) · [_code_](src/day15) | 16 · [_notes_](#day-16) · [_code_](src/day16) | 17 · [_notes_](#day-17) · [_code_](src/day17) | 18 · [_notes_](#day-18) · [_code_](src/day18) | 19 · [_notes_](#day-19) · [_code_](src/day19) |
| 20 · [_notes_](#day-20) · [_code_](src/day20) | 21 · [_notes_](#day-21) · [_code_](src/day21) | 22 · [_notes_](#day-22) · [_code_](src/day22) | 23 · [_notes_](#day-23) · [_code_](src/day23) | 24 · [_notes_](#day-24) · [_code_](src/day24) |
| 25 · [_notes_](#day-25) · [_code_](src/day25) |                                               |                                               |                                               |                                               |

# Day 1

We essentially need to iterate with a window over the list.
See [windowed][1a] in the docs. This leads to one-line solutions for both parts.

In general, see this [page on retrieving collection parts][1b].

Kotlin's generous standard library for the win.

[1a]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/windowed.html
[1b]: https://kotlinlang.org/docs/collection-parts.html

# Day 2

Dusting off the trusty [when][2a] construct. It often looks cleaner than `if` / `else if` / `else`.

In the case of the code here for Day 2, an `else` branch is not needed because `when` is not used as an expression.

[2a]: https://kotlinlang.org/docs/control-flow.html#when-expression

# Day 3

For part 1, a scope function [with][3a] is used for the first time. This is my favorite way to use a `StringBuilder`:

```kotlin
with(StringBuilder()) {
    append("Hello, ")
    append("World!")
    toString()
}
```

**_EDIT:_** Apparently there is a synonym of the above: [`buildString`][3b].
The above can be replaced with:

```kotlin
buildString {
    append("Hello, ")
    append("World!")
}
```

(note that `toString()` is called for us).

For part 2, an [extension function][3c] is used for the first time.
This simplifies things by breaking the code across different places while allowing expressive code.

Both *scope functions* and *extension functions* are some of the best features of Kotlin! ❤️

[3a]: https://kotlinlang.org/docs/scope-functions.html#with
[3b]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/build-string.html
[3c]: https://kotlinlang.org/docs/extensions.html

# Day 4

For the first time, a `class` was used to represent the board (bingo card). A board is a list of rows, and a row is a list of strings.

Two data structures are built in the Board class:

- All bingos for the board, made of the rows and columns, each as a set of Strings.
- All entries on the board as a set.

When comparing against the set of all called numbers, it is easy to check for bingo with

```kotlin
fun hasBingo(called: Set<String>) = bingos.any { called.containsAll(it) }
```

and get all uncrossed numbers with

```kotlin
fun getUncrossed(called: Set<String>) = entries - called
```

We can break up the input on new lines into chunks with:

```kotlin
fun getChunks(input: List<str>) = input.joinToString("\n").trim().split("\n\n")
```

Then from there, split each chunk on new lines to get rows again.

# Day 5

Today, we continue to explore Kotlin's collection methods in the standard library with [`flatMap`][5a].
For each line in the input, we get a list of all grid points that the line crosses. `flatMap` combines all of those lists into a single list.

A few more notes. To use [regex][5b] in Kotlin, an easy way to do this is to convert a [raw string][5c] to a regex object, for example:

```kotlin
"""\d+""".toRegex().findAll(line)
```

[5a]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/flat-map.html
[5b]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/
[5c]: https://kotlinlang.org/docs/basic-types.html#string-literals

# Day 6

My template for solutions return integers. 
So when the results were coming back negative, it was not hard to figure out we were dealing with numbers larger than [`Int.MAX_VALUE`][6a].
(On my machine, that happens to be 2147483647 = 2^31 - 1).

The great thing about the Kotlin compiler being able to infer types is that this change only required one function signature change.

I built a map of days left to spawn as keys with counts of lanternfish as values.

In this solution, I got to use [`Map.mapValues`][6b] during set up to convert integer counts to long counts and [`Map.mapKeys`][6c] to decrease the counters.

[6a]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/-m-a-x_-v-a-l-u-e.html
[6b]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/map-keys.html
[6c]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/map-values.html

# Day 7

The key is to calculate the fuel cost based on distance traveled.
For part 1, the cost is equal to the distance.
For part 2, it is equal to the sum of integers from 1 up to `dist`, or `dist * (dist + 1) / 2`.

This different fuel calculations for the two parts are passed to a common `moveCrabs` function.

Today I learned about the [`maxOf`][7a] and [`minOf`][7b] functions.
These have a non-nullable return value and throw an error if there is nothing to evaluate.

**_EDIT:_** After the fact, I realized that I was doing something essentially like this:

```kotlin
fun myFun() {
    val crabs = someComputation()
    return doSomethingElse(crabs)
}
```

This can be refined to use the [`let`][7c] scope function:

```kotlin
fun myFun() = someComputation().let { doSomethingElse(it) }
```

This is an improvement, in my opinion.

[7a]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/max-of.html
[7b]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/min-of.html
[7c]: https://kotlinlang.org/docs/scope-functions.html#let

# Day 8

For part 1, in order to get the last four numbers of each entry, we make use of the `startIndex` parameter of [`Regex.findAll()`][8a].
The `startIndex` is where we find the pipe (`|`) character with `line.indexOf('|')`.

Kotlin provides a few ways to get a map from a sequence:

- [`associate`][8b]: the lambda must return a `Pair`. These become the map entries.
- [`associateBy`][8c]: the lambda returns the key. The original list item is the value.
- [`associateWith`][8d]: the lambda returns the value. The original list item is the key.


In part 2, we make use of these. First we create a map with the digits we know based on segment size, i.e. 1, 4, 7, and 8.
Then we use [`also`][8e] to return that map after adding the rest of the digits to it.

**_Tip:_** Standard library functions typically do not return mutuable maps.
The best way to get one is to take a map, e.g. from `associate`, and call `toMutableMap()` on it.

**_Tip:_** An easy way to reverse a map is:

```kotlin
myMap.associate{ (k, v) -> v to k }
```


[8a]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/find-all.html
[8b]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/associate.html
[8c]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/associate-by.html
[8d]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/associate-with.html
[8e]: https://kotlinlang.org/docs/scope-functions.html#also

# Day 9

This is our first time with an [`init`][9a] block.
We used it because we had a value we wanted to initialize, but it needed to take more than one line.
That is why an initializer like `val myValue = EXPR` doesn't work. Instead, we do something like this:

```kotlin
class myClass() {
    val myVal: String
    init {
        // could be any arbitrary code here.
        myVal = EXPR
    }
}
```

We also used the [`run`][9b] scope function. We wanted to return the result of the lambda, but use `this` inside the lambda.

[9a]: https://kotlinlang.org/docs/classes.html#constructors
[9b]: https://kotlinlang.org/docs/scope-functions.html#run

# Day 10

I am trying to be more expressive with code today, rather than a lot of collection streaming.
This is done by making extension functions.

Part 1 is to get the first illegal character for lines that have them ([`mapNotNull`][10a]).
Then sum up the illegal character score. That translates to:

```kotlin
fun part1(input: List<String>) = input.mapNotNull { it.firstIllegalCharOrNull() }.sumOf { illegalCharPoints.getValue(it) }
```

and it is quite readable, in my opinion.

For part 2, we keep the lines that do not have an illegal character. 
Then for each line, get the completion string, and calculate its score.
Then sort all scores and take the middle item. That translates to:

```kotlin
fun part2(input: List<String>) = input.filter { it.firstIllegalCharOrNull() == null }.map { it.completionString().completionScore() }.sorted().middle()
```

We describe what we are doing in English, translate to Kotlin, then add the necessary extension functions.

[10a]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/map-not-null.html

# Day 11

Today we override our first operator. In this case, we have a grid of points.
One dimension varies from 0 up to `nrow` and the other varies from 0 up to `ncol`.
How can we get all points in that grid? This might be nice:

```kotlin
for ( (i, j) in (0 until nrow) * (0 until ncol) ) doStuff()
```

We do that by [overloading the `*` operator][11a] (and we also get to use an extension function!):

```kotlin
operator fun IntRange.times(other: IntRange) = this.flatMap { i -> other.map { j -> i to j }}
```

Do not forget the `operator` modifier! It is required! Also note: `0 until nrow` is an [`IntRange`][11b]

Today, for debugging purposes I implemented the `toString` method on my `Octopuses` class.
I had to [override][11c] the base class `toString`.
One nice thing about Kotlin is that it makes you acknowledge you are overriding.
Without the `override` modifier, the compiler wouldn't accept this:

```kotlin
override fun toString() = "Hello!"
```

This helps to cut down on errors when a base class method may be overridden unintentionally.

The [`generateSequence`][11d] came in handy today for part 2.
We wanted to start at 1 and keep increasing until we got all octopuses to flash.

[11a]: https://kotlinlang.org/docs/operator-overloading.html
[11b]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/
[11c]: https://kotlinlang.org/docs/inheritance.html#overriding-methods
[11d]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/generate-sequence.html

# Day 12

Today we used [`Map.withDefault { ... }`][12a] to help get to something like Python's [`defaultdict`][12b].

The solution is a fairly straightforward [DFS recursion][12c].

[12a]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/with-default.html
[12b]: https://docs.python.org/3/library/collections.html#collections.defaultdict
[12c]: https://en.wikipedia.org/wiki/Depth-first_search

# Day 13

Today we used more [data classes][13a].
We used them to model a point, a fold, and the whole piece of folded origami.
Since `hashCode` and `equals` are implemented for us with data classes, we can
use them as members of a `Set`.
The origami paper is modeled to have a set of points.

[13a]: https://kotlinlang.org/docs/data-classes.html

# Day 14

This required a trick to keep track of [bigrams][14a] (two adjacent letters).

I used the nice trick for building up a dictionary of counts, using the [`apply`][14b] scope function and [`withDefault`][14c]:

```kotlin
fun nextGen(bigramCounts: Map<String, Long>, rules: Map<String, String>) = mutableMapOf<String, Long>().withDefault { 0 }.apply {
    bigramCounts.forEach { (bigram, count) ->
        val middle = rules.getValue(bigram)
        val first = "${bigram[0]}$middle"
        val second = "$middle${bigram[1]}"
        put(first, getValue(first) + count)
        put(second, getValue(second) + count)
    }
}
```

As usual, the receiver is referred to as `this` inside the lambda, and it can be omitted where it can be inferred, e.g. `put` instead of `this.put`.

[14a]: https://en.wikipedia.org/wiki/Bigram
[14b]: https://kotlinlang.org/docs/scope-functions.html#apply
[14c]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/with-default.html

# Day 15

For this challenge, I write an implementation of Dijkstra's shortest path algorithm, which necessitates writing a directed graph class.
This is a beefier challenge than most.

First, I make use of [`typealias`][15a] to reduce the visual clutter.
The graph has nodes being `Pair<Int, Int>`.
Instead of using that type everywhere, I first declared a type alias: 

```kotlin
typealias Node = Pair<Int, Int>
```

and now I can use `Node` where I need to declare a type.

The graph class is declared with [generics][15b]:

```kotlin
class WeightedDiGraph<E> {
    // ... do stuff with E
}
```

I overload the [indexed access operators][15c], so that `graph[node]` returns the neighbors and `graph[node1, node2]` returns the edge weight.

This is a fun challenge!

My Kotlin solution runs in 3.2 seconds while my Python solution with `networkx` runs in 4 seconds. I expected Kotlin to be much better 🤔

### Resources for Dijkstra's algorithm and A*

- [Dijkstra and A* algorithm explained][15d]
- [A* pathfinding explanation on Youtube][15e]

[15a]: https://kotlinlang.org/docs/type-aliases.html
[15b]: https://kotlinlang.org/docs/generics.html#generic-constraints
[15c]: https://kotlinlang.org/docs/operator-overloading.html#indexed-access-operator
[15d]: https://www.redblobgames.com/pathfinding/a-star/implementation.html
[15e]: https://www.youtube.com/watch?v=-L-WgKMFuhE

# Day 16

I use a top-level factory function for different data classes, `Literal`, `Header`, and `Packet`.

See a [discussion here about different types of factory functions][16a].
Other options are a companion object or an extension function. 

Today, I wrote a nice extension function for converting a hex string to a binary string:

```kotlin
fun String.hexToBin() = this.map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")
```

This makes use of [`Char.digitToInt()`][16b].

[16a]: https://kt.academy/article/ek-factory-functions
[16b]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/digit-to-int.html

# Day 17

This solution utilizes several different concepts that are idiomatic to Kotlin.

First, I use a `typealias` again for `Point` and `Velocity`. Both are `Pair<Int, Int>`.

One interesting thing is that I added [extension properties][17a] here:

```kotlin
typealias Point = Pair<Int, Int>
val Point.x get() = first
val Point.y get() = second
```

so that instead of accessing the x-coordinate of a Point with `point.first`, I can use `point.x`.

This is the first time using the [`run`][17b] scope function.
In this case, I use it to group together a chunk of code that initializes a class property.
I could have also used an `init { ... }` block to do that.

In other words, these are somewhat equivalent:

```kotlin
class A {
    val prop = run {
        // ... calculations
        computeSomething()
    }
}

class B {
    val prop: Int // type must be declared
    init {
        // ... calculations
        prop = computeSomething()
    }
}
```

With the `run` block, the type can be inferred from the result of the lambda.

I also use [`infix`][17c] functions to make things more readable.
For example, instead of `trajectory.endsIn(target)` we can have `trajectory endsIn target`.
Taking away those parentheses make things easier on the eyes.

[17a]: https://kotlinlang.org/docs/extensions.html#extension-properties
[17b]: https://kotlinlang.org/docs/scope-functions.html#run
[17c]: https://kotlinlang.org/docs/functions.html#infix-notation

# Day 18

This solution makes heavy use of [regular expressions][18a] and their [match results][18b].
Kotlin has a lot of built-in functionality for regular expressions and, as always, the documentation is great.

_Aside:_ I use a `List` as a stack and the methods `.add(item)` and `.removeLast()` for push and pop, respectively.
The stack tracks the `'['` and `']'` characters in the string.

For the _explode_ functionality we use a regular expression, we get substrings before and after the nested pair either:

- `val match = """\d+""".toRegex().findAll(before).lastOrNull()` to get the number just before the nested pair
- `val match = """\d+""".toRegex().findAll(after).firstOrNull()` to get the number just after the nested pair

That `MatchResult` object has a `range` property that pairs nicely with [`String.replaceRange()`][18c].
Once we have the match, we calculate the new number and insert it into the string.
The full definition for adding to the number just after the nested pair is:

```kotlin
fun String.addToFirstInt(int: Int): String {
    val match = """\d+""".toRegex().findAll(this).firstOrNull()
    return if (match == null) this else {
        replaceRange(match.range, (match.value.toInt() + int).toString())
    }
}
```

The _split_ functionality on snail numbers uses similar techniques. One small detail to get the right number correct:

```kotlin
fun String.split(): String {
    val match = """\d{2,}""".toRegex().findAll(this).firstOrNull()
    return if (match == null) this else {
        val left = match.value.toInt() / 2
        val right = match.value.toInt() / 2 + match.value.toInt() % 2  // remember this second term!
        replaceRange(match.range, "[$left,$right]")
    }
}
```

To compute the _magnitude_, I look for all `[A,B]`, where `A` and `B` are integers, using regular expressions.
Then I make the computation `3 * A + 2 * B` and replace the match with that computed value.
This time, however, the match includes non-digit characters.
So I use regular expression "groups" by putting parentheses around expressions I want to capture.

```kotlin
snail.replace("""\[(\d+),(\d+)]""".toRegex()) { match ->
    val (left, right) = match.groupValues.drop(1).map { it.toInt() }
    (3 * left + 2 * right).toString()
}
```

- We use [`String.replace()`][18d] to replace all matches at once.
The replacement can depend on the match itself by passing a lambda.
- Here I have to `.drop(1)` because the first group is always the entire matched string, e.g. `[A, B]`. See [`groupValues`][18e].

_Side note 1:_ Destructuring uses `.componentN()` functionality.
This works even though the result of `.map()` may have more than two integers after mapping:

_Side note 2:_ [`Regex matches String`][18f] (infix function) is used to check if a string is a number, e.g. `"""\d+""".toRegex() matches string`

```kotlin
val (int1, int2) = line.split(',').map { it.toInt() }
// is equivalent to
val int1 = line.split(',').map { it.toInt() }.component1()
val int2 = line.split(',').map { it.toInt() }.component2()
```

[18a]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/
[18b]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-match-result/
[18c]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/replace-range.html
[18d]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/replace.html
[18e]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-match-result/group-values.html
[18f]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/matches.html

# Day 19

This is probably the most difficult day in Advent of Code 2021.
The main strategy is label the first scanner's readings as solved with scanner at `(0,0,0)`. Then:

1. Iterate through the remaining unknown scanners' readings.
2. Perform all rotations
3. Sort the known beacons and the rotated beacons along X, Y, and Z dimensions to look for common diffs from one beacon to the next.
4. If there is a common diff, try to align rotated beacons to the known beacons based on the two beacons where that diff happens
5. If there is overlap of 12 or more, keep the aligned beacons and remove them from the unknown scanners' readings.

We get to use some cool Kotlin features today.

First: I use a data class `XYZ` to track a point or vector in space.
It supports addition, subtraction, unary minus, and computing manhattan distance between two.
The data class `XYZ` can also rotate around the X, Y, and Z axes.

The next interesting thing is how I iterate through all the rotations.

It is well known that rotations of 90 degree increments in X, Y, and Z dimensions form a mathematical group.
There are 4 rotations in each dimension, so you might think that there are `4 * 4 * 4 = 64` total rotations to check.
In fact there are only 24. That can be calculated as follows:
There are 6 ways to face (think sides of a cube), and four ways to be rotated while facing that direction, `6 * 4 = 24`.

I iterate through all four rotations of each of X, Y, Z rotations (64 total), composing a rotation function.
But I only yield the ones that are unique.
I keep track of uniqueness by computing the rotation on the basis vectors in 3D space. That looks like:

```kotlin
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
```

This makes use of [`iterator`][19a] and [`yield`][19b].
Of course, I could have just made a list of these rotation lambdas and returned that, too.

Another interesting feature is that I make a [`Comparator`][19c] of `XYZ` instances using, for example, [`compareBy<XYZ> { it.x }`][19d].
A `Comparator` takes two instances of a class and returns an integer representing the ordering of the two objects.
Meanwhile, `compareBy` can do multiple things, but here I pass a lambda that returns a comparable value (order on the `XYZ.x` property, for example).
The `Comparator` is used in [`Collection.sortedWith(Comparator)`][19e].

As an aside, there are a lot of ways to sort built into Kotlin! Read the documentation carefully.

Finally, I want to look at a few things here:

```kotlin
fun findAlignedBeacons(knownBeacons: Set<XYZ>, allUnknownScanners: List<List<XYZ>>): IndexedValue<BeaconsAndScanners> {
    for ((index, unknownScanners) in allUnknownScanners.withIndex()) for (rotate in allRotations()) {
        val rotatedBeacons = unknownScanners.map { point -> rotate(point) }
        attemptAlignment(knownBeacons, rotatedBeacons)?.let {
            return@findAlignedBeacons IndexedValue(index, it)
        }
    }
    throw Exception("Unable to find contiguous scanners")
}
```

- I return an [IndexedValue][19f]. This is a built-in class to represent a number (the index) and an object (the value).
I could use a pair, but this is more idiomatic. I want to return the index gives aligned beacons along with those aligned beacons and the scanner.
- The IndexedValue object is returned on condition that alignment succeeds. However, the return value is not nullable because I have a `throw` and Exception.
- I have two nested `for` loops on a single line. This works because the `for` loop performs the next statement, even if it isn't in braces `{` and `}`, just like `if (condition) value`
- I [return a value for the function from inside a lambda using an implicit label][19g]. Normally a return statement inside a lambda returns a value for that lambda. Sure, I could have saved the value to a val, and if not null, then return the IndexedValue. But I'm trying out some Kotlin features! 😄 

[19a]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/iterator.html
[19b]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/-sequence-scope/yield.html
[19c]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/#kotlin.Comparator
[19d]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.comparisons/compare-by.html
[19e]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/sorted-with.html
[19f]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-indexed-value/
[19g]: https://kotlinlang.org/docs/returns.html#return-to-labels

# Day 20

For this solution I use [arrays][20a] of True/False to represent the image. For example, this initializes the image:

```kotlin
val imageChars = input.drop(2)
val height = imageChars.size
val width = imageChars[0].length
val image = Array(height) { i -> Array(width) { j -> imageChars[i][j] == '#' } }
```

where `input` is a list of `String`, the puzzle input.

With each enhancement, the image grows bigger by one pixel in each direction (add 2 to the previous height and width).

In order to calculate the enhancement algorithm index, I get the 9 bits surrounding a pixel and then

```kotlin
bits.reversed().withIndex().sumOf { (index, bit) -> if (bit) 1 shl index else 0 }
```

Note: `1 shl index` is the same as `2 ** index` in python. There is no native exponent operator in Kotlin.

[20a]: https://kotlinlang.org/docs/basic-types.html#arrays

# Day 21

Today we do modular arithmetic on something other than, e.g. 0 - N, mod N + 1.
Today it is 1 - 10 then repeat back to 1. We need to bring it back to a scale that starts at 0.
Since 1 - 10 starts at 1, we subtract by 1. Since there are 10 squares, we `mod 10`. Then to get back to the original scale, add 1.

```kotlin
val nextSquare = (currentSquare - 1) % 10 + 1
```

Do similar for the deterministic die, but that one is 1-100.

What is interesting in Kotlin? We use [argument defaults][21a], e.g.

```kotlin
data class Player(val square: Int, val score: Int = 0)
```

A new player starts with a score of 0, initially. Just initialize with a starting square, e.g. `Player(8)`.

We use [`buildList`][21b] when getting the list of next states in the Dirac version.
Since we know there are `3 * 3 * 3 = 27` next states being added to the list (rolling the die three times, each with three possibilities), we declare the size of the array.

```kotlin
buildList(27) {
    for (i in 1..3) for (j in 1..3) for (k in 1..3) {
        // do stuff, build nextState
        add(nextState)
    }
}
```

When writing a recursive function, we need to carefully define the base case / stop conditions. In this situation, it is when the game is over.

My recursive function is `getWinTally(cache: DiracCache = emptyCache())`.
If it is the first time being called, then I create a new cache, otherwise I pass the cache from call to call.
The cache speeds things up, because there are many identical game states that are reached from different paths.

[21a]: https://kotlinlang.org/docs/functions.html#default-arguments
[21b]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/build-list.html

# Day 22

Today I have more fun with operator overloading and infix functions.
I have a `Cuboid` data class that supports the `minus` operator (e.g. `cuboid1 - cuboid2`) and `intersect` (e.g. `cuboid1 intersect cuboid2`).
This allows for concise and expressive code!

# Day 23

For some reason, I have written everything with extension functions and typealiases instead of using classes.

I use a `List<Int>` to represent the state of the squares. 

- 0 means empty
- 1 means A
- 2 means B
- 3 means C
- 4 means D

Indexes 0 - 6, inclusive, are the hallway squares that can be occupied.
The squares in front of the rooms cannot be occupied, so they are not in the array.

There are also helper arrays that show the distance and which squares would be crossed from to go from a room to a hallway, or vice versa.

One strange that happened from using extension functions is that I would get multiple receivers, and the compiler didn't always know what to do.
For example:

```kotlin
fun Squares.endConfiguration() = buildList {
    // what does "this" refer to?
}
```

So I try to avoid having multiple receivers in the same block. In fact, the above better written as:

```kotlin
fun Squares.endConfiguration() = List(size) {
    if (it in 0 until LEN_HALL) EMPTY_SQUARE else (it - LEN_HALL) / roomDepth + 1
}
```

Using a nice / convenient [constructor for `List`][23a], that uses an initializer function based on the index in the list.

[23a]: https://kotlinlang.org/docs/constructing-collections.html#initializer-functions-for-lists

# Day 24

This is a difficult problem that requires analyzing the input to see what is happening.
Once you know what is happening, it is not too difficult.

I use [`.chunked()`][24a] to iterate through the instructions. Every 18 lines is a set common block of instructions.

One more interesting point is that [`.joinToString()`][24b] works on a collection of any type.
Under the hood, the code calls `.toString()` on each element before joining.
The method can also accept a lambda that transforms an element to a string, according to the design of the programmer.

[24a]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/chunked.html
[24b]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/join-to-string.html

# Day 25

To solve the problem, we run a simulation until the sea cucumbers cannot move anymore.
If we were to create a list, this could be a _very large list_.
Instead, we make a [sequence][25a].
Kotlin sequences have a lot of benefits.

- They are evaluated lazily
- A lot of operations are stateless
- A terminal operation is what gives a result (such as `toList()`)

We have a data class `SeaCucumbers` and a factory method `makeSeaCucumbers`.
It has a method called `march()` that advances the east and south herds one step and returns a new `SeaCucumbers` instance.
Using [`generateSequence()`][25b], we make a sequence of the sea cucumber progression.

```kotlin
generateSequence( makeSeaCucumbers(input) ) { it.march() }
```

Take note, this does not actually make a list of `SeaCucumber` instances! We need to apply a _terminal_ operation (we use `first`) to get a result.

Before we get to that terminal operation, we need some _intermediate, stateless_ operations.
We need to know the index of the sea cucumber march progression, so we use [`withIndex()`][25c].
We also need to compare one sea cucumber configuration to the next to see if they are equal.
Kotlin's [`zipWithNext()`][26d] is perfect for that.

Now, we are ready to apply a terminal operation: [`first()`][26e].
We pass in a predicate that is true when the previous configuration is equal to the current one.
Therefore, the full solution is:

```kotlin
fun part1(input: List<String>) = generateSequence( makeSeaCucumbers(input) ) {
    it.march()
}.withIndex().zipWithNext().first { (prev, next) ->
    prev.value == next.value
}.second.index
```

Another fun thing I did was to use `typealias` and add some extension properties and functions:

```kotlin
typealias Point = Pair<Int, Int>
val Point.i get() = first
val Point.j get() = second
operator fun Point.plus(other: Point) = (first + other.i) to (second + other.j)
operator fun Point.rem(other: Point) = (first % other.i) to (second % other.j)
```

This lets me refer to `i` for the row number and `j` for the column.
I can add points together and use modular arithmetic, too. For example, if `seaCucumber` is a `Point`, a.k.a `Pair<Int, Int>`

```kotlin
val east = 0 to 1
val dimensions = input.size to input[0].length
val movedEast = (seaCucumber + east) % dimensions
```

Now `movedEast` is the new location for this sea cucumber. The modular arithmetic handles the wrap around, as required by the problem.

[25a]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/
[25b]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/generate-sequence.html
[25c]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/with-index.html
[26d]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/zip-with-next.html
[26e]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/first.html