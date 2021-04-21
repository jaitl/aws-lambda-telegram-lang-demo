package com.github.jaitl.aws.telegram.english.executor

import com.github.jaitl.aws.telegram.english.action.Action
import com.github.jaitl.aws.telegram.english.command.Command

class CommandExecutor(private val actions: List<Action>) {
    fun execute(command: Command) {
        val action: Action? = actions.firstOrNull { it.getCommandType() == command::class }

        if (action == null) {
            throw Exception("action for command: $command not found")
        }

        action.handleCommand(command)
    }
}
