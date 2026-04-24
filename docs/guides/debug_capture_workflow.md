# Vedika V2 — Debug Capture Workflow

This guide explains how to use the automated toolkit to capture high-fidelity debug reports from physical Android devices.

## Prerequisites
1. **Android Studio** installed.
2. **ADB** added to your System PATH (or configured in `tools/debug/debug_config.ps1`).
3. A physical Android device with **Developer Options** enabled.
4. (Recommended) **Wi-Fi Debugging** set up for a cable-free experience.

## Phone Setup
To ensure your screen recordings are useful, follow these steps on your phone:
1. Go to **Settings > About Phone** and tap **Build Number** 7 times.
2. Go to **Settings > System > Developer Options**.
3. Enable **Wi-Fi Debugging** (if on a Wi-Fi network).
4. (Optional) Enable **Show Taps** to visualize your interaction on the recording.
    *   *Note: The Vedika Toolkit can enable/disable this automatically on start/stop.*

## Using the Toolkit

The toolkit is located in the `tools/debug/` directory.

### Quick Start (Interactive Menu)
```powershell
.\tools\debug\run_debug_capture.ps1
```

### When to Use Which Mode?
*   **Video Capture (Option 1)**: Best for reproducing UI bugs, navigation issues, or providing a walkthrough for developers.
*   **Device Test Report (Option 6)**: Best for final sign-off, verifying installation stability, checking for silent backend errors (logcat), and generating formal QA records for Beta readiness.

### Manual Commands
If you prefer direct command execution:

| Action | Command |
| :--- | :--- |
| **Start Capture** | `.\tools\debug\start_debug_capture.ps1` |
| **Stop Capture** | `.\tools\debug\stop_debug_capture.ps1` |
| **Take Screenshot** | `.\tools\debug\take_screenshot.ps1` |
| **Generate Bugreport** | `.\tools\debug\generate_bugreport.ps1` |
| **Physical-Device QA Report** | `.\tools\debug\run_device_test_report.ps1` |
| **Open Reports** | `.\tools\debug\open_latest_report.ps1` |

---

## Capture Bundle Content
### 1. Standard Capture (Video + Logs)
A successful capture session creates a folder in `debug_reports/YYYYMMDD_HHMMSS/` containing:

- `session_video.mp4`: A screen recording of your interaction (**3-minute limit**).
- `logcat.txt`: Real-time system and app logs, including `VedikaDebug` tags.
- `device_info.txt`: Hardware and Android OS version details.
- `screenshots/`: Any screenshots taken during the session.
- `bugreport.zip`: (Optional) Comprehensive system state diagnostic.

### 2. Physical-Device QA Report (Diagnostics Only)
The automated report mode creates a folder in `debug_reports/device_test_YYYYMMDD_HHMMSS/` containing:

- `device_test_report.md`: A structured summary of the device state and launch result.
- `logcat_full.txt`: Full device logs.
- `logcat_errors.txt`: Filtered logcat containing only `E` (Error) and `F` (Fatal) levels.
- `app_launch_log.txt`: Detailed log of the `am start` and `monkey` launch attempts.
- `dumpsys_*.txt`: Memory, activity, and package state dumps.
- `smoke_test_results.json`: Machine-readable summary for QA tracking.

## Troubleshooting

### ADB Not Found
If the script fails to find `adb`, update the `$ADB_PATH` in `tools/debug/debug_config.ps1` to the absolute path of your platform-tools folder.

### No Devices Found
Ensure your phone is connected and "Allow USB Debugging?" prompt has been accepted on the device screen. Run `adb devices` to check.

### Wi-Fi Debugging Disconnects
Wi-Fi debugging can be unstable on some networks. If it disconnects, the toolkit may fail to pull the video. Use a USB cable for critical high-length capture sessions.

### Screen Recording Limit
Android's native `screenrecord` tool has a **3-minute hard limit**. If your repro takes longer, start a second capture session or perform the repro in shorter chunks.
