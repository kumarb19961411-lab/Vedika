# Vedika Stop Debug Capture
# Finalizes the debugging session, pulls data from device, and cleans up.

$ErrorActionPreference = "Continue" # Don't stop if one step fails (e.g. logcat already died)
. "$PSScriptRoot\debug_config.ps1"
. "$PSScriptRoot\session_helper.ps1"

Write-Host "--- Vedika Debug Capture: Stopping ---" -ForegroundColor Cyan

$session = Get-ActiveSession
if ($null -eq $session) {
    Write-Host "Error: No active session found. Did you run start_debug_capture.ps1?" -ForegroundColor Red
    exit 1
}

$serial = $session.DeviceSerial
$reportDir = $session.ReportPath

# 1. Stop Local Logcat
if ($null -ne $session.LogcatPid) {
    Write-Host "Stopping Logcat process ($($session.LogcatPid))..."
    try {
        Stop-Process -Id $session.LogcatPid -Force -ErrorAction SilentlyContinue
    } catch {}
}

# 2. Stop Screen Recording on Device
Write-Host "Stopping Screen Recording on device..."
# Send SIGINT (2) to screenrecord to ensure it finalizes the MP4 file
& $ADB_PATH -s $serial shell "pkill -2 screenrecord"
Start-Sleep -Seconds 2 # Wait for file to finalize

# 3. Pull Video
Write-Host "Pulling video to session folder..."
$videoDest = Join-Path $reportDir "session_video.mp4"
& $ADB_PATH -s $serial pull $DEVICE_VIDEO_PATH $videoDest
& $ADB_PATH -s $serial shell rm $DEVICE_VIDEO_PATH

# 4. Capture Device Info
Write-Host "Capturing device info..."
$infoPath = Join-Path $reportDir "device_info.txt"
$info = @()
$info += "Model: " + (& $ADB_PATH -s $serial shell getprop ro.product.model)
$info += "Android: " + (& $ADB_PATH -s $serial shell getprop ro.build.version.release)
$info += "SDK: " + (& $ADB_PATH -s $serial shell getprop ro.build.version.sdk)
$info += "Serial: $serial"
$span = (Get-Date) - [DateTime]$session.StartTime
$info += "Session Duration: $($span.Minutes)m $($span.Seconds)s"
$info | Out-File $infoPath

# 5. Restore Developer Settings
if ($RestoreDeveloperVisualSettingsOnStop) {
    Write-Host "Restoring Developer Visuals..."
    Set-DeviceSetting $serial "system" "show_touches" $session.OriginalShowTaps
    Set-DeviceSetting $serial "system" "pointer_location" $session.OriginalPointerLocation
}

# 6. Final Status
Write-Host "`nCapture COMPLETE!" -ForegroundColor Green
Write-Host "Report saved to: $reportDir"

# Cleanup session state
Save-ActiveSession $null

Write-Host "Use open_latest_report.ps1 to view the results."
Write-Host "---"
