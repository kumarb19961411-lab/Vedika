# 🛠️ Vedika Environment Tasks

This document tracks local environmental issues, configuration tasks, and development tool maintenance that are NOT blockers for the production codebase.

## 🟢 Open Tasks

### JDK / jlink Build Issue
- **Description**: The `assembleDebug` task or similar Gradle tasks may fail with a `jlink` error related to the JDK configuration in the local environment.
- **Context**: This appears to be a local toolchain mismatch and does not affect the correctness of the Kotlin/Compose code.
- **Status**: Tracked (Workaround: Use separate terminal or IDE-managed JDK if needed).
- **Owner**: Local Environment

## ⚪ Resolved Tasks
- (None)
