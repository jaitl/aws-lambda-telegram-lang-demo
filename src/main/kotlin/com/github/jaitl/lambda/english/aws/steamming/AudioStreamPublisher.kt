package com.github.jaitl.lambda.english.aws.steamming

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import software.amazon.awssdk.services.transcribestreaming.model.AudioStream
import java.io.InputStream

internal class AudioStreamPublisher(val inputStream: InputStream) :
    Publisher<AudioStream> {

    override fun subscribe(s: Subscriber<in AudioStream>) {
        s.onSubscribe(SubscriptionImpl(s, inputStream))
    }
}
