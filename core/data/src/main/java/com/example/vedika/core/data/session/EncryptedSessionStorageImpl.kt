package com.example.vedika.core.data.session

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.vedika.core.data.model.AccountMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedSessionStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SessionStorage {

    companion object {
        private const val PREFS_NAME = "vedika_secure_session"
        private const val KEY_ACCOUNT_MODE = "last_known_account_mode"
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun getAccountMode(): AccountMode? {
        val modeStr = sharedPreferences.getString(KEY_ACCOUNT_MODE, null)
        return modeStr?.let { 
            try {
                AccountMode.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }

    override fun saveAccountMode(mode: AccountMode) {
        sharedPreferences.edit()
            .putString(KEY_ACCOUNT_MODE, mode.name)
            .apply()
    }

    override fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }
}
