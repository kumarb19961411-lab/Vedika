package com.example.vedika

import android.app.Application
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Opt-in Emulator Routing for non-production variants.
        if (BuildConfig.USE_FIREBASE_EMULATOR) {
            try {
                Firebase.auth.useEmulator("10.0.2.2", 9099)
                Firebase.firestore.useEmulator("10.0.2.2", 8080)
            } catch (e: Exception) {
                // Ignore if already configured
            }
        }
    }
}
