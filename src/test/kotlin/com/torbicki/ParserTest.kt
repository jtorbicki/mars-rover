package com.torbicki

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe

class ParserTest : FunSpec({
    test("Parse position should throw exception") {
        shouldThrow<RuntimeException> {
            parsePosition("Z")
        }
        shouldThrow<RuntimeException> {
            parsePosition("1 E")
        }
        shouldThrow<RuntimeException> {
            parsePosition("1 2 Z")
        }
        shouldThrow<RuntimeException> {
            parsePosition("1 A NORTH")
        }
    }

    test("Parse position should return parsed position") {
        val north = parsePosition("1 2 NORTH")
        north.point.x shouldBeExactly 1
        north.point.y shouldBeExactly 2
        north.orientation shouldBe Position.Orientation.NORTH
        val east = parsePosition("1 2 EAST")
        east.orientation shouldBe Position.Orientation.EAST
        val west = parsePosition("1 2 WEST")
        west.orientation shouldBe Position.Orientation.WEST
        val south = parsePosition("1 2 SOUTH")
        south.orientation shouldBe Position.Orientation.SOUTH
    }
})

class ActionParserTest : FunSpec({
    test("Parse action should throw exception") {
        shouldThrow<RuntimeException> {
            parseActions("XXX")
        }
        shouldThrow<RuntimeException> {
            parseActions("FFLRBA")
        }
    }

    test("Parse action should return parsed actions") {
        val stream = parseActions("FBLR").actions.iterator()
        stream.next() shouldBe Action.MoveForward
        stream.next() shouldBe Action.MoveBackwards
        stream.next() shouldBe Action.TurnLeft
        stream.next() shouldBe Action.TurnRight
    }
})

class ProcessCommandStreamTest: FunSpec({
    test("Should create 3 rovers") {
        val area = Area()
        val stream = """
            1 1 WEST
            LLLLRRRR
            2 2 EAST
            FBBFBBFBBFBB
            3 3 NORTH
            LFFFFFFFFF
        """.trimIndent()
        processCommandStream(area, stream.byteInputStream().bufferedReader())
        area.rovers.size shouldBeExactly 3
    }
})
