# Vedika Generate Bugreport
# Triggers a full adb bugreport into the active session folder.

$ErrorActionPreference = "Stop"
. "$PSScriptRoot\debug_config.ps1"
. "$PSScriptRoot\session_helper.ps1"

$session = Get-ActiveSession
if ($null -eq $session) {
    Write-Host "No active session. Run start_debug_capture.ps1 first." -ForegroundColor Gray
    exit 0
}

$serial = $session.DeviceSerial
$reportDir = $session.ReportPath
$dest = Join-Path $reportDir "bugreport.zip"

Write-Host "Generating bugreport... This may take up to 2 minutes. Please wait." -ForegroundColor Yellow
& $ADB_PATH -s $serial bugreport $dest

Write-Host "`nBugreport saved to: $dest" -ForegroundColor Green
