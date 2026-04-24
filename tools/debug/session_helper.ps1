# Shared functions for the Vedika Debug Toolkit

function Get-ActiveSession {
    $sessionFile = Join-Path $PSScriptRoot ".active_session.json"
    if (Test-Path $sessionFile) {
        $json = Get-Content $sessionFile | ConvertFrom-Json
        return $json
    }
    return $null
}

function Save-ActiveSession($session) {
    if ($null -eq $session) {
        $sessionFile = Join-Path $PSScriptRoot ".active_session.json"
        if (Test-Path $sessionFile) { Remove-Item $sessionFile }
    } else {
        $session | ConvertTo-Json | Out-File (Join-Path $PSScriptRoot ".active_session.json") -Encoding utf8
    }
}

function Invoke-Adb($Arguments) {
    # . $PSScriptRoot\debug_config.ps1 # Ensure config is loaded
    $proc = Start-Process -FilePath $ADB_PATH -ArgumentList $Arguments -NoNewWindow -PassThru -Wait -ErrorAction SilentlyContinue
    if ($null -eq $proc) {
        Write-Error "ADB not found or failed to execute. Check ADB_PATH in debug_config.ps1."
        return $null
    }
    return $proc
}

function Get-ConnectedDevice {
    $devices = @(& $ADB_PATH devices | Select-String -Pattern "\tdevice$" | ForEach-Object { $_.ToString().Split("`t")[0].Trim() })
    
    if ($null -eq $devices -or $devices.Count -eq 0) {
        Write-Warning "No devices connected via ADB."
        return $null
    }

    if ($devices.Count -eq 1) {
        return $devices[0]
    }

    Write-Host "`nMultiple devices detected:" -ForegroundColor Yellow
    for ($i = 0; $i -lt $devices.Count; $i++) {
        Write-Host " [$($i + 1)] $($devices[$i])"
    }

    $choice = Read-Host "`nSelect a device serial [1-$($devices.Count)] (or press Enter for first)"
    if ([string]::IsNullOrWhiteSpace($choice)) {
        return $devices[0]
    }

    if ($choice -as [int] -and [int]$choice -le $devices.Count) {
        return $devices[[int]$choice - 1]
    }

    Write-Error "Invalid selection. Stopping."
    return $null
}

function Get-DeviceSetting($serial, $namespace, $key) {
    $val = & $ADB_PATH -s $serial shell settings get $namespace $key
    return $val.Trim()
}

function Set-DeviceSetting($serial, $namespace, $key, $value) {
    & $ADB_PATH -s $serial shell settings put $namespace $key $value
}
