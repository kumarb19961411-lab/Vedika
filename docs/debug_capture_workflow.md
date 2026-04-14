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
The easiest way to use the toolkit is via the interactive wrapper:
```powershell
.\tools\debug\run_debug_capture.ps1
```

### Manual Commands
If you prefer direct command execution:

| Action | Command |
| :--- | :--- |
| **Start Capture** | `.\tools\debug\start_debug_capture.ps1` |
| **Stop Capture** | `.\tools\debug\stop_debug_capture.ps1` |
| **Take Screenshot** | `.\tools\debug\take_screenshot.ps1` |
| **Generate Bugreport** | `.\tools\debug\generate_bugreport.ps1` |
| **Open Reports** | `.\tools\debug\open_latest_report.ps1` |

---

## Capture Bundle Content
A successful capture session creates a folder in `debug_reports/YYYYMMDD_HHMMSS/` containing:

- `session_video.mp4`: A screen recording of your interaction (**3-minute limit**).
- `logcat.txt`: Real-time system and app logs, including `VedikaDebug` tags.
- `device_info.txt`: Hardware and Android OS version details.
- `screenshots/`: Any screenshots taken during the session.
- `bugreport.zip`: (Optional) Comprehensive system state diagnostic.

## Troubleshooting

### ADB Not Found
If the script fails to find `adb`, update the `$ADB_PATH` in `tools/debug/debug_config.ps1` to the absolute path of your platform-tools folder.

### No Devices Found
Ensure your phone is connected and "Allow USB Debugging?" prompt has been accepted on the device screen. Run `adb devices` to check.

### Wi-Fi Debugging Disconnects
Wi-Fi debugging can be unstable on some networks. If it disconnects, the toolkit may fail to pull the video. Use a USB cable for critical high-length capture sessions.

### Screen Recording Limit
Android's native `screenrecord` tool has a **3-minute hard limit**. If your repro takes longer, start a second capture session or perform the repro in shorter chunks.
