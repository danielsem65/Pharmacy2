<div align="center">
  <img src="app/src/main/res/mipmap-xhdpi/ic_launcher.png" alt="Pharmacy Inventory" width="96" height="96">
  <h1>Pharmacy Inventory</h1>
  <p>Real-time medicine pricing &amp; inventory at your fingertips</p>

  <p>
    <a href="https://github.com/danielsem65/Pharmacy2/actions/workflows/build.yml">
      <img src="https://github.com/danielsem65/Pharmacy2/actions/workflows/build.yml/badge.svg" alt="Build">
    </a>
    <a href="https://github.com/danielsem65/Pharmacy2">
      <img src="https://img.shields.io/badge/version-1.0-blue" alt="Version">
    </a>
    <a href="https://github.com/danielsem65/Pharmacy2/blob/main/LICENSE">
      <img src="https://img.shields.io/badge/license-MIT-green" alt="License">
    </a>
    <img src="https://img.shields.io/badge/API-21%2B-brightgreen" alt="API 21+">
    <img src="https://img.shields.io/badge/platform-Android-lightgrey" alt="Platform">
    <img src="https://img.shields.io/badge/language-Kotlin-purple" alt="Kotlin">
    <img src="https://img.shields.io/badge/arch-MVVM-important" alt="MVVM">
  </p>
</div>

---

## Overview

**Pharmacy Inventory** is a modern Android application that connects customers with real-time medication pricing at SCAB Pharmacy. Browse the complete catalog, search for specific drugs, view prices, and access the pharmacy website &mdash; all with a polished Material 3 interface and robust offline support.

Built with **Kotlin**, **MVVM architecture**, **Room database**, and **Retrofit**, this app demonstrates production-grade Android development practices.

---

## Features

| | Feature | Description |
|---|---------|-------------|
| 🔍 | **Smart Search** | Real-time filtering with Material SearchView |
| 📡 | **Offline First** | Room database caches data locally; works fully offline after first load |
| 🔄 | **Pull to Refresh** | Swipe down to fetch the latest prices |
| 🌐 | **In-App WebView** | Browse SCAB Pharmacy website without leaving the app |
| ⚡ | **Splash Screen** | Android 12+ SplashScreen API with seamless transition |
| 🎨 | **Material 3** | Modern design with dynamic theming |
| 🗄️ | **Room Database** | Structured local persistence with DAO pattern |

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| **Language** | Kotlin 1.9 |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **Async** | Kotlin Coroutines + Flow |
| **Networking** | Retrofit 2.9 + OkHttp 4.12 |
| **Serialization** | Gson 2.11 |
| **Local Storage** | Room 2.6 (SQLite) |
| **UI** | Material 3, ViewBinding, RecyclerView |
| **CI/CD** | GitHub Actions |
| **Min SDK** | API 21 (Android 5.0) |
| **Target SDK** | API 34 (Android 14) |

---

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                       UI LAYER                          │
│                                                         │
│  ┌────────────────┐     ┌──────────────────────────┐   │
│  │ SplashActivity  │────>│    DashboardActivity      │   │
│  │ (SplashScreen)  │     │                          │   │
│  └────────────────┘     │  MaterialToolbar + Menu   │   │
│                         │  SearchView               │   │
│                         │  RecyclerView (medicines) │   │
│                         │  SwipeRefreshLayout       │   │
│                         │  WebView (pharmacy site)  │   │
│                         └──────────┬───────────────┘   │
│                                    │ observes          │
│                         ┌──────────┴───────────────┐   │
│                         │   DashboardViewModel      │   │
│                         │   (StateFlow<UiState>)    │   │
│                         └──────────┬───────────────┘   │
└────────────────────────────────────┼───────────────────┘
                                     │ calls
┌────────────────────────────────────┼───────────────────┐
│                         DATA LAYER │                   │
│                                     │                   │
│  ┌──────────────────────────────────┴──────────────┐   │
│  │            MedicineRepository                   │   │
│  │  ┌──────────────────┐    ┌──────────────────┐   │   │
│  │  │  Network First   │    │  Cache Fallback  │   │   │
│  │  └────────┬─────────┘    └────────┬─────────┘   │   │
│  └───────────┼───────────────────────┼──────────────┘   │
│              │                       │                   │
│  ┌───────────┴─────────┐  ┌─────────┴────────────┐   │
│  │  Retrofit (Remote)  │  │  Room (Local DB)     │   │
│  │  ApiService         │  │  MedicineDao         │   │
│  │  MedicineDto        │  │  MedicineEntity      │   │
│  └─────────────────────┘  └──────────────────────┘   │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### Data Flow

```
User opens app
      │
      ▼
SplashActivity (1.5s) ───► DashboardActivity
                                │
                                ▼
                    DashboardViewModel.init()
                                │
                    ┌───────────┴───────────┐
                    │                       │
                    ▼                       ▼
         Load from Room            Fetch from API
         (offline cache)           (Retrofit)
                    │                       │
                    │               ┌───────┴───────┐
                    │               │               │
                    │               ▼               ▼
                    │          Success          Failure
                    │               │               │
                    │               ▼               ▼
                    │       Save to Room      Use Room cache
                    │       Update UI         (if available)
                    │               │               │
                    └───────┬───────┘               │
                            │                       │
                            ▼                       ▼
                    UiState.Success          UiState.Offline
                    UiState.Error (no cache)
```

---

## Project Structure

```
Pharmacy2/
├── .github/workflows/
│   └── build.yml                    # CI: automated APK builds
├── app/
│   └── src/main/
│       ├── java/com/semdev/pharma/inv/
│       │   ├── PharmacyApp.kt                  # Application class
│       │   ├── data/
│       │   │   ├── local/
│       │   │   │   ├── AppDatabase.kt          # Room database singleton
│       │   │   │   ├── MedicineDao.kt          # Data access object
│       │   │   │   └── MedicineEntity.kt       # Room entity
│       │   │   ├── remote/
│       │   │   │   ├── ApiService.kt           # Retrofit API interface
│       │   │   │   ├── MedicineDto.kt          # Network DTO
│       │   │   │   └── RetrofitClient.kt       # HTTP client config
│       │   │   └── repository/
│       │   │       └── MedicineRepository.kt   # Repository (network + cache)
│       │   ├── ui/
│       │   │   ├── dashboard/
│       │   │   │   ├── DashboardActivity.kt    # Main screen
│       │   │   │   ├── DashboardViewModel.kt   # ViewModel with StateFlow
│       │   │   │   └── MedicineAdapter.kt      # RecyclerView adapter
│       │   │   └── splash/
│       │   │       └── SplashActivity.kt       # Splash screen
│       │   └── util/
│       │       └── NetworkUtils.kt             # Connectivity checker
│       ├── res/
│       │   ├── layout/
│       │   │   ├── activity_dashboard.xml      # Dashboard layout (Material 3)
│       │   │   └── item_medicine.xml           # List item card layout
│       │   ├── menu/
│       │   │   └── dashboard_menu.xml          # Overflow menu
│       │   ├── values/
│       │   │   ├── colors.xml                  # Material 3 color palette
│       │   │   ├── strings.xml
│       │   │   └── styles.xml                  # Material 3 theme
│       │   ├── drawable-xhdpi/                 # Vector icons
│       │   └── mipmap-*/                       # Launcher icons
│       └── AndroidManifest.xml
├── build.gradle
├── app/build.gradle
├── settings.gradle
├── gradle.properties
└── README.md
```

---

## Quick Start

### Download the APK

Pre-built debug APKs are generated automatically with every push.

1. Go to [Actions](https://github.com/danielsem65/Pharmacy2/actions)
2. Click the latest successful workflow run
3. Scroll to **Artifacts** and download `PharmacyInventory-Debug.apk`
4. Install on your device (enable **Install from unknown sources**)

### Build from Source

```bash
# Clone the repository
git clone https://github.com/danielsem65/Pharmacy2.git
cd Pharmacy2

# Build debug APK (requires JDK 17 and Android SDK)
./gradlew assembleDebug

# Output:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## Contributing

1. **Fork** the project
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. Open a **Pull Request**

---

## License

```
MIT License

Copyright (c) 2024 Daniel Sem

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

<div align="center">
  <sub>Built with ❤️ by <a href="https://github.com/danielsem65">Daniel Sem</a></sub>
</div>
