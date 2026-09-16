param([switch]$Integration,[string]$DatabaseConfig)
$ErrorActionPreference='Stop'
Set-Location -LiteralPath $PSScriptRoot
& (Join-Path $PSScriptRoot 'build.ps1')
New-Item -ItemType Directory -Force -Path 'build\test-classes' | Out-Null
& javac -proc:none --release 17 -encoding UTF-8 -cp 'build/classes;lib/*' -d 'build/test-classes' 'tests/CoreTests.java' 'tests/ConfigurationTests.java' 'tests/IntegrationTests.java' 'tests/VisualSmoke.java'
if($LASTEXITCODE -ne 0){throw 'Khong bien dich duoc kiem thu.'}
& java '-Djava.awt.headless=true' -cp 'build/classes;build/test-classes;lib/*' CoreTests
if($LASTEXITCODE -ne 0){throw 'Kiem thu logic that bai.'}
& java '-Djava.awt.headless=true' -cp 'build/classes;build/test-classes;lib/*' ConfigurationTests
if($LASTEXITCODE -ne 0){throw 'Kiem thu cau hinh that bai.'}
if($Integration){
    if(-not $DatabaseConfig){throw 'Truyen -DatabaseConfig voi cau hinh CSDL kiem thu rieng.'}
    $resolvedConfig=(Resolve-Path -LiteralPath $DatabaseConfig).Path
    & java "-Dqltc.config=$resolvedConfig" -cp 'build/classes;build/test-classes;lib/*' IntegrationTests
    if($LASTEXITCODE -ne 0){throw 'Kiem thu tich hop that bai.'}
}
