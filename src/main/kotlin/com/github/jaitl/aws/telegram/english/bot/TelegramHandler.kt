package com.github.jaitl.aws.telegram.english.bot

import com.elbekD.bot.Bot
import com.elbekD.bot.types.Message
import com.github.jaitl.aws.telegram.english.Config
import com.github.jaitl.aws.telegram.english.command.CommandParser
import com.github.jaitl.aws.telegram.english.executor.CommandExecutor
import org.slf4j.LoggerFactory

class TelegramHandler(private val config: Config, private val executor: CommandExecutor) {
    private val logger = LoggerFactory.getLogger(this::class.java.canonicalName)
    private val telegramBot = Bot.createPolling("lambdaEnglishBot", config.telegramToken)

    fun startPolling() {
        logger.info("start polling")
        telegramBot.onMessage { handleMessage(it) }
        telegramBot.start()
    }

    fun handleMessage(message: Message) {
        try {
            val command = CommandParser.parse(message)
            executor.execute(command)
        } catch (e: Exception) {
            telegramBot.sendMessage(
                message.chat.id,
                "exception: ${e.javaClass.canonicalName}, reason: ${e.message}"
            )
        }
    }
}
