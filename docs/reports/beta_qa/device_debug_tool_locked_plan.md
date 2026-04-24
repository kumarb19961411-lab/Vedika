# 🔒 Locked Implementation Plan: Physical-Device QA Debug Toolkit Enhancement

## Goal Description
Enhance the existing ADB debug toolkit (`tools/debug/`) to support a "Physical-Device QA Report" mode. This mode focuses on automated diagnostic collection (logs, dumpsys, device info) and test execution without screen recording, tailored for the final beta QA gate.

## Approved Amendments
1. **Multiple-Device Handling**:
   - The script will not default to the first device.
   - If multiple devices are detected, the user will be prompted to select a serial.
   - If no selection is made, the script will stop with a clear error message.
   - The selected serial will be used consistently via `adb -s <serial>`.
2. **Robust App Launch**:
   - Values for package and activity will be sourced from `debug_config.ps1`.
   - **Primary**: `adb shell am start -n <package>/<activity>`.
   - **Fallback**: `adb shell monkey -p <package> 1`.
   - Both attempts and their results will be documented in the final report.

## Proposed Changes

### [Component] ADB Debug Toolkit (`tools/debug/`)

#### [MODIFY] [run_debug_capture.ps1](file:///c:/Users/Welcome/Documents/GitHub/Vedika/tools/debug/run_debug_capture.ps1)
- Add menu option: `6. Run Device Test Report — No Screen Recording`.
- Call `run_device_test_report.ps1` upon selection.

#### [MODIFY] [session_helper.ps1](file:///c:/Users/Welcome/Documents/GitHub/Vedika/tools/debug/session_helper.ps1)
- Update `Get-ConnectedDevice` to strictly require selection when multiple devices are present.

#### [NEW] [run_device_test_report.ps1](file:///c:/Users/Welcome/Documents/GitHub/Vedika/tools/debug/run_device_test_report.ps1)
- **Connectivity**: Interactive serial selection if >1 device (via `session_helper.ps1`).
- **Diagnostics**: `logcat` (Full/Errors), `dumpsys` (Activity, Package, Window, Meminfo).
- **Automation**: Install APK (optional), Launch App (am start -> monkey), Run Android Tests (optional).
- **Outputs**: Timestamped folder in `debug_reports/` with Markdown report and JSON summary.

---

### [Component] Documentation

#### [MODIFY] [README.md](file:///c:/Users/Welcome/Documents/GitHub/Vedika/tools/debug/README.md)
- Document the new automated test report mode.

#### [MODIFY] [debug_capture_workflow.md](file:///c:/Users/Welcome/Documents/GitHub/Vedika/docs/guides/debug_capture_workflow.md)
- Integrate the physical-device report tool into the canonical workflow.

#### [NEW] [device_debug_tool_report.md](file:///c:/Users/Welcome/Documents/GitHub/Vedika/docs/reports/beta_qa/device_debug_tool_report.md)
- Placeholder for integrating tool results into project QA records.

---

## Verification Plan
1. **Syntax**: `powershell -Command "Get-Command -File .\tools\debug\run_device_test_report.ps1"`.
2. **No-Device**: Verify graceful failure when no device is connected.
3. **Multi-Device**: Verify script stops/prompts without defaulting.
4. **End-to-End**: Run report on physical device and verify file generation in `debug_reports/`.

## Risks and Rollback Notes
- **Risk**: ADB serial detection string parsing. **Mitigation**: Use robust regex for serial extraction.
- **Rollback**: Revert `run_debug_capture.ps1` and delete `run_device_test_report.ps1`.
