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