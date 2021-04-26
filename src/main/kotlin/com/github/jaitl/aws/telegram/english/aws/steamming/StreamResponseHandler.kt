package com.github.jaitl.aws.telegram.english.aws.steamming

import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.transcribestreaming.model.Result
import software.amazon.awssdk.services.transcribestreaming.model.StartStreamTranscriptionResponseHandler
import software.amazon.awssdk.services.transcribestreaming.model.TranscriptEvent
import software.amazon.awssdk.services.transcribestreaming.model.TranscriptResultStream
import java.util.concurrent.BlockingQueue

object StreamResponseHandler {
    private val logger = LoggerFactory.getLogger(this::class.java.canonicalName)

    fun createResponseHandler(lockingQueue: BlockingQueue<String>): StartStreamTranscriptionResponseHandler {
        return StartStreamTranscriptionResponseHandler.builder()
            .onError { e: Throwable ->
                logger.error("Fail during StreamTranscription", e)
            }
            .subscriber { event: TranscriptResultStream ->
                val results: List<Result> =
                    (event as TranscriptEvent).transcript().results()
                if (results.isNotEmpty()) {
                    if (results[0].alternatives()[0].transcript().isNotEmpty()) {
                        lockingQueue.add(results[0].alternatives()[0].transcript())
                    }
                }
            }
            .build()
    }
}

