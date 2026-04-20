package com.example.vedika

import android.app.Application
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase first to ensure all sub-systems (like App Check) are ready.
        Firebase.initialize(this)

        // Opt-in Emulator Routing for non-production variants.
        if (BuildConfig.USE_FIREBASE_EMULATOR) {
            try {
                // Initialize App Check with Debug Provider for development/emulator use.
                // Rule: This must happen before other Firebase service calls.
                Firebase.appCheck.installAppCheckProviderFactory(
                    DebugAppCheckProviderFactory.getInstance()
                )

                Firebase.auth.useEmulator("10.0.2.2", 9099)
                Firebase.firestore.useEmulator("10.0.2.2", 8080)
            } catch (e: Exception) {
                // Ignore if already configured
            }
        }
    }
}
