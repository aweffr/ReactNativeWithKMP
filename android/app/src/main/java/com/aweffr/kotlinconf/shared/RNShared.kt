package com.aweffr.kotlinconf.shared

import android.util.Log
import com.aweffr.shared.Todo
import com.aweffr.shared.TodoService
import com.aweffr.shared.TodoServiceEventPayload
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.uimanager.ViewManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "RNShared"

private fun debugLog(message: String) {
    val threadId = Thread.currentThread().id
    val threadName = Thread.currentThread().name
    Log.i(TAG, "[threadId:$threadId, threadName:$threadName]$message")
}

fun Todo.asReactMap(): WritableMap {
    return Arguments.createMap().apply {
        putInt("id", id)
        putString("title", title)
        putBoolean("completed", completed)
    }
}

class RNSharedPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(RNSharedModule(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}

class RNSharedModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return TAG
    }

    private val todoService = TodoService()
    private val scope: CoroutineScope
        get() = todoService.scope


    @ReactMethod
    fun getLocalTodos(promise: Promise) {
        debugLog("getLocalTodos called.")
        try {
            val todos = todoService.getLocalTodos()
            val result = Arguments.createArray().apply {
                todos.forEach {
                    pushMap(it.asReactMap())
                }
            }
            promise.resolve(result)
        } catch (e: Exception) {
            promise.reject("RNSharedError", e)
        }
    }

    @ReactMethod
    fun getHttpTodos(promise: Promise) {
        debugLog("getHttpTodos called.")
        scope.launch {
            try {
                val todos = todoService.getHttpTodos()
                val result = Arguments.createArray().apply {
                    todos.forEach {
                        pushMap(it.asReactMap())
                    }
                }
                promise.resolve(result)
            } catch (e: Exception) {
                promise.reject("RNSharedError", e)
            }
        }
        debugLog("getHttpTodos finished.")
    }

    @ReactMethod
    fun getHttpTodosAndSendEvent(taskId: String) {
        todoService.getHttpTodosAndSendEvent(taskId)
    }

    @ReactMethod
    fun addListener(eventName: String) {
    }

    @ReactMethod
    fun removeListeners(count: Int) {
    }

    private val todosReceivedCallback: (TodoServiceEventPayload) -> Unit = { payload ->
        debugLog("todosReceivedCallback called. payload: $payload")
        val jsPayload = Arguments.createMap().apply {
            putString("taskId", payload.taskId)
            putString("message", payload.message)
            putArray("todos", Arguments.createArray().apply {
                payload.todos.forEach {
                    pushMap(it.asReactMap())
                }
            })
        }
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(TodoService.EVENT_NAME_TODOS_RECEIVED, jsPayload)
    }

    init {
        todoService.registerSendEventCallback(todosReceivedCallback)
    }

    override fun getConstants(): MutableMap<String, Any>? {
        return mutableMapOf(
            "EVENT_NAME_TODOS_RECEIVED" to TodoService.EVENT_NAME_TODOS_RECEIVED
        )
    }
}
