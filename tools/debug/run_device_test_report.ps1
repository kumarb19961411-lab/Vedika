# Vedika Run Device Test Report — No Screen Recording
# Automates diagnostic collection and app verification for physical-device QA.

$ErrorActionPreference = "Stop"
. "$PSScriptRoot\debug_config.ps1"
. "$PSScriptRoot\session_helper.ps1"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Vedika Physical-Device QA Report" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# 1. Device Verification
$serial = Get-ConnectedDevice
if ($null -eq $serial) {
    Write-Host "Error: No physical device detected. Connect via USB or Wi-Fi first." -ForegroundColor Red
    exit 1
}
Write-Host "Selected Device: $serial" -ForegroundColor Green

# 2. Session Initialization
$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$reportDir = Join-Path (Resolve-Path "$PSScriptRoot\..\..\$REPORT_ROOT") "device_test_$timestamp"
if (!(Test-Path $reportDir)) { New-Item -ItemType Directory -Path $reportDir | Out-Null }

$smokeResults = @{
    Timestamp = (Get-Date).ToString("o")
    DeviceSerial = $serial
    Package = $PACKAGE_NAME
    LaunchStatus = "FAILED"
    DiagnosticCollection = "INCOMPLETE"
    FinalStatus = "FAIL"
}

Write-Host "Report Directory: $reportDir"

# 3. Metadata Collection
Write-Host "Collecting device metadata..."
$deviceInfo = & $ADB_PATH -s $serial shell getprop
$deviceInfo | Out-File (Join-Path $reportDir "device_info.txt") -Encoding utf8

$model = (& $ADB_PATH -s $serial shell getprop ro.product.model).Trim()
$version = (& $ADB_PATH -s $serial shell getprop ro.build.version.release).Trim()

# 4. App Audit
Write-Host "Auditing app package: $PACKAGE_NAME"
$packages = & $ADB_PATH -s $serial shell pm list packages $PACKAGE_NAME
$packages | Out-File (Join-Path $reportDir "installed_packages.txt") -Encoding utf8

$isInstalled = $packages -match $PACKAGE_NAME
if (!$isInstalled) {
    Write-Host "Warning: App $PACKAGE_NAME is not installed on device." -ForegroundColor Yellow
    $apkPath = Read-Host "Enter path to APK to install (or press Enter to skip)"
    if (![string]::IsNullOrWhiteSpace($apkPath) -and (Test-Path $apkPath)) {
        Write-Host "Installing APK: $apkPath..."
        & $ADB_PATH -s $serial install -r $apkPath
        $isInstalled = $true
    }
}

# 5. Clear Logcat
if ($ClearLogcatOnStart) {
    Write-Host "Clearing logcat buffer..."
    & $ADB_PATH -s $serial logcat -c
}

# 6. App Launch handling (Amendment 2)
Write-Host "Launching app..."
$launchLog = Join-Path $reportDir "app_launch_log.txt"
$launchResult = ""

# Try am start
$launchTarget = "$PACKAGE_NAME/$LAUNCH_ACTIVITY"
if ($LAUNCH_ACTIVITY.StartsWith(".")) {
    $launchTarget = "$PACKAGE_NAME/$PACKAGE_NAME$LAUNCH_ACTIVITY"
}

Write-Host "Attempt 1: am start ($launchTarget)..."
$amOutput = & $ADB_PATH -s $serial shell am start -n $launchTarget 2>&1
$amOutput | Out-File $launchLog -Append

if ($amOutput -match "Error" -or $amOutput -match "does not exist") {
    Write-Host "Attempt 1 failed. Attempting Fallback: Monkey..." -ForegroundColor Yellow
    $monkeyOutput = & $ADB_PATH -s $serial shell monkey -p $PACKAGE_NAME 1 2>&1
    $monkeyOutput | Out-File $launchLog -Append
    
    if ($monkeyOutput -match "Events injected: 1") {
        $launchResult = "SUCCESS (via Monkey)"
    } else {
        $launchResult = "FAILED"
    }
} else {
    $launchResult = "SUCCESS (via am start)"
}
Write-Host "Launch Result: $launchResult"

# 7. Connected Tests (Optional)
$runTests = Read-Host "Run connected Android tests? (y/n)"
if ($runTests -eq 'y') {
    Write-Host "Running ./gradlew connectedCheck (this may take minutes)..."
    $testOutput = Join-Path $reportDir "connected_android_test_output.txt"
    # Note: Assuming project root is 2 levels up
    Push-Location (Resolve-Path "$PSScriptRoot\..\..")
    ./gradlew connectedCheck | Out-File $testOutput -Encoding utf8
    Pop-Location
}

# 8. Diagnostic Collection
Write-Host "Collecting diagnostics..."
& $ADB_PATH -s $serial logcat -d -v time | Out-File (Join-Path $reportDir "logcat_full.txt") -Encoding utf8
& $ADB_PATH -s $serial logcat -d -v time *:E | Out-File (Join-Path $reportDir "logcat_errors.txt") -Encoding utf8

$dumpsysTasks = @("activity", "package", "window", "meminfo")
foreach ($task in $dumpsysTasks) {
    $arg = if ($task -eq "window") { "" } else { $PACKAGE_NAME }
    & $ADB_PATH -s $serial shell dumpsys $task $arg | Out-File (Join-Path $reportDir "dumpsys_$task.txt") -Encoding utf8
}

# 9. Generate Markdown Report
Write-Host "Generating final report..."
$reportFile = Join-Path $reportDir "device_test_report.md"
$reportMd = @"
# 🛡️ Physical-Device QA Report

- **Timestamp**: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
- **Device**: $model ($serial)
- **Android Version**: $version
- **Package**: $PACKAGE_NAME

## 🚀 Launch Results
- **Result**: $launchResult
- **Launch Log**: [app_launch_log.txt](./app_launch_log.txt)

## 📊 Diagnostics Summary
- **Full Logcat**: [logcat_full.txt](./logcat_full.txt)
- **Errors/Fatals**: [logcat_errors.txt](./logcat_errors.txt)
- **Activity State**: [dumpsys_activity.txt](./dumpsys_activity.txt)
- **Memory Info**: [dumpsys_meminfo.txt](./dumpsys_meminfo.txt)

## 🔍 Log Observations (Top Errors)
$(Get-Content (Join-Path $reportDir "logcat_errors.txt") | Select-Object -First 10 | ForEach-Object { "- $_" } | Out-String)

## 🧪 Manual Physical-Device Checklist
- [ ] Firebase App Check enforcement verified.
- [ ] Storage image loading verified on real sensors.
- [ ] Deep link restoration (`vedika://app/vendor/123`) verified.
- [ ] Push notification token registration verified.

---
**Final Status**: $(if ($launchResult -match "SUCCESS") { "PASS" } else { "FAIL" })
"@

$reportMd | Out-File $reportFile -Encoding utf8

# 10. Update Summary
$smokeResults.LaunchStatus = $launchResult
$smokeResults.DiagnosticCollection = "SUCCESS"
$smokeResults.FinalStatus = if ($launchResult -match "SUCCESS") { "PASS" } else { "FAIL" }
$smokeResults | ConvertTo-Json | Out-File (Join-Path $reportDir "smoke_test_results.json") -Encoding utf8

Write-Host "`nReport Generated: $reportFile" -ForegroundColor Green
Write-Host "---"
