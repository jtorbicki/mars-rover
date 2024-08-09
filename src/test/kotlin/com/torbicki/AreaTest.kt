package com.torbicki

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe

class AreaTest : FunSpec({
    test("Should move rover in the area with obstacles") {
        val obstacles = listOf(
            Point2D(x = 1, y = 4),
            Point2D(x = 3, y = 5),
            Point2D(x = 7, y = 4),
            Point2D(x = 6, y = 5)
        )
        val area = Area(obstacles)

        val initial = Position(
            point = Point2D(4, 2),
            orientation = Position.Orientation.EAST
        )

        val rover = area.spawnRover(initial)
        val actions = ActionStream(
            sequenceOf(
                Action.MoveForward,
                Action.TurnLeft,
                Action.MoveForward,
                Action.MoveForward,
                Action.MoveForward,
                Action.TurnRight,
                Action.MoveForward,
                Action.TurnLeft,
                Action.MoveBackwards
            )
        )
        val future = rover.execute(actions).thenAccept { final ->
            final.point.x shouldBeExactly 5
            final.point.y shouldBeExactly 5
            final.orientation shouldBe Position.Orientation.EAST
            rover.obstacle shouldBe true
        }
        future.isCompletedExceptionally shouldBe false
    }

    test("Should not be able to spawn rover outside of the allowed boundaries") {
        val distance = 20
        val outsidePositionX = Position(Point2D(distance + 1, 0))
        val outsidePositionY = Position(Point2D(0, distance + 1))
        val outsidePositionMinusX = Position(Point2D(-distance - 1, 0))
        val outsidePositionMinusY = Position(Point2D(0, -distance - 1))

        shouldThrow<RuntimeException> {
            Area(distance = distance).spawnRover(outsidePositionX)
        }
        shouldThrow<RuntimeException> {
            Area(distance = distance).spawnRover(outsidePositionY)
        }
        shouldThrow<RuntimeException> {
            Area(distance = distance).spawnRover(outsidePositionMinusX)
        }
        shouldThrow<RuntimeException> {
            Area(distance = distance).spawnRover(outsidePositionMinusY)
        }
    }
})
