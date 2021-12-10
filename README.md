# Advent of Code 2021 in Kotlin

These are my solutions to Advent of Code 2021 using the Kotlin language. Each day has its own subpackage.

Try the problems yourself at [https://adventofcode.com/2021/](https://adventofcode.com/2021/).

Feel free to create a Github issue if you want to discuss anything!

# Usage

1. Clone this repo: `git clone https://github.com/jkpr/advent-of-code-2021-kotlin`
2. Open in IntelliJ IDEA

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