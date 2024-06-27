buildscript {
    extra.apply {
        set("buildToolsVersion", "34.0.0")
        set("minSdkVersion", 24)
        set("compileSdkVersion", 34)
        set("targetSdkVersion", 34)
        set("ndkVersion", "25.1.8937393")
        set("kotlinVersion", "1.9.24")
    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.facebook.react:react-native-gradle-plugin")
    }
}

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlin.native.cocoapods) apply false
}

apply(plugin = "com.facebook.react.rootproject")


