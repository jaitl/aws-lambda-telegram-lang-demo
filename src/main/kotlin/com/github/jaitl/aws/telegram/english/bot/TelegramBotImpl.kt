package com.github.jaitl.aws.telegram.english.bot

import com.elbekD.bot.Bot
import com.github.jaitl.aws.telegram.english.Config
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import java.io.InputStream

class TelegramBotImpl(private val config: Config) : TelegramBot {
    private val logger = LoggerFactory.getLogger(this::class.java.canonicalName)
    private val telegramBot = Bot.createPolling("lambdaEnglishBot", config.telegramToken)
    private val client = OkHttpClient()

    override fun sendMessage(chatId: Long, message: String) {
        telegramBot.sendMessage(chatId, message).join()
    }

    override fun sendAudio(chatId: Long, audio: ByteArray, title: String) {
        telegramBot.sendAudio(chatId, audio, title = title).join()
    }

    override fun getFileStream(fileId: String): InputStream {
        val fileUrl = telegramBot.getFile(fileId).get().file_path
        val url = "https://api.telegram.org/file/bot${config.telegramToken}/$fileUrl"

        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()

        return response.body()?.byteStream() ?: throw Exception("Can't download file: $fileId")
    }
}
