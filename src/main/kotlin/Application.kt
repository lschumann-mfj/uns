package io.mfj

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.jetty.jakarta.EngineMain.main(args)
}

fun Application.module() {
    configureRouting()
}
