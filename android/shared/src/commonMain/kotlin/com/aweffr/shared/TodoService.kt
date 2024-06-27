package com.aweffr.shared

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextLong

expect fun getHttpClient(): HttpClient

expect fun randomUUID(): String

expect fun getCoroutineDispatcher(): CoroutineDispatcher

@Serializable
data class Todo(
    val id: Int,
    val title: String,
    val completed: Boolean
)

data class TodoServiceEventPayload(val taskId: String, val message: String, val todos: List<Todo>)


class TodoService {

    val scope = CoroutineScope(getCoroutineDispatcher())

    val httpClient = getHttpClient().config {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15 * 1000
        }
        install(DefaultRequest)

        defaultRequest {
            header("X-Request-ID", randomUUID())
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("TodoService Http Request: $message")
                }
            }
            level = LogLevel.HEADERS
        }

    }

    // case 1: call a local function and get return value
    fun getLocalTodos(): List<Todo> {
        return listOf(
            Todo(
                id = 1,
                title = "Buy milk",
                completed = false
            ),
            Todo(
                id = 2,
                title = "Buy bread",
                completed = true
            )
        )
    }


    // case 2: call a suspend function and get return value
    @Throws(Throwable::class)
    suspend fun getHttpTodos(): List<Todo> {
        return httpClient.get("https://jsonplaceholder.typicode.com/todos").body()
    }

    // case 3: start a async task and get result later ( through events )
    fun getHttpTodosAndSendEvent(taskId: String) {
        scope.launch {
            val todos = getHttpTodos()
            val chunkedTodos = todos.chunked(5)
            chunkedTodos.forEachIndexed() { index, chunk ->
                val timeToSleep = Random.nextLong(1000L..2000L)
                val message = if (index == chunkedTodos.lastIndex) {
                    "processed ${chunk.size} todos. done."
                } else {
                    "processed ${chunk.size} todos. sleep for $timeToSleep ms."
                }
                sendEventCallback(TodoServiceEventPayload(taskId = taskId, todos = chunk, message = message))
                delay(timeToSleep)
            }
        }
    }

    var sendEventCallback: (TodoServiceEventPayload) -> Unit = defaultSendEventCallback

    fun registerSendEventCallback(callback: (TodoServiceEventPayload) -> Unit) {
        sendEventCallback = callback
    }


    companion object {
        val defaultSendEventCallback = { payload: TodoServiceEventPayload ->
            print("default callback called. payload: $payload")
        }
        val EVENT_NAME_TODOS_RECEIVED = "RN_SHARED_ON_TODOS_RECEIVED"
    }
}