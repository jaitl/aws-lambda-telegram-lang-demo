package com.github.jaitl.aws.telegram.english.action

import com.github.jaitl.aws.telegram.english.command.Command
import kotlin.reflect.KClass

interface Action {
    fun handleCommand(command: Command)
    fun getCommandType(): KClass<out Command>
}