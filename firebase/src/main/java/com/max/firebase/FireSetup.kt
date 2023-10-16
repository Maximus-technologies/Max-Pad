package com.max.firebase

import android.content.Context
import com.google.firebase.FirebaseApp

class FireSetup {
    fun initialize(context: Context) {
        FirebaseApp.initializeApp(context)
    }
}