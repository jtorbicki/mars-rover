package com.torbicki

enum class Action {
    TurnLeft, TurnRight, MoveForward, MoveBackwards;

    val changingPosition: Boolean
        get() = this == MoveForward || this == MoveBackwards
}

class ActionStream(val actions: Sequence<Action>)
