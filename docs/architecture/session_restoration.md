# Session Restoration Strategy

## Core Objective
Drive perceived application loading latency to absolute zero for authenticated, returning application users.

## 1. Local Cache Mechanism
The core driver of session restoration is the `EncryptedSharedPreferences` instance initialized securely upon application boot.
- Variables Cached:
  1. `accessToken` (JWT string representing the current live session)
  2. `refreshToken` (Auth string allowing renewal of the access token)
  3. `uid` (The absolute structural ID pointer)
  4. `cachedRole` (Enum String stating `VENDOR` or `USER`)

## 2. Lifecycle Evaluation
1. **App Launch Initialization:** `MainActivity` inflates and invokes `SplashViewModel`.
2. **State Validation:** `SplashViewModel` reads local state synchronously.
3. **Execution Branch - Success:** If `accessToken` parses valid against local timestamp metrics, navigation executes a `popUpTo(0)` wiping auth histories entirely and routes directly to the UI layer matching `cachedRole`.
4. **Execution Branch - Renewal Needed:** If local logic flags token staleness, it silently hits Firebase Auth via `refreshToken` exchange in a background dispatcher. If successful, state updates and routes to Dashboard.
5. **Execution Branch - Critical Miss:** If `refreshToken` fails, or `accessToken` is null/empty, application completely transitions to the initial `AuthIntro` screens.

## 3. Data Sensitivity Rules
At no point do `users/{uid}` passwords or PI metrics map into the internal preferences store. Only generic role and identity coordinates are preserved here. Wiping user preferences inside Settings -> App Data natively purges this logic and successfully de-authenticates.
