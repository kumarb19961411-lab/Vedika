---
title: Backend Integration Notes
type: notes
status: active
owner: Firebase Backend & Data Model Engineer
phase: Phase 3
last_updated: 2026-04-20
tags: [notes, backend, database]
---

# Backend Integration Notes

## Source of truth
Scratchpad for mapping the Firebase Cloud Firestore `set()` calls.

## Current implementation
- `VendorRepository` currently bypasses live sync and only caches in memory logic.
- We need to translate the Vendor UI models into `backend_sync_contract.md` structures.

## Future work
- Set up automated emulators locally and add CI workflows to spin them up before PR asserts.
