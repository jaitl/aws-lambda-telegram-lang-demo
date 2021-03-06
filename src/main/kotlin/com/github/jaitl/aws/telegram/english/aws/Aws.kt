package com.github.jaitl.aws.telegram.english.aws

import com.github.jaitl.aws.telegram.english.aws.steamming.AudioStreamPublisher
import com.github.jaitl.aws.telegram.english.aws.steamming.StreamResponseHandler
import org.slf4j.LoggerFactory
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.polly.PollyClient
import software.amazon.awssdk.services.polly.model.DescribeVoicesRequest
import software.amazon.awssdk.services.polly.model.OutputFormat
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest
import software.amazon.awssdk.services.polly.model.Voice
import software.amazon.awssdk.services.transcribestreaming.TranscribeStreamingAsyncClient
import software.amazon.awssdk.services.transcribestreaming.model.LanguageCode
import software.amazon.awssdk.services.transcribestreaming.model.MediaEncoding
import software.amazon.awssdk.services.transcribestreaming.model.StartStreamTranscriptionRequest
import software.amazon.awssdk.services.translate.TranslateClient
import software.amazon.awssdk.services.translate.model.TranslateTextRequest
import software.amazon.awssdk.utils.IoUtils
import java.io.InputStream
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque


class Aws {
    private val logger = LoggerFactory.getLogger(this::class.java.canonicalName)

    private val awsRegion = Region.US_WEST_2
    private val credentialsProvider = DefaultCredentialsProvider.create()

    private val transcribeStreamingClient = TranscribeStreamingAsyncClient.builder()
        .credentialsProvider(credentialsProvider)
        .region(awsRegion)
        .build()

    private val translateClient = TranslateClient.builder()
        .credentialsProvider(credentialsProvider)
        .region(awsRegion)
        .build();

    private val pollyClient = PollyClient.builder()
        .credentialsProvider(credentialsProvider)
        .region(awsRegion)
        .build()

    private val voice: Voice by lazy {
        val describeVoiceRequest = DescribeVoicesRequest.builder()
            .engine("standard")
            .build()

        val describeVoicesResult = pollyClient.describeVoices(describeVoiceRequest)
        describeVoicesResult.voices()[26]
    }

    fun transcribe(inputAudio: InputStream): String {
        val request = StartStreamTranscriptionRequest.builder()
            .languageCode(LanguageCode.EN_US.toString())
            .mediaEncoding(MediaEncoding.OGG_OPUS)
            .mediaSampleRateHertz(48000)
            .build()

        val blockingQueue: BlockingQueue<String> = LinkedBlockingDeque(100)

        val result = transcribeStreamingClient.startStreamTranscription(
            request,
            AudioStreamPublisher(inputAudio),
            StreamResponseHandler.createResponseHandler(blockingQueue)
        )

        result.get()

        return blockingQueue.last()
    }

    fun translate(text: String): String {
        val textRequest = TranslateTextRequest.builder()
            .sourceLanguageCode("en")
            .targetLanguageCode("ru")
            .text(text)
            .build()

        return translateClient.translateText(textRequest).translatedText()
    }

    fun synthesizeSpeech(text: String): ByteArray {
        val synthReq: SynthesizeSpeechRequest = SynthesizeSpeechRequest.builder()
            .text(text)
            .voiceId(voice.id())
            .outputFormat(OutputFormat.MP3)
            .build()

        val synthRes = pollyClient.synthesizeSpeech(synthReq)
        val fileByteArray = IoUtils.toByteArray(synthRes)

        synthRes.close()

        return fileByteArray
    }
}
