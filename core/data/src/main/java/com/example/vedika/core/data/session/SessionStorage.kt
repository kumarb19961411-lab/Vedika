package com.example.vedika.core.data.session

import com.example.vedika.core.data.model.AccountMode

/**
 * Interface for persisting minimal session hints securely.
 */
interface SessionStorage {
    /**
     * Retrieves the last known account mode (USER or PARTNER).
     */
    fun getAccountMode(): AccountMode?

    /**
     * Persists the selected account mode.
     */
    fun saveAccountMode(mode: AccountMode)

    /**
     * Clears all persisted session data.
     */
    fun clearSession()
}
