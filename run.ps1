$ErrorActionPreference = 'Stop'
Set-Location -LiteralPath $PSScriptRoot
if (-not (Get-Command java -ErrorAction SilentlyContinue)) { throw 'Can cai Java/JDK 17 tro len.' }
if (-not (Test-Path -LiteralPath 'QuanLyTinChi.jar')) { & (Join-Path $PSScriptRoot 'build.ps1') }
New-Item -ItemType Directory -Force -Path 'logs' | Out-Null
$logFile = Join-Path $PSScriptRoot ('logs\app-' + (Get-Date -Format 'yyyyMMdd-HHmmss') + '.log')
& java '-Dfile.encoding=UTF-8' "-Dqltc.home=$PSScriptRoot" -jar 'QuanLyTinChi.jar' 2>&1 | Tee-Object -FilePath $logFile
if ($LASTEXITCODE -ne 0) { throw "Ung dung dung do loi. Xem $logFile" }
