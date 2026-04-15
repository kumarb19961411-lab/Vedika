# Vedika Start Debug Capture
# Initializes a new debugging session, starts logging, and begins screen recording.

$ErrorActionPreference = "Stop"
. "$PSScriptRoot\debug_config.ps1"
. "$PSScriptRoot\session_helper.ps1"

Write-Host "--- Vedika Debug Capture: Starting ---" -ForegroundColor Cyan

# 1. Device Verification
$serial = Get-ConnectedDevice
if ($null -eq $serial) {
    Write-Host "Error: No device found. Connect via USB or Wi-Fi Debugging first." -ForegroundColor Red
    exit 1
}
Write-Host "Device: $serial"

# 2. Session Initialization
$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$reportDir = Join-Path (Resolve-Path "$PSScriptRoot\..\..\$REPORT_ROOT") $timestamp
if (!(Test-Path $reportDir)) { New-Item -ItemType Directory -Path $reportDir | Out-Null }

Write-Host "Report Directory: $reportDir"

# 3. Store Original Settings
$origShowTaps = Get-DeviceSetting $serial "system" "show_touches"
$origPointerLoc = Get-DeviceSetting $serial "system" "pointer_location"

$session = @{
    Timestamp = $timestamp
    ReportPath = $reportDir
    DeviceSerial = $serial
    OriginalShowTaps = $origShowTaps
    OriginalPointerLocation = $origPointerLoc
    LogcatPid = $null
    StartTime = (Get-Date).ToString("o")
}

# 4. Apply Debug Visuals
if ($AutoEnableShowTaps) {
    Write-Host "Enabling Show Taps..."
    Set-DeviceSetting $serial "system" "show_touches" 1
}
if ($AutoEnablePointerLocation) {
    Write-Host "Enabling Pointer Location..."
    Set-DeviceSetting $serial "system" "pointer_location" 1
}

# 5. Clear Logcat (if configured)
if ($ClearLogcatOnStart) {
    Write-Host "Clearing logcat buffer..."
    & $ADB_PATH -s $serial logcat -c
}

# 6. Start Logcat Capture (Background)
$logPath = Join-Path $reportDir "logcat.txt"
Write-Host "Starting Logcat capture to logcat.txt..."
$logcatProc = Start-Process -FilePath $ADB_PATH -ArgumentList "-s $serial logcat -v time" -RedirectStandardOutput $logPath -NoNewWindow -PassThru
$session.LogcatPid = $logcatProc.Id

# 7. Start Screen Recording (On Device)
Write-Host "Starting Screen Recording (3min limit)..."
& $ADB_PATH -s $serial shell "screenrecord --time-limit 180 $DEVICE_VIDEO_PATH &" # Run in background on device

Save-ActiveSession $session

Write-Host "`nCapture is ACTIVE!" -ForegroundColor Green
Write-Host "1. Reproduce the issue now."
Write-Host "2. Use take_screenshot.ps1 as needed."
Write-Host "3. Run stop_debug_capture.ps1 when finished."
Write-Host "---"
