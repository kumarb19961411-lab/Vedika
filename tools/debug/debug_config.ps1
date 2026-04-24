# Vedika Debug Capture Toolkit Config
# Use this file to customize your local debug environment.

function Get-AdbPath {
    # 1. Check system PATH first
    $adbCmd = Get-Command adb -ErrorAction SilentlyContinue
    if ($null -ne $adbCmd -and (Test-Path $adbCmd.Source)) {
        return $adbCmd.Source
    }

    # 2. Check ANDROID_HOME environment variable
    if ($env:ANDROID_HOME) {
        $path = Join-Path $env:ANDROID_HOME "platform-tools\adb.exe"
        if (Test-Path $path) { return $path }
    }

    # 3. Fallback to generic command
    return "adb"
}

# ADB Execution
$ADB_PATH = Get-AdbPath

# Application Details
$PACKAGE_NAME = "com.example.vedika"
$LAUNCH_ACTIVITY = ".MainActivity"

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
