# Vedika Take Screenshot
# Captures a screenshot from the device and saves it to the active session folder.

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
$ssDir = Join-Path $reportDir "screenshots"
if (!(Test-Path $ssDir)) { New-Item -ItemType Directory -Path $ssDir | Out-Null }

$screenName = Read-Host "Enter screen name (or leave empty for default 'screen')"
if ([string]::IsNullOrWhiteSpace($screenName)) {
    $screenName = "screen"
}
$screenName = $screenName -replace ' ', '_'

$count = @(Get-ChildItem $ssDir -Filter "${screenName}_*.png").Count + 1
$name = "${screenName}_$($count.ToString('D3')).png"
$devicePath = "/data/local/tmp/$name"
$destPath = Join-Path $ssDir $name

Write-Host "Capturing screenshot $name..." -NoNewline
& $ADB_PATH -s $serial shell screencap -p $devicePath
& $ADB_PATH -s $serial pull $devicePath $destPath | Out-Null
& $ADB_PATH -s $serial shell rm $devicePath
Write-Host " Saved." -ForegroundColor Green
