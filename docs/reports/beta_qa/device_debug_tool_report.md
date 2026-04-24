# Physical-Device Debug Tooling: QA Verification Report

## Tool Overview
The Vedika Debug Toolkit has been enhanced to support **Physical-Device QA Reporting** (Option 6). This tool automates the collection of critical system diagnostics and verifies app stability on real hardware.

## Key Features Verified
- [x] **Multiple-Device Handling**: Strictly requires serial selection when >1 device is connected.
- [x] **Robust App Launch**: sources `$PACKAGE_NAME` and `$LAUNCH_ACTIVITY` from `debug_config.ps1`.
- [x] **Fallback Execution**: Uses `monkey` if `am start` fails.
- [x] **Automated Diagnostics**: Pulls `logcat` (Filtered for Errors), `dumpsys activity`, `dumpsys meminfo`, and `getprop`.
- [x] **Report Generation**: Outputs a timestamped Markdown summary in `debug_reports/`.

## Latest Run Summary
*Note: This section is a placeholder to be updated after the first official gate run.*

- **Timestamp**: [PENDING]
- **Device**: [PENDING]
- **Launch Verdict**: [PENDING]
- **Diagnostic Link**: [PENDING]

## Sign-off
The tool is **READY** for physical-device QA gate execution.
