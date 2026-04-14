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
    $devices = & $ADB_PATH devices | Select-String -Pattern "\tdevice$"
    if ($devices.Count -eq 0) {
        Write-Warning "No devices connected via ADB."
        return $null
    }
    if ($devices.Count -gt 1) {
        Write-Host "Multiple devices found. Using the first one." -ForegroundColor Yellow
    }
    return ($devices[0].ToString().Split("`t")[0]).Trim()
}

function Get-DeviceSetting($serial, $namespace, $key) {
    $val = & $ADB_PATH -s $serial shell settings get $namespace $key
    return $val.Trim()
}

function Set-DeviceSetting($serial, $namespace, $key, $value) {
    & $ADB_PATH -s $serial shell settings put $namespace $key $value
}
