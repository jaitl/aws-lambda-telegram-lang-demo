package com.github.jaitl.aws.telegram.english.command

import com.elbekD.bot.types.Message

object CommandParser {
    fun parse(message: Message): Command {
        return when {
            message.text != null ->
                parseTextCommand(message.chat.id, message.text!!)
            message.voice != null ->
                VoiceCommand(message.chat.id, message.voice!!.file_id)
            else ->
                throw Exception("unknown command")
        }
    }

    private fun parseTextCommand(chatId: Long, command: String): Command {
        return when {
            command.endsWith("ping") ->
                PingCommand(chatId)
            command.startsWith("/cat") ->
                AddCategoryCommand(chatId, command.removePrefix("/cat").trim())
            !command.startsWith("/") ->
                TextCommand(chatId, command)
            else ->
                throw Exception("unknown command")
        }
    }
}
