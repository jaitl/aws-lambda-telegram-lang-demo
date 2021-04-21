package com.github.jaitl.lambda.english

import com.elbekD.bot.types.Update
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.io.OutputStream

// aws lambda endpoint
class AwsLambdaMain {
    private val logger = LoggerFactory.getLogger(this::class.java.canonicalName)
    private val gson = Gson()
    private val botRunner = BotRunner()

    fun handler(input: InputStream, output: OutputStream) {
        logger.info("start lambda handler")

        val content = input.bufferedReader().readText()
        val update: Update = gson.fromJson(content, Update::class.java)

        try {
            if (update.message != null) {
                botRunner.handleMessage(update.message!!)
            } else {
                logger.warn("unknown update: $update")
            }
        } catch (e: Exception) {
            logger.error("Fail to handle update: $update", e)
        }

        val writer = output.writer()
        writer.write("ok")
        writer.flush()
        writer.close()
        output.close()

        logger.info("end lambda handler")
    }
}
