package com.torbicki

import java.io.BufferedReader
import java.io.File

fun main(args: Array<String>) {
    if (args.size == 1) {
        val stream = File(args[0]).bufferedReader()
        val area = Area(parseObstacles(stream.readLine()))
        processCommandStream(area = area, stream = stream)
    } else {
        println("Usage: filename")
    }
}

fun processCommandStream(area: Area, stream: BufferedReader) {
    do {
        val spawnLine = stream.readLine()
        val actionsLine = stream.readLine()
        val isEndOfFile = spawnLine == null || actionsLine == null
        if (!isEndOfFile) {
            val position = parsePosition(spawnLine)
            val actions = parseActions(actionsLine)
            area.spawnRover(position).apply {
                execute(stream = actions).thenAccept { position ->
                    println("(${position.point.x}, ${position.point.y}) ${position.orientation}")
                }
            }
        }
    } while (!isEndOfFile)
}
