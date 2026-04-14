# Vedika Open Latest Report
# Finds the most recent report folder and opens it in Windows Explorer.

. "$PSScriptRoot\debug_config.ps1"

$reportRoot = Resolve-Path "$PSScriptRoot\..\..\$REPORT_ROOT"
$latest = Get-ChildItem -Path $reportRoot -Directory | Sort-Object LastWriteTime -Descending | Select-Object -First 1

if ($null -eq $latest) {
    Write-Host "No reports found in $reportRoot" -ForegroundColor Gray
} else {
    Write-Host "Opening $($latest.FullName)..."
    ii $latest.FullName
}
