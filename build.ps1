$ErrorActionPreference = 'Stop'
Set-Location -LiteralPath $PSScriptRoot
if (-not (Get-Command javac -ErrorAction SilentlyContinue)) { throw 'Can cai JDK 17 tro len (khong chi JRE), sau do mo lai cua so nay.' }
$classDirectory = Join-Path $PSScriptRoot 'build\classes'
New-Item -ItemType Directory -Force -Path $classDirectory | Out-Null
$sourceFiles = Get-ChildItem -LiteralPath (Join-Path $PSScriptRoot 'src') -Recurse -Filter '*.java'
$argumentLines = $sourceFiles | ForEach-Object { '"' + $_.FullName.Replace('\','/') + '"' }
[IO.File]::WriteAllLines((Join-Path $PSScriptRoot 'build\sources.txt'),[string[]]$argumentLines,[Text.UTF8Encoding]::new($false))
& javac -proc:none --release 17 -encoding UTF-8 -cp 'lib/*' -d $classDirectory '@build/sources.txt'
if ($LASTEXITCODE -ne 0) { throw 'Bien dich that bai. Xem loi phia tren.' }
$libraryPaths = Get-ChildItem -LiteralPath (Join-Path $PSScriptRoot 'lib') -Filter '*.jar' | ForEach-Object { 'lib/' + $_.Name }
$manifest = "Manifest-Version: 1.0`r`nMain-Class: App`r`nClass-Path: " + ($libraryPaths -join " `r`n ") + "`r`n`r`n"
[IO.File]::WriteAllText((Join-Path $PSScriptRoot 'build\MANIFEST.MF'),$manifest,[Text.UTF8Encoding]::new($false))
& jar --create --file 'QuanLyTinChi.jar' --manifest 'build/MANIFEST.MF' -C $classDirectory .
if ($LASTEXITCODE -ne 0) { throw 'Khong tao duoc tep JAR.' }
Write-Host 'Da bien dich thanh cong. Chay RUN.bat de mo ung dung.'
