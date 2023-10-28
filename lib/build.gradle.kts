/*
 * Copyright (c) Maximus Technologies - 2023.
 * All Rights Reserved and Copy only allowed if given reference
 */

//@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    id("kotlin-kapt")
    kotlin("android")
}

android {
    namespace = "com.max.lib"
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
            buildConfigField("String", "sp", "\"${Ext.maxLocalName}\"")
            buildConfigField("String", "lct", "\"${Ext.tag}\"")
        }
        create("qa") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "consumer-rules.pro"
            )
            buildConfigField("String", "sp", "\"${Ext.maxLocalName}\"")
            buildConfigField("String", "lct", "\"${Ext.tag}\"")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "consumer-rules.pro"
            )
            buildConfigField("String", "sp", "\"${Ext.maxLocalName}\"")
            buildConfigField("String", "lct", "\"${Ext.tag}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    api(Libs.AndroidX.fragmentKtx)
    api(Libs.Koin.koin)
    api(Libs.Koin.koinAndroid)
    api(Libs.AndroidX.multiDex)
    api(Libs.Third.timber)
    api(Libs.Third.glide)
    api(Libs.Third.sdp)
    api(Libs.Third.ssp)
    api(Libs.Third.permissionX)
}


