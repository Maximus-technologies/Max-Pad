package com.max.firebase.messaging

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.max.firebase.BuildConfig

object MaxFCM {
    fun init() {
        Firebase.messaging.subscribeToTopic(BuildConfig.application_id)
    }
}