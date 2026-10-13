<<<<<<< HEAD
# BADMAN DRIVER'S — Android diagnostic project

An Android Studio / Gradle Kotlin + Jetpack Compose project for Android 15 (compile/target SDK 35). Includes the neon logo, centered animated splash screen, unlock reveal, button tones, device/kernel information, root-state display, and truthful diagnostic logs.

## Build
- Open this folder in Android Studio with JDK 17 and Android SDK Platform 35 installed.
- Allow Gradle to sync and download the Android Gradle Plugin and dependencies.
- Run `:app:assembleDebug` or use Build > Build APK(s).
- Gradle's Android packaging pipeline stores `resources.arsc` uncompressed and applies APK alignment; debug builds are automatically signed with the local debug key.
- For release signing, configure a private keystore in Android Studio's Generate Signed Bundle/APK wizard. Never commit the keystore or passwords.

## Safety / status accuracy
This initial project does not execute bundled QX/RT binaries, load kernel modules, hide processes, or bypass Android security. The QX and RT controls are inspection-only until the exact module source, licensing/authorization, device compatibility, and required privileges are verified. UI status never claims a driver is active merely because a button was tapped.

## Build environment note
This source archive is not a compiled APK. A complete APK build requires Android SDK Platform 35, JDK 17, Gradle dependency access, and signing tools.
=======
# badman-drivers
BADMAN DRIVERS Android project
>>>>>>> 0ea086736f1016ef80f5855c602112f380deff18
