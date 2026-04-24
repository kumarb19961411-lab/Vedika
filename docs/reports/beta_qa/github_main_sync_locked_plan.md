# 🛡️ Locked Plan: GitHub Main Synchronization & Cleanup

- **Owner**: Senior Android Release Engineer / QA Owner
- **Status**: LOCKED
- **Gate**: CONDITIONAL GO TO BETA BUG BASH
- **Sync Date**: April 2026

## 📋 Goal
Synchronize local repository state with GitHub Main requirements. This includes hardening the debug toolkit for portability, aligning documentation to the **CONDITIONAL GO TO BETA BUG BASH** status, and consolidating the documentation base without compromising security or historical records.

## ⚖️ Mandatory Constraints
1. **Status**: Must remain `CONDITIONAL GO TO BETA BUG BASH`. The project is NOT "Beta Ready".
2. **Security Rules**: `firestore.rules` and `storage.rules` will NOT be modified in this task. Rule remediation is a separate workstream.
3. **Legacy Preservation**: Documentation will be archived to `docs/archive/` rather than being deleted broadly. Redundant files (exact duplicates) will be consolidated.
4. **Data Privacy**: No relaxation of inventory or blocked_dates reads. Remediation will prefer a safe `/public_vendors/` model in the future.
5. **Debug Portability**: Tools must detect ADB automatically and support local overrides without checking in local paths.

## 🛠️ Proposed Changes

### A. Debug Toolkit Hygiene
- **[MODIFY] `tools/debug/session_helper.ps1`**: Add `Get-AdbPath` function to find ADB via `$env:ANDROID_HOME`, common paths, or `where.exe`.
- **[MODIFY] `tools/debug/debug_config.ps1`**: Replace hardcoded path with `Get-AdbPath` call. Preserve `.local_config.ps1` override.
- **[MODIFY] `tools/debug/run_device_test_report.ps1`**:
    - **PASS**: Success + No fatals + Package detected + Diagnostics complete.
    - **CONDITIONAL_PASS**: Success with minor non-blocking errors.
    - **FAIL**: Crash/ANR or launch failure.
    - Defer Push Notification checklist to Phase 5.
    - Ensure all logs/dumpsys files are linked in the final report.

### B. Documentation Synchronization
- **[MODIFY] `README.md`**: Set status to `CONDITIONAL GO TO BETA BUG BASH`. Set OTP to `123456`.
- **[MODIFY] `docs/SYSTEM_STATUS.md`**: Update to reflect:
    - Non-device QA: Passed with conditions.
    - Physical-device QA: Conditional pass.
    - Final remediation: Pending.
- **[CLEANUP] Documentation Base**:
    - Consolidate `docs/docs/` into `docs/`.
    - Archive stray phase docs to `docs/archive/`.
    - Fix broken wikilinks in `Project_Hub.md` and `SYSTEM_STATUS.md`.

### C. Discovery Module Status
- **Actual State**: `:feature:discovery` exists and is included in `settings.gradle.kts`.
- **Verification**: Will be verified via `./gradlew :feature:discovery:assembleDebug`.

### D. Security Remediation (Plan Only)
- **[NEW] `docs/reports/beta_qa/firebase_rules_remediation_plan.md`**:
    - Proposed model: `/public_vendors/{vendorId}` for discovery read access.
    - Constraint: No public access to private operational/KYC data.
    - Requirement: Owner-only writes for all collections.

## 🧪 Verification Plan
- `./gradlew clean assembleDevDebug`
- `./gradlew testDebugUnitTest`
- `./gradlew lintDevDebug`
- `./gradlew :feature:discovery:assembleDebug`
- PowerShell syntax validation.

## 📦 Final Status
- **SYNC CLEANUP COMPLETE** (Pending Execution)
