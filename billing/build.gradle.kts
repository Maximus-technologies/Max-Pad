plugins {
    id("com.android.library")
    id("kotlin-kapt")
    kotlin("android")
}

android {
    namespace = "com.max.billing"
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
            buildConfigField("String", "in_app_key", "\"${Ext.inAppKey}\"")
            buildConfigField("String", "application_id", "\"${Ext.applicationId}\"")
        }
        create("qa") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "consumer-rules.pro"
            )
            buildConfigField("String", "in_app_key", "\"${Ext.inAppKey}\"")
            buildConfigField("String", "application_id", "\"${Ext.applicationId}\"")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "consumer-rules.pro"
            )
            buildConfigField("String", "in_app_key", "\"${Ext.inAppKey}\"")
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
    implementation(project(":lib"))
    implementation(Libs.AndroidX.billing)
}