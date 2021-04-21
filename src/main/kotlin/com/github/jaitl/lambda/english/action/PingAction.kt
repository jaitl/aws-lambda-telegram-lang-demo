package com.github.jaitl.lambda.english.action

import com.github.jaitl.lambda.english.bot.TelegramBot
import com.github.jaitl.lambda.english.command.Command
import com.github.jaitl.lambda.english.command.PingCommand
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

class PingAction(private val telegramBot: TelegramBot) : Action {
    private val logger = LoggerFactory.getLogger(this::class.java.canonicalName)


    override fun handleCommand(command: Command) {
        logger.info("ping-pong")
        telegramBot.sendMessage(command.chatId, "pong")
    }

    override fun getCommandType(): KClass<out Command> {
        return PingCommand::class
    }
}
