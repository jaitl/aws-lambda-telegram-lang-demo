import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    application
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("com.palantir.graal") version "0.7.2"
}

group = "com.github.jaitl.bot.lambda"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
    implementation("com.sksamuel.hoplite:hoplite-core:1.4.0")
    implementation("com.github.elbekD:kt-telegram-bot:1.3.8")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.okhttp3:okhttp:3.10.0")

    implementation("software.amazon.awssdk:transcribestreaming:2.16.18")
    implementation("software.amazon.awssdk:polly:2.16.18")
    implementation("software.amazon.awssdk:translate:2.16.18")

    implementation("ch.qos.logback:logback-classic:1.2.3")

    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClassName = "com.github.jaitl.aws.telegram.english.MainLocalKt"

}

tasks.withType<JavaExec> {
    systemProperties(System.getProperties().toMap() as Map<String?,Any?>)
}
