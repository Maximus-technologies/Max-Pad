object Ext {
    const val applicationName = "Demo Project"
    const val applicationId = "com.max"
    const val minSdkVersion = 24
    const val compileSdkVersion = 33
    const val buildToolsVersion = "33.0.2"
    const val targetSdkVersion = 33
    const val versionCode = 1
    const val versionName = "1.0"
    const val feedbackEmail = "demo@demo.com"
    const val maxLocalName = "maxLocal"
    const val moreAppsLink = "https://google.com"
    const val privacyPolicyLink = "https://demo.privacy.com"
    const val termsAndCondition = "https://demo.terms.com"
    const val admobId = "2343432"
}

object ExtDebug {
    const val applicationName = "Demo Project"
    const val applicationId = "com.max.main"
    const val minSdkVersion = 24
    const val compileSdkVersion = 33
    const val buildToolsVersion = "33.0.2"
    const val targetSdkVersion = 33
    const val versionCode = 1
    const val versionName = "1.0"
    const val feedbackEmail = "demo@demo.com"
    const val maxLocalName = "maxLocal"
    const val moreAppsLink = "https://google.com"
    const val privacyPolicyLink = "https://demo.privacy.com"
    const val termsAndCondition = "https://demo.terms.com"
    const val admobId = "2343432"
}

object Libs {

    object Version {
        const val agp = "8.0.1"
        const val kotlin = "1.8.21"
        const val koin = "3.5.0"
        const val multiDex = "2.0.1"
        const val room = "2.5.2"
        const val firebase = "32.3.1"
        const val google = "4.4.0"
    }

    object Plugin {
        const val AGP = "com.android.tools.build:gradle:${Version.agp}"
        const val KGP = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}"
    }

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.kotlin}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.6.1"
        const val coreKtx = "androidx.core:core-ktx:1.10.0"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.5.7"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.3.0"
        const val multiDex = "androidx.multidex:multidex:${Version.multiDex}"
    }

    object Firebase {
        const val firebaseBom = "com.google.firebase:firebase-bom:${Version.firebase}"
        const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    }

    object Koin {
        const val koin = "io.insert-koin:koin-core:${Version.koin}"
        const val koinAndroid = "io.insert-koin:koin-android:${Version.koin}"
    }

    object Room {
        const val roomRuntime = "androidx.room:room-runtime:${Version.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Version.room}"
        const val roomKtx = "androidx.room:room-ktx:${Version.room}"
    }

    object Squareup {
        private const val retrofit2_version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$retrofit2_version"
        const val converter = "com.squareup.retrofit2:converter-gson:$retrofit2_version"
        const val log = "com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11"
    }

    object Third {
        const val timber = "com.jakewharton.timber:timber:5.0.1"
    }

    val deps = arrayOf(
        Plugin.AGP,
        Plugin.KGP,
        Kotlin.stdlib,
        Kotlin.coroutines,
        AndroidX.appcompat,
        AndroidX.coreKtx,
        AndroidX.fragmentKtx,
        AndroidX.constraintLayout,
        AndroidX.recyclerview,
        Squareup.retrofit,
        Squareup.converter,
        Squareup.log,
        Third.timber,
    )
}
