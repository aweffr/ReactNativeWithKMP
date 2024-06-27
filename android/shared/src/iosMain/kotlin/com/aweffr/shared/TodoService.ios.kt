package com.aweffr.shared

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.newFixedThreadPoolContext
import platform.Foundation.NSUUID

actual fun getHttpClient(): HttpClient {
    return HttpClient(Darwin) {
        engine {
            configureRequest {
                setAllowsCellularAccess(true)
            }
        }
    }
}

actual fun randomUUID(): String {
    return NSUUID().UUIDString()
}

actual fun getCoroutineDispatcher(): CoroutineDispatcher {
    return newFixedThreadPoolContext(4, "shared-dispatcher")
}