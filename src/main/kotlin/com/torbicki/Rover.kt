package com.torbicki

import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.completedFuture

interface Actionable<R> {
    fun execute(action: Action): R

    fun execute(stream: ActionStream): R
}

typealias FuturePosition = CompletableFuture<Position>

enum class Detection {
    Empty, Obstacle
}

/**
 * If we ever need to implement rover's abilities, like detecting obstacles, other rovers, etc.
 */
sealed interface Sensor {
    fun detect(point: Point2D): Detection

    class Camera(private val area: Area) : Sensor {
        override fun detect(point: Point2D): Detection =
            if (area.obstacles.find { it.x == point.x && it.y == point.y } != null) {
                Detection.Obstacle
            } else {
                Detection.Empty
            }
    }
}

class Rover(
    position: Position,
    private val sensors: List<Sensor>
) : Actionable<FuturePosition> {

    var position = position
        private set

    var obstacle = false
        private set

    override fun execute(stream: ActionStream): FuturePosition {
        stream.actions.forEach { action ->
            execute(action)
        }
        return reportPosition()
    }

    override fun execute(action: Action): FuturePosition {
        val newPosition = when (action) {
            Action.MoveForward -> position.move(1)
            Action.MoveBackwards -> position.move(-1)
            Action.TurnLeft -> position.turnLeft()
            Action.TurnRight -> position.turnRight()
        }
        if (!obstacle && (!action.changingPosition || (action.changingPosition && canMove(newPosition.point)))) {
            position = newPosition
        } else {
            obstacle = true
        }
        return reportPosition()
    }

    private fun reportPosition(): FuturePosition = completedFuture(position)

    private fun canMove(point: Point2D): Boolean = sensors.all { it.detect(point) == Detection.Empty }

    companion object {
        fun create(pos: Position, area: Area): Rover = Rover(pos, listOf(Sensor.Camera(area)))
    }
}
