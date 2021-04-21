package com.github.jaitl.lambda.english.bot

import java.io.InputStream

interface TelegramBot {
    fun sendMessage(chatId: Long, message: String)
    fun sendAudio(chatId: Long, audio: ByteArray, title: String)
    fun getFileStream(fileId: String): InputStream
}
