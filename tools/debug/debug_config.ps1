# Vedika Debug Capture Toolkit Config
# Use this file to customize your local debug environment.

# ADB Execution
$ADB_PATH = "C:\Users\Welcome\AppData\Local\Android\Sdk\platform-tools\adb.exe" # Discovered local path

# Application Details
$PACKAGE_NAME = "com.example.vedika"

# Debug Capture Settings
$REPORT_ROOT = "debug_reports"
$DEVICE_VIDEO_PATH = "/downloads/vedika_debug.mp4"

# Developer Visuals (Visualizing taps and pointers)
$AutoEnableShowTaps = $true
$AutoEnablePointerLocation = $false
$RestoreDeveloperVisualSettingsOnStop = $true

# Automation Settings
$AutoGenerateBugreportOnStop = $false
$ClearLogcatOnStart = $true

# Helper to ensure we don't accidentally check in local paths
$LocalConfigPath = Join-Path $PSScriptRoot ".local_config.ps1"
if (Test-Path $LocalConfigPath) {
    . $LocalConfigPath
}
