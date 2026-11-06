@echo off
title Speaking Partner PRO MAX - 1-Click APK Builder
echo ========================================================
echo   AI Speaking Partner PRO MAX - APK Builder for Windows
echo ========================================================
echo.

if exist "debug.keystore.base64" (
    if not exist "debug.keystore" (
        echo Decoding debug.keystore...
        certutil -decode debug.keystore.base64 debug.keystore >nul
    )
)

echo Checking for Gradle / Android SDK...
where gradle >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Building APK with Gradle...
    call gradle assembleDebug
) else (
    echo Gradle CLI not found in PATH.
    echo Tip: Open this folder in Android Studio and click:
    echo Build -^> Build Bundle(s) / APK(s) -^> Build APK(s)
    pause
    exit /b 1
)

if exist "app\build\outputs\apk\debug\app-debug.apk" (
    copy /Y "app\build\outputs\apk\debug\app-debug.apk" "Speaking_Partner_PRO_MAX.apk" >nul
    echo.
    echo ========================================================
    echo SUCCESS! APK File Created Right Here:
    echo %CD%\Speaking_Partner_PRO_MAX.apk
    echo ========================================================
    explorer /select,"%CD%\Speaking_Partner_PRO_MAX.apk"
) else (
    echo Build did not produce app-debug.apk. Please check error messages above.
)
pause
