# 🕉️ Vedika: Elevate Your Service Discovery

![Vedika Banner](resources/brand/banner.png)

**Vedika** is a premium, multi-module Android application designed to bridge the gap between service vendors and consumers. Built with a modern, reactive architecture, Vedika provides a seamless experience for discovering, booking, and managing services with transactional integrity.

---

## 🚀 Beta Status: CONDITIONAL GO
The project has successfully passed the **Physical Device QA Gate** (Android API 35). 
Current phase: **Final Stabilization & Beta Bug Bash**.

---

## ✨ Key Features

### 🏢 For Vendors
- **Unified Dashboard**: At-a-glance metrics for bookings, revenue, and inventory health.
- **Smart Inventory Management**: Real-time availability tracking with double-submission protection and conflict detection.
- **Role-Based Workflows**: Tailored onboarding and management screens designed for professional operations.

### 👤 For Consumers
- **Discover & Explore**: A reactive discovery engine to find high-quality local vendors.
- **Instant Bookings**: Seamless booking flow with real-time feedback and premium success UX.
- **Deep Link Integration**: Instant access to vendor profiles via `vedika://app/vendor/{id}`.

### 🛡️ Security & Reliability
- **Firebase App Check**: Hardened backend security to protect against unauthorized traffic.
- **Transactional Integrity**: Ensuring every booking and inventory update is atomic and consistent.
- **Build Regression Guard**: A robust suite of unit and lint tests ensuring every release is production-ready.

---

## 🏗️ Architecture & Tech Stack

Vedika is engineered for scalability and maintainability using **Clean Architecture** principles.

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) for a modern, declarative UI.
- **Programming Language**: [Kotlin](https://kotlinlang.org/) with Coroutines & Flow for reactive state management.
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/) for robust modular DI.
- **Backend**: [Firebase](https://firebase.google.com/) (Authentication, Firestore, Storage, App Check).
- **Navigation**: Type-safe Jetpack Navigation with Deep Link support.

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Iguana (or newer)
- JDK 17
- Firebase Project Configuration (`google-services.json`)

### Build Commands
```bash
# Assemble the debug build
./gradlew assembleDevDebug

# Run unit tests across all modules
./gradlew testDebugUnitTest

# Run quality checks
./gradlew lintDevDebug
```

---

## 📚 Documentation & Knowledge Base

Vedika maintains a comprehensive **Obsidian-ready** documentation vault.

- **[🕉️ Project Hub](docs/hubs/Project_Hub.md)**: The central entry point for all documentation.
- **[📊 System Status](docs/SYSTEM_STATUS.md)**: Real-time feature checklist and build health.
- **[🚦 QA Gate Summary](docs/reports/beta_qa/physical_device_qa_gate_summary.md)**: Final results from the Physical Device QA Gate.
- **[🐞 Defect Log](docs/reports/beta_qa/defect_log.md)**: Current known issues and fixes.

---
Built with ❤️ by the Vedika Engineering Team.
