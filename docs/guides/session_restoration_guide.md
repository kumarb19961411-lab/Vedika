---
title: Session Restoration Guide
type: guide
status: active
owner: Android Compose Engineer
phase: Phase 3
last_updated: 2026-04-20
tags: [guide, auth, session, restoration]
---

# Session Restoration Guide

## Source of truth
Defines the implementation logic for restoring user sessions without requiring them to re-enter OTP. Based on the [[session_restoration]] architecture.

## Current implementation

### Local Persistence
Save `token` via `EncryptedSharedPreferences` on first load.
Check this on `SplashViewModel` load to determine immediate routing to dashboard, bypassing OTP entry altogether for active sessions.

## Future work
- Enhance security validations on token restoration.
