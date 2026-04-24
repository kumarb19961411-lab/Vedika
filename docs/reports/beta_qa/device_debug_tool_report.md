# 🛠️ Physical-Device QA Tooling Status

This document tracks the status and integration of the automated physical-device QA reporting tool.

## Tool Overview
- **Command**: `.\tools\debug\run_device_test_report.ps1`
- **Purpose**: Collects diagnostics without screen recording for physical-device QA gates.
- **Output**: Generates timestamped reports in `debug_reports/device_test_<timestamp>/`.

## Integration Status
- [x] Multi-device handling implemented (Interactive serial selection).
- [x] Robust app launch implemented (am start + monkey fallback).
- [x] Diagnostic collection implemented (Logcat, Dumpsys).
- [x] Integration with `run_debug_capture.ps1` menu complete.

## Sample Report Output Structure
When executed, the tool generates a `device_test_report.md` with the following sections:
- **Device Metadata**: Model, Serial, Android Version.
- **Launch Results**: Success/Failure status of the app launch sequence.
- **Diagnostics Summary**: Links to full logs and memory dumps.
- **Log Observations**: Top 10 errors found in the logcat.
- **Manual Checklist**: Verification steps for physical hardware (sensors, deep-links).

## Next Steps for Testers
1. Connect physical device via USB or Wi-Fi.
2. Run `.\tools\debug\run_debug_capture.ps1`.
3. Select Option `6` (Run Device Test Report).
4. Review the generated folder in `debug_reports/`.
5. Attach the `smoke_test_results.json` to the Beta Readiness ticket.

---
*Last Updated: 2026-04-24*
