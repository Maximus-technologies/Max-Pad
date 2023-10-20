plugins {
    id("com.android.library")
    id("kotlin-kapt")
    kotlin("android")
}

android {
    namespace = "com.max.firebase"
    compileSdk = Ext.compileSdkVersion
    buildToolsVersion = Ext.buildToolsVersion
    defaultConfig {
        minSdk = Ext.minSdkVersion
        proguardFile("consumer-rules.pro")
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "consumer-rules.pro"
            )
            buildConfigField("String", "remote_key", "\"${Ext.remoteKey}\"")
            buildConfigField("String", "application_id", "\"${Ext.applicationId}\"")
        }
        create("qa") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "consumer-rules.pro"
            )
            buildConfigField("String", "remote_key", "\"${Ext.remoteKey}\"")
            buildConfigField("String", "application_id", "\"${Ext.applicationId}\"")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "consumer-rules.pro"
            )
            buildConfigField("String", "remote_key", "\"${Ext.remoteKey}\"")
            buildConfigField("String", "application_id", "\"${Ext.applicationId}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(Libs.Koin.koin)
    api(Libs.Koin.koinAndroid)
    implementation(Libs.AndroidX.multiDex)
    implementation(platform(Libs.Firebase.firebaseBom))
    implementation(Libs.Firebase.firebaseAnalytics)
    implementation(Libs.Firebase.firebaseMessaging)
    implementation(Libs.Firebase.firebaseConfig)
    implementation(Libs.Firebase.firebaseCraslytics)
    implementation(Libs.Google.GSON)
    implementation(project(":lib"))
}