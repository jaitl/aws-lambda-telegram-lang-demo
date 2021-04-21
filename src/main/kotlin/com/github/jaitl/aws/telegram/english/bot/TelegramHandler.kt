package com.github.jaitl.aws.telegram.english.bot

import com.elbekD.bot.Bot
import com.elbekD.bot.types.Message
import com.github.jaitl.aws.telegram.english.Config
import com.github.jaitl.aws.telegram.english.action.RecognizeVoiceAction
import com.github.jaitl.aws.telegram.english.action.SynthesizeSpeechAction
import com.github.jaitl.aws.telegram.english.aws.Aws
import org.slf4j.LoggerFactory

class TelegramHandler(config: Config) {
    private val logger = LoggerFactory.getLogger(this::class.java.canonicalName)
    private val telegramBot = Bot.createPolling("lambdaEnglishBot", config.telegramToken)

    private val aws = Aws()
    private val bot = TelegramBotImpl(config)

    private val recognizeVoiceAction = RecognizeVoiceAction(aws, bot)
    private val synthesizeSpeechAction = SynthesizeSpeechAction(aws, bot)

    fun startPolling() {
        logger.info("start polling")
        telegramBot.onAnyUpdate { update -> if (update.message != null) handleMessage(update.message!!) }
        telegramBot.start()
    }

    fun handleMessage(message: Message) {
        try {
            when {
                message.text != null ->
                    handleTextMessage(message.chat.id, message.text!!)
                message.voice != null ->
                    handleVoiceMessage(message.chat.id, message.voice!!.file_id)
                else ->
                    throw Exception("unknown command")
            }
        } catch (e: Exception) {
            logger.error("Fail during command execution", e)
            bot.sendMessage(
                message.chat.id,
                "exception: ${e.javaClass.canonicalName}, reason: ${e.message}"
            )
        }
    }

    private fun handleTextMessage(chatId: Long, text: String) {
        when {
            text.endsWith("ping") -> {
                logger.info("ping-pong command")
                bot.sendMessage(chatId, "pong")
            }
            !text.startsWith("/") ->
                synthesizeSpeechAction.handleCommand(chatId, text)
            else ->
                throw Exception("unknown text command")
        }
    }

    private fun handleVoiceMessage(chatId: Long, voiceFileId: String) {
        recognizeVoiceAction.handleCommand(chatId, voiceFileId)
    }
}
