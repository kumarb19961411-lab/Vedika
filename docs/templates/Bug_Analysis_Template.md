---
title: "Bug: {{title}}"
created: {{date}}
tags: [bug, "status/investigating"]
severity: High
---
# Bug: {{title}}
**Stability Impact**: 🔴 High
**Status**: 🔎 Investigating

## Description
What is the bug? What is the observed behavior compared to the expected behavior?

## Reproduction Steps
1. Navigate to...
2. Perform action...
3. Result: ...
4. Expected: ...

## Environment Details
- **Build**: devDebug / stagingDebug / release
- **Device**: Emulator or specific physical device
- **Backend Status**: Reaching Firebase or Emulator?

## Root Cause Analysis
Explain the exact layer (View, ViewModel, Domain, Data) where the bug originates.

## Proposed Code Fix
- Which files will be touched?
- Link to [[docs/android_build_regression_guard|Regression Guard]] for build rules.

## Verification
- [ ] Manual regression block tested
- [ ] CI execution clean
