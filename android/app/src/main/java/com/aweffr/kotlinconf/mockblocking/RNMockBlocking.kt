package com.aweffr.kotlinconf.mockblocking

import android.util.Log
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.uimanager.ViewManager

private const val TAG = "RNMockBlocking"

class RNMockBlockingPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(RNMockBlockingModule(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}

class RNMockBlockingModule(val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return TAG
    }

    @ReactMethod
    fun mockBlocking(duration: Int) {
        // run on ui thread
        reactContext.currentActivity?.runOnUiThread {
            Log.i(TAG, "mockBlocking: run on ui thread! duration: $duration(ms)")
            Thread.sleep(duration.toLong())
            Log.i(TAG, "mockBlocking: done!")
        }
    }
}