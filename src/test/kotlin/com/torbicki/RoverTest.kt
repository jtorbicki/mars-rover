package com.torbicki

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class RoverTest : FunSpec({
    test("Should process actions") {
        val initial = Position(
            point = Point2D(0, 0),
            orientation = Position.Orientation.NORTH
        )
        val area = Area()
        val rover = Rover.create(initial, area)

        rover.execute(Action.TurnRight)
        rover.position.orientation shouldBe Position.Orientation.EAST

        rover.execute(Action.TurnLeft)
        rover.position.orientation shouldBe Position.Orientation.NORTH

        rover.execute(Action.MoveForward)
        rover.position.point shouldBe initial.point.copy(initial.point.x, initial.point.y + 1)

        rover.execute(Action.MoveBackwards)
        rover.position.point shouldBe initial.point
    }
})