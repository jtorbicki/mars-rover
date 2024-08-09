package com.torbicki

fun parseActions(line: String): ActionStream {
    val actions = line.map { action ->
        when (action) {
            'L' -> Action.TurnLeft
            'R' -> Action.TurnRight
            'F' -> Action.MoveForward
            'B' -> Action.MoveBackwards
            else -> throw java.lang.RuntimeException("Parsing error - unknown action: $action in line $line")
        }
    }
    return ActionStream(actions.asSequence())
}

fun parsePosition(line: String): Position {
    val (x, y, o) = line.split(" ")

    val orientation = when (o) {
        "NORTH" -> Position.Orientation.NORTH
        "SOUTH" -> Position.Orientation.SOUTH
        "EAST" -> Position.Orientation.EAST
        "WEST" -> Position.Orientation.WEST
        else -> throw java.lang.RuntimeException("Parsing error - unknown orientation: $o in line $line")
    }

    return Position(Point2D(x.toInt(), y.toInt()), orientation)
}

fun parseObstacles(line: String): List<Point2D> {
    val regex = """\[(\d+),\s*(\d+)]""".toRegex()
    val result = regex.findAll(line)
    return result.map {
        val (x, y) = it.destructured
        Point2D(x.toInt(), y.toInt())
    }.toList()
}
