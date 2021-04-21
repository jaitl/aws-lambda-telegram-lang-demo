package com.github.jaitl.lambda.english.action

import com.github.jaitl.lambda.english.command.Command
import kotlin.reflect.KClass

interface Action {
    fun handleCommand(command: Command)
    fun getCommandType(): KClass<out Command>
}