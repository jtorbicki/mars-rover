package com.torbicki

import kotlin.math.absoluteValue

data class Point2D(
    val x: Int,
    val y: Int
)

data class Position(
    val point: Point2D,
    val orientation: Orientation = Orientation.NORTH
) {
    enum class Orientation {
        EAST, WEST, NORTH, SOUTH;

        val right
            get() = when (this) {
                EAST -> SOUTH
                SOUTH -> WEST
                WEST -> NORTH
                NORTH -> EAST
            }

        val left
            get() = when (this) {
                EAST -> NORTH
                SOUTH -> EAST
                WEST -> SOUTH
                NORTH -> WEST
            }
    }

    fun turnLeft() = copy(orientation = orientation.left)

    fun turnRight() = copy(orientation = orientation.right)

    fun move(step: Int): Position {
        val newPoint = when (orientation) {
            Orientation.NORTH -> Point2D(point.x, point.y + step)
            Orientation.EAST -> Point2D(point.x + step, point.y)
            Orientation.SOUTH -> Point2D(point.x, point.y - step)
            Orientation.WEST -> Point2D(point.x - step, point.y)
        }
        return Position(newPoint, orientation)
    }
}

class Area(
    val obstacles: List<Point2D> = emptyList(),
    distance: Int? = null
) {
    private val _rovers: MutableList<Rover> = mutableListOf()

    val distance = distance?.absoluteValue

    val rovers: List<Rover>
        get() = _rovers.toList()

    fun spawnRover(position: Position): Rover = Rover.create(position, this).also {
        if (positionAllowed(position)) {
            _rovers += it
        } else {
            throw RuntimeException("Rover outside of boundaries at position ${position}")
        }
    }

    private fun positionAllowed(position: Position): Boolean {
        if (distance != null) {
            return position.point.x.absoluteValue < distance && position.point.y.absoluteValue < distance
        }
        return true
    }
}
