plugins {
    id("com.android.library")
    id("kotlin-kapt")
    kotlin("android")
    id("com.google.gms.google-services")
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
            buildConfigField("String", "remote_key", "\"${Ext.maxLocalName}\"")
            buildConfigField("String", "application_id", "\"${Ext.applicationId}\"")
        }
        create("qa") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "consumer-rules.pro"
            )
            buildConfigField("String", "remote_key", "\"${Ext.maxLocalName}\"")
            buildConfigField("String", "application_id", "\"${Ext.applicationId}\"")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "consumer-rules.pro"
            )
            buildConfigField("String", "remote_key", "\"${Ext.maxLocalName}\"")
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
}