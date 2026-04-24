# Vedika Debug Capture Toolkit - Interactive Wrapper
# One script to rule them all.

. "$PSScriptRoot\debug_config.ps1"
. "$PSScriptRoot\session_helper.ps1"

function Show-Menu {
    $session = Get-ActiveSession
    $status = if ($null -ne $session) { "CAPTURE ACTIVE ($($session.Timestamp))" } else { "IDLE" }
    $color = if ($null -ne $session) { "Green" } else { "Gray" }

    Clear-Host
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "   Vedika Debug Capture Toolkit" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host " Status: $status" -ForegroundColor $color
    Write-Host "----------------------------------------"
    Write-Host " 1. Start Capture (Logcat + Video)"
    Write-Host " 2. Stop Capture (Pull results)"
    Write-Host " 3. Take Screenshot"
    Write-Host " 4. Generate Full Bugreport"
    Write-Host " 5. Open Latest Report Folder"
    Write-Host " 6. Run Device Test Report — No Screen Recording"
    Write-Host " 7. Exit"
    Write-Host "----------------------------------------"
}

do {
    Show-Menu
    $choice = $null
    Write-Host "`nSelect an option [1-7] (or wait if capturing) " -NoNewline

    while ($true) {
        $session = Get-ActiveSession
        if ($null -ne $session) {
            $startTime = $session.StartTime -as [DateTime]
            if ($null -eq $startTime) { $startTime = Get-Date }
            $duration = (Get-Date) - $startTime
            if ($duration.TotalSeconds -ge 180) {
                Write-Host "`n`n[!] 3-minute video limit reached. Auto-stopping capture..." -ForegroundColor Yellow
                & "$PSScriptRoot\stop_debug_capture.ps1"
                Write-Host ""
                $ans = Read-Host "Capture files exported. Start a new capture? (y/n)"
                if ($ans -notmatch '^[nN]') {
                    & "$PSScriptRoot\start_debug_capture.ps1"
                }
                break
            }
        }

        if ([Console]::KeyAvailable) {
            $key = [Console]::ReadKey($true)
            $choice = [string]$key.KeyChar
            Write-Host $choice
            break
        }
        Start-Sleep -Milliseconds 500
    }

    if ($null -eq $choice) { continue }

    switch ($choice) {
        "1" { & "$PSScriptRoot\start_debug_capture.ps1"; Pause }
        "2" { & "$PSScriptRoot\stop_debug_capture.ps1"; Pause }
        "3" { & "$PSScriptRoot\take_screenshot.ps1"; Start-Sleep -Seconds 1 }
        "4" { & "$PSScriptRoot\generate_bugreport.ps1"; Pause }
        "5" { & "$PSScriptRoot\open_latest_report.ps1"; Start-Sleep -Seconds 1 }
        "6" { & "$PSScriptRoot\run_device_test_report.ps1"; Pause }
        "7" { Write-Host "Goodbye!" -ForegroundColor Cyan; break }
        default { Write-Host "Invalid choice." -ForegroundColor Red; Start-Sleep -Seconds 1 }
    }
} while ($choice -ne "7")
