package com.aweffr.shared

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

actual fun getHttpClient(): HttpClient = HttpClient(OkHttp) {
    engine {
        config {
            dispatcher(Dispatcher().apply {
                maxRequests = 64
                maxRequestsPerHost = 5
            })
        }
    }
}

actual fun randomUUID(): String {
    return java.util.UUID.randomUUID().toString()
}

actual fun getCoroutineDispatcher(): CoroutineDispatcher {
    return Dispatchers.IO
}