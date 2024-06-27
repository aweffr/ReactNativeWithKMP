import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.native.cocoapods)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "0.1.0"
        ios.deploymentTarget = "12"
        framework {
            isStatic = true
            baseName = "shared"
            binaryOption("bundleId", "com.aweffr.shared")
            embedBitcode(BitcodeEmbeddingMode.BITCODE)
        }
    }

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
        }
    }
}

android {
    namespace = "com.aweffr.sharedmodule.shared"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    defaultConfig {
        minSdk = rootProject.extra["minSdkVersion"] as Int
    }
}

tasks.register("iosDebugPodBuild") {
    dependsOn("iosSimulatorArm64Binaries", "podPublishDebugXCFramework")
}
