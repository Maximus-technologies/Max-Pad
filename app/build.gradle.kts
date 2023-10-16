@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.max.main"
    signingConfigs {
        create("release") {
            storeFile = file(project.property("RELEASE_STORE_FILE").toString())
            storePassword = project.property("RELEASE_STORE_PASSWORD").toString()
            keyPassword = project.property("RELEASE_KEY_PASSWORD").toString()
            keyAlias = project.property("RELEASE_KEY_ALIAS").toString()

            enableV3Signing = true
            enableV4Signing = true
        }

        getByName("debug") {
            storeFile = file("../signings/debug/signkey.jks")
            storePassword = "123456"
            keyPassword = "123456"
            keyAlias = "demo"

            enableV3Signing = true
            enableV4Signing = true
        }
    }
    compileSdk = Ext.compileSdkVersion
    buildToolsVersion = Ext.buildToolsVersion
    defaultConfig {
        applicationId = Ext.applicationId
        minSdk = Ext.minSdkVersion
        targetSdk = Ext.targetSdkVersion
        versionCode = Ext.versionCode
        versionName = Ext.versionName
        proguardFile("consumer-rules.pro")
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            applicationIdSuffix = ".dev"
            versionNameSuffix = ".dev"
            isDebuggable = true
            buildConfigField("String", "feedback_email", "\"${ExtDebug.feedbackEmail}\"")
            buildConfigField("String", "more_apps_link", "\"${ExtDebug.moreAppsLink}\"")
            buildConfigField("String", "privacy_policy_link", "\"${ExtDebug.privacyPolicyLink}\"")
            buildConfigField("String", "terms_and_conditions", "\"${ExtDebug.termsAndCondition}\"")
            manifestPlaceholders += mapOf("admob_id" to "\"${ExtDebug.admobId}\"")
            manifestPlaceholders += mapOf("app_name" to "\"${ExtDebug.applicationName}\"")

        }
        create("qa") {
            signingConfig = signingConfigs.getByName("debug")
            applicationIdSuffix = ".qa"
            versionNameSuffix = ".qa"
            isDebuggable = true
            buildConfigField("String", "feedback_email", "\"${ExtDebug.feedbackEmail}\"")
            buildConfigField("String", "more_apps_link", "\"${ExtDebug.moreAppsLink}\"")
            buildConfigField("String", "privacy_policy_link", "\"${ExtDebug.privacyPolicyLink}\"")
            buildConfigField("String", "terms_and_conditions", "\"${ExtDebug.termsAndCondition}\"")
            manifestPlaceholders += mapOf("admob_id" to "\"${ExtDebug.admobId}\"")
            manifestPlaceholders += mapOf("app_name" to "\"${ExtDebug.applicationName}\"")

        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "consumer-rules.pro"
            )
            applicationIdSuffix = ".release"
            isDebuggable = false
            buildConfigField("String", "feedback_email", "\"${Ext.feedbackEmail}\"")
            buildConfigField("String", "more_apps_link", "\"${Ext.moreAppsLink}\"")
            buildConfigField("String", "privacy_policy_link", "\"${Ext.privacyPolicyLink}\"")
            buildConfigField("String", "terms_and_conditions", "\"${Ext.termsAndCondition}\"")
            manifestPlaceholders += mapOf("admob_id" to "\"${Ext.admobId}\"")
            manifestPlaceholders += mapOf("app_name" to "\"${Ext.applicationName}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
    configurations {
        implementation {
            exclude(group = "org.jetbrains", module = "annotations")
        }
    }
}


dependencies {
    implementation(project(":lib"))
    implementation(project(":network"))
    implementation(project(":database"))
    implementation(project(":firebase"))
    implementation(project(":ads"))
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.constraintLayout)
    implementation(Libs.AndroidX.recyclerview)
    implementation(Libs.AndroidX.multiDex)
    //
    implementation(Libs.Kotlin.stdlib)
}
