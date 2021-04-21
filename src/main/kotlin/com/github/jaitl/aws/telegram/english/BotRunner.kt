package com.github.jaitl.aws.telegram.english

import com.elbekD.bot.types.Message
import com.github.jaitl.aws.telegram.english.bot.TelegramHandler
import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.EnvironmentVariablesPropertySource
import com.sksamuel.hoplite.SystemPropertiesPropertySource

class BotRunner {
    private val config = ConfigLoader.Builder()
        .addSource(EnvironmentVariablesPropertySource(
            useUnderscoresAsSeparator = true,
            allowUppercaseNames = true
        )
    ).addSource(SystemPropertiesPropertySource)
        .build().loadConfigOrThrow<Config>()

    private val telegramHandler = TelegramHandler(config)

    fun runLongPolling() {
        telegramHandler.startPolling()
    }

    fun handleMessage(message: Message) {
        telegramHandler.handleMessage(message)
    }
}
