package com.github.jaitl.aws.telegram.english.action

import com.github.jaitl.aws.telegram.english.aws.Aws
import com.github.jaitl.aws.telegram.english.bot.TelegramBot
import com.github.jaitl.aws.telegram.english.command.Command
import com.github.jaitl.aws.telegram.english.command.TextCommand
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.reflect.KClass

class SynthesizeSpeechAction(private val aws: Aws, private val telegramBot: TelegramBot): Action {
    private val logger = LoggerFactory.getLogger(this::class.java.canonicalName)

    override fun handleCommand(command: Command) {
        logger.info("synthesize speech")
        val cmd = command as TextCommand
        val translatedText = aws.translate(cmd.text)
        val fileByteArray = aws.synthesizeSpeech(cmd.text)
        val fileName = UUID.randomUUID().toString().substring(0, 6)
        telegramBot.sendMessage(cmd.chatId,"Translation: $translatedText")
        telegramBot.sendAudio(cmd.chatId, fileByteArray, title = "$fileName.mp3")
    }

    override fun getCommandType(): KClass<out Command> {
        return TextCommand::class
    }
}
