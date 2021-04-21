package com.github.jaitl.aws.telegram.english.action

import com.github.jaitl.aws.telegram.english.aws.Aws
import com.github.jaitl.aws.telegram.english.bot.TelegramBot
import org.slf4j.LoggerFactory

class RecognizeVoiceAction(private val aws: Aws, private val telegramBot: TelegramBot) {
    private val logger = LoggerFactory.getLogger(this::class.java.canonicalName)

    fun handleCommand(chatId: Long, fileId: String) {
        logger.info("recognize voice")
        val fileSteam = telegramBot.getFileStream(fileId)
        val msg = aws.transcribe(fileSteam)
        telegramBot.sendMessage(chatId, "You said: $msg")
    }
}
