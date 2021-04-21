package com.github.jaitl.aws.telegram.english.action

import com.github.jaitl.aws.telegram.english.aws.Aws
import com.github.jaitl.aws.telegram.english.bot.TelegramBot
import org.slf4j.LoggerFactory
import java.util.*

class SynthesizeSpeechAction(private val aws: Aws, private val telegramBot: TelegramBot) {
    private val logger = LoggerFactory.getLogger(this::class.java.canonicalName)

    fun handleCommand(chatId: Long, text: String) {
        logger.info("synthesize speech")
        val translatedText = aws.translate(text)
        val fileByteArray = aws.synthesizeSpeech(text)
        val fileName = UUID.randomUUID().toString().substring(0, 6)
        telegramBot.sendMessage(chatId, "Translation: $translatedText")
        telegramBot.sendAudio(chatId, fileByteArray, title = "$fileName.mp3")
    }
}
