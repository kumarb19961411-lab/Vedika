package com.example.vedika

import android.app.Application
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.util.Log

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase first to ensure all sub-systems (like App Check) are ready.
        Firebase.initialize(this)

        // Opt-in Emulator Routing for non-production variants.
        // Initialize App Check with Debug Provider for all debug builds.
        // This allows physical devices to verify via debug tokens in the Firebase Console.
        if (BuildConfig.DEBUG) {
            Log.d("VedikaDebug", "App Check Debug Provider Installation Started")
            try {
                Firebase.appCheck.installAppCheckProviderFactory(
                    DebugAppCheckProviderFactory.getInstance()
                )
                Log.d("VedikaDebug", "App Check Debug Provider Installed Successfully")
                
                // Forced heartbeat: Fetch token immediately to trigger the Logcat output
                // of the debug secret.
                @Suppress("OptInUsageInspection")
                GlobalScope.launch {
                    try {
                        Firebase.appCheck.getToken(false)
                        Log.d("VedikaDebug", "App Check Heartbeat: Token request triggered.")
                    } catch (e: Exception) {
                        Log.e("VedikaDebug", "App Check Heartbeat failed: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                Log.w("VedikaDebug", "App Check warning: ${e.message}")
            }
        }

        // Opt-in Emulator Routing only if explicitly requested.
        // Note: For physical devices, ensuring you use the staging/prod flavor (or skipping this) 
        // prevents connection errors to 10.0.2.2.
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
