# Vedika Debug Capture Toolkit

Practical, script-based toolkit for capturing high-fidelity debug data from physical Android devices over Wi-Fi debugging.

## Quick Start (Main Entry)
The toolkit is designed to be run via the interactive wrapper. Use PowerShell:
```powershell
.\run_debug_capture.ps1
```

## Available Scripts
- **`start_debug_capture.ps1`**: Initializes a session and begins background capture.
- **`stop_debug_capture.ps1`**: Finalizes capture and pulls all media to a timestamped folder.
- **`take_screenshot.ps1`**: Snap a high-res screenshot directly into the active session.
- **`generate_bugreport.ps1`**: Trigger a full system diagnostic.
- **`run_device_test_report.ps1`**: Automated diagnostic collection and app launch verification (No Video).
- **`open_latest_report.ps1`**: Instantly browse the results.

## Configuration
Edit `debug_config.ps1` to customize:
- `AutoEnableShowTaps`: Visualize touches on video.
- `ClearLogcatOnStart`: Ensure clean logs for every reproduction.
- `ADB_PATH`: Custom path to Android platform-tools if not in PATH.

## Documentation
For the full setup guide and Wi-Fi debugging instructions, see:
[docs/debug_capture_workflow.md](../../docs/debug_capture_workflow.md)
