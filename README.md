# Pharmacy Inventory

[![Build Android APK](https://github.com/danielsem65/Pharmacy2/actions/workflows/build.yml/badge.svg)](https://github.com/danielsem65/Pharmacy2/actions/workflows/build.yml)

An Android app for browsing medicine prices and details at SCAB Pharmacy. Customers can easily check medication costs, search by name, and view the pharmacy's website — all from their phone.

## Features

- **Medicine List** — Browse all available medications with current prices
- **Search** — Filter medicines by name in real-time
- **Offline Mode** — Data is cached locally; view prices even without internet
- **WebView** — Quick access to the pharmacy website
- **Pull to Refresh** — Update medicine data with one tap

## Screenshots

| Splash | Dashboard | Search |
|--------|-----------|--------|
| <img src="app/src/main/res/mipmap-xhdpi/ic_launcher.png" width="120" alt="Splash"> | | |

*Screenshots to be added.*

## Tech Stack

- **Language:** Java
- **Build System:** Gradle (Android)
- **Min SDK:** 21 (Android 5.0)
- **Target SDK:** 34 (Android 14)
- **Networking:** OkHttp
- **JSON Parsing:** Gson
- **UI:** ViewBinding

## Download

Pre-built APKs are available from the [Actions](https://github.com/danielsem65/Pharmacy2/actions) tab:

1. Click the latest successful workflow run
2. Scroll to **Artifacts**
3. Download `PharmacyInventory-Debug.apk`

## Build Locally

```bash
git clone https://github.com/danielsem65/Pharmacy2.git
cd Pharmacy2
./gradlew assembleDebug
```

The APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

## Project Structure

```
Pharmacy2/
├── app/
│   ├── src/main/
│   │   ├── java/com/semdev/pharma/inv/
│   │   │   ├── DashboardActivity.java    # Main dashboard screen
│   │   │   ├── MainActivity.java         # Splash screen
│   │   │   ├── RequestNetwork.java       # Network request wrapper
│   │   │   ├── RequestNetworkController.java  # OkHttp client
│   │   │   ├── FileUtil.java             # File I/O utilities
│   │   │   └── SketchwareUtil.java       # Utility helpers
│   │   ├── res/layout/                   # XML layouts
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## License

Distributed under the MIT License. See `LICENSE` for more information.
