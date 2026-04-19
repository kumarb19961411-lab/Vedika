---
title: Auth Decisions
type: notes
status: active
owner: Security Access Guard
phase: Phase 3
last_updated: 2026-04-20
tags: [notes, auth, security]
---

# Auth Decisions

## Source of truth
Ongoing tracker for authentication integration decisions.

## Current implementation
- Using generic PhoneAuth without custom claims at the moment.
- Users and vendors are bounded only by directory targeting.

## Future work
- Shift to Custom Claims on Cloud Functions to prevent spoofed `firestore.rules` queries based on path alone.
