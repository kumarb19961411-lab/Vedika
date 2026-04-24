---
title: Backend Sync Contract
type: architecture
status: active
owner: Firebase Backend & Data Model Engineer
phase: Phase 3
last_updated: 2026-04-20
tags: [architecture, firebase, sync, hub-leaf]
---

# 🔄 Backend Sync Contract

## Principle
Vedika uses a "local-first UI, remote-second data state" operational philosophy. The application leverages a reactive cache for UI speed, backed by real-time Firestore synchronization.

## 📊 Collections & Schemas

### `vendors/{uid}`
- `businessName` (String): Public business identity.
- `category` (String): Partner service type (e.g., VENUE, DECORATOR).
- `onboardingComplete` (Boolean): Guard for dashboard access.
- `profileImageUrl` (String, optional): Reference to Firebase Storage asset.

### `users/{uid}`
- `name` (String): Consumer display name.
- `phone` (String): Normalized contact number.
- `onboardingComplete` (Boolean): Guard for customer profile setup.

## 🔄 Sync Behavior
Profile updates are written directly to Firestore and trigger a reactive snapshot listener refresh across all active ViewModels. Bookings and leads follow an identical subscription-based update pattern.

---
[[Project_Hub|🏠 Project Hub]] | [[Architecture_Hub|🏗️ Architecture Hub]] | [[Firebase_Hub|🔥 Firebase Hub]] | [[backend_sync_contract|Top]]
