---
last_updated: '2026-04-24T00:00:00.000Z'
phase: Phase 4 - Stabilization
status: active
tags:
  - guide
  - onboarding
  - discovery
  - flows
title: Flow Architecture Guide
---
# 🗺️ Flow Architecture: Onboarding & Discovery

This document maps the primary user journeys within Vedika, including onboarding for all roles and the discovery experience for consumers.

---

## 👋 Onboarding Flows

### 👤 Consumer Onboarding
1. **Entry**: Triggered after first-time OTP verification.
2. **Details**: Collects Name and optional Event Date.
3. **Persistence**: Writes to `users/{uid}`.
4. **Exit**: Redirects to `UserShell`.

### 🏢 Vendor Onboarding & Registration
1. **Journey**: Role Selection -> Business Profile -> Verification (Admin-driven).
2. **Steps**:
    - **Business Details**: Name, Category, Location.
    - **Verification**: ID/License upload (Optional in Phase 2).
    - **Persistence**: Writes to `vendors/{uid}` with `onboardingComplete = true`.
3. **Consumption**: Post-onboarding, screens consume the canonical Firestore stream via `vendorRepository.getVendorProfileStream(uid)`.

---

## 🔍 Consumer Discovery Flow

### 🗺️ Journey Overview
1. **Home**: `UserHomeScreen` displays categories and featured vendors.
2. **Browse**: `VendorBrowseScreen` allows filtering vendors by category.
3. **Detail**: `VendorDetailScreen` shows comprehensive vendor catalogs and reviews.
4. **Action**: User initiates an inquiry or booking.

### 🛠️ Technical Architecture
- **ViewModels**: `UserHomeViewModel`, `VendorBrowseViewModel`, and `VendorDetailViewModel` manage reactive state.
- **Deep Linking**: Supports `vedika://vendor/{vendorId}` for direct navigation to vendor profiles.
- **Verification**: Covered by `DiscoveryRegressionTest.kt` UI test suite.

---

## 🏗️ Technical Context
- **Backstack Management**: All flows use `popUpTo` logic to ensure clean navigation boundaries.
- **Mock Data**: A tiered system ensures consistent mock data across Browse and Detail views during development.

---
[[Project_Hub|🏠 Project Hub]] | [[User_Flow_Hub|👤 User Flow Hub]] | [[Vendor_Flow_Hub|🏢 Vendor Flow Hub]]
