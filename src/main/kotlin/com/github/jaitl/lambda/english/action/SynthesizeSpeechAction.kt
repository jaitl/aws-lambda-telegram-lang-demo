package com.github.jaitl.lambda.english.action

import com.github.jaitl.lambda.english.aws.Aws
import com.github.jaitl.lambda.english.bot.TelegramBot
import com.github.jaitl.lambda.english.command.Command
import com.github.jaitl.lambda.english.command.TextCommand
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.reflect.KClass

class SynthesizeSpeechAction(private val aws: Aws, private val telegramBot: TelegramBot): Action {
    private val logger = LoggerFactory.getLogger(this::class.java.canonicalName)

    override fun handleCommand(command: Command) {
        logger.info("synthesize speech")
        val cmd = command as TextCommand
        val fileByteArray = aws.synthesizeSpeech(cmd.text)
        val fileName = UUID.randomUUID().toString().substring(0, 6)
        telegramBot.sendAudio(cmd.chatId, fileByteArray, title = "$fileName.mp3")
    }

    override fun getCommandType(): KClass<out Command> {
        return TextCommand::class
    }
}