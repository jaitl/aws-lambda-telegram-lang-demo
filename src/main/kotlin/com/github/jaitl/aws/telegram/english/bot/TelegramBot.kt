package com.github.jaitl.aws.telegram.english.bot

import java.io.InputStream

interface TelegramBot {
    fun sendMessage(chatId: Long, message: String)
    fun sendAudio(chatId: Long, audio: ByteArray, title: String)
    fun getFileStream(fileId: String): InputStream
}
