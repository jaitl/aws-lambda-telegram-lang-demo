package com.github.jaitl.lambda.english

import com.elbekD.bot.types.Message
import com.github.jaitl.lambda.english.action.PingAction
import com.github.jaitl.lambda.english.action.RecognizeVoiceAction
import com.github.jaitl.lambda.english.action.SynthesizeSpeechAction
import com.github.jaitl.lambda.english.aws.Aws
import com.github.jaitl.lambda.english.bot.TelegramBotImpl
import com.github.jaitl.lambda.english.bot.TelegramHandler
import com.github.jaitl.lambda.english.executor.CommandExecutor
import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.EnvironmentVariablesPropertySource

class BotRunner {
    private val config = ConfigLoader.Builder().addSource(
        EnvironmentVariablesPropertySource(
            useUnderscoresAsSeparator = true,
            allowUppercaseNames = true
        )
    ).build().loadConfigOrThrow<Config>()

    private val aws = Aws()
    private val telegramBot = TelegramBotImpl(config)
    private val actions = listOf(
        PingAction(telegramBot),
        RecognizeVoiceAction(aws, telegramBot),
        SynthesizeSpeechAction(aws, telegramBot)
    )

    private val executor = CommandExecutor(actions)
    private val telegramHandler = TelegramHandler(config, executor)

    fun runLongPolling() {
        telegramHandler.startPolling()
    }

    fun handleMessage(message: Message) {
        telegramHandler.handleMessage(message)
    }
}
