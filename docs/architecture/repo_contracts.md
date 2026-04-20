---
title: Repository Contracts
type: architecture
status: active
owner: All
phase: Phase 3
last_updated: 2026-04-21
tags: [architecture, data, contracts, repository]
---

# 📜 Repository Contracts

This document defines the formal architectural contracts for data repositories in the `:core:data` module.

## 🏛️ General Requirements

1. **Interface Aggregation**: All feature-facing capabilities must be exposed via an interface (e.g., `BookingRepository`).
2. **Dependency Inversion**: Feature modules (`:feature:*`) must only depend on the interfaces, never the implementations.
3. **Data Mapping**: Repositories must map raw Firebase/API models into Clean Domain models before returning them.

## 🖋️ Standard Signatures

### 1. One-Shot Operations
- **Pattern**: `suspend fun doAction(...): Result<T>`
- **Usage**: Sign-up, creating a booking, updating a profile.
- **Rule**: Always wrap in `Result` to force the UI layer to handle success/failure states explicitly.

### 2. Streaming Data
- **Pattern**: `fun getObserver(...): Flow<T>`
- **Usage**: Dashboard metrics, live booking lists, inventory status.
- **Rule**: These should NOT be `suspend` functions. They return a stream that the UI collects.

## 📦 Specific Contracts

### `AuthRepository`
- `getCurrentUserId(): String?` - Synchronous check of the current session.
- `observeAuthState(): Flow<UserAuthStatus>` - Live stream of auth transitions.

### `VendorRepository`
- `getCurrentVendorId(): String` - Assumes authenticated context; throws if not found.
- `getVendorProfileStream(uid: String): Flow<VendorProfile>` - Real-time profile updates.

### `BookingRepository`
- `getBookingsForVendor(vendorId: String): Flow<List<Booking>>` - Primary revenue/metric source.

---
[[backend_integration_blueprint|🧱 Backend Blueprint]] | [[dev_fake_repository_parity|💾 Fake Repo Parity]]
