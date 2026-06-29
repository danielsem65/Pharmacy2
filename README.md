<div align="center">
  <img src="app/src/main/res/mipmap-xhdpi/ic_launcher.png" alt="Pharmacy Inventory" width="96" height="96">
  <h1>Pharmacy Inventory</h1>
  <p>Real-time medicine pricing &amp; inventory at your fingertips</p>

  <p>
    <a href="https://github.com/danielsem65/Pharmacy2/actions/workflows/build.yml">
      <img src="https://github.com/danielsem65/Pharmacy2/actions/workflows/build.yml/badge.svg" alt="Build">
    </a>
    <a href="https://github.com/danielsem65/Pharmacy2/releases">
      <img src="https://img.shields.io/badge/version-1.0-blue" alt="Version">
    </a>
    <a href="https://github.com/danielsem65/Pharmacy2/blob/main/LICENSE">
      <img src="https://img.shields.io/badge/license-MIT-green" alt="License">
    </a>
    <img src="https://img.shields.io/badge/API-21%2B-brightgreen" alt="API 21+">
    <img src="https://img.shields.io/badge/platform-Android-lightgrey" alt="Platform">
  </p>
</div>

---

## Overview

**Pharmacy Inventory** bridges the gap between customers and medication pricing. Browse the complete medicine catalog at **SCAB Pharmacy**, search for specific drugs by name, check current prices, and access the pharmacy website &mdash; all in one place.

The app intelligently caches data locally so you can browse medicine prices anytime, even without an internet connection.

---

## Features

| | Feature | Description |
|---|---------|-------------|
| 🔍 | **Smart Search** | Real-time filtering as you type &mdash; find any medicine instantly |
| 📡 | **Offline Mode** | Data is cached locally on first load; browse without internet |
| 🔄 | **Live Updates** | Pull fresh pricing data with a single tap from the menu |
| 🌐 | **In-App WebView** | Browse the SCAB Pharmacy website without leaving the app |
| ⚡ | **Fast Startup** | Splash screen loads in 3 seconds, cached data shows immediately |
| 🎨 | **Clean UI** | Material-inspired design with intuitive navigation |

---

## Tech Stack

| Component | Technology |
|-----------|-----------|
| **Language** | Java 8+ |
| **Minimum SDK** | API 21 (Android 5.0 Lollipop) |
| **Target SDK** | API 34 (Android 14) |
| **Architecture** | Single-Activity with ViewBinding |
| **Networking** | OkHttp 4.12 |
| **Serialization** | Gson 2.11 |
| **Build System** | Gradle 8.7 + Android Gradle Plugin 8.7 |
| **CI/CD** | GitHub Actions |
| **Caching** | Internal file storage (JSON) |

---

## Quick Start

### Download the APK

Pre-built debug APKs are generated automatically with every push. Grab the latest from GitHub Actions:

1. Go to [Actions](https://github.com/danielsem65/Pharmacy2/actions)
2. Click the latest successful workflow run
3. Scroll to **Artifacts** and download `PharmacyInventory-Debug.apk`
4. Install on your device (enable **Install from unknown sources**)

### Build from Source

```bash
# Clone the repository
git clone https://github.com/danielsem65/Pharmacy2.git
cd Pharmacy2

# Build debug APK
./gradlew assembleDebug

# Output:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## Project Structure

```
Pharmacy2/
├── .github/workflows/
│   └── build.yml                # CI: automated APK builds on push
├── app/
│   ├── src/main/
│   │   ├── java/com/semdev/pharma/inv/
│   │   │   ├── DashboardActivity.java       # Main UI, search, list, WebView
│   │   │   ├── MainActivity.java            # Splash screen (3s delay)
│   │   │   ├── RequestNetwork.java          # Network request wrapper
│   │   │   ├── RequestNetworkController.java # OkHttp client (SSL, headers)
│   │   │   ├── FileUtil.java                # File I/O utilities
│   │   │   └── SketchwareUtil.java          # General-purpose helpers
│   │   ├── res/
│   │   │   ├── layout/                      # XML layouts (main, dashboard, items)
│   │   │   ├── values/                      # Colors, strings, styles
│   │   │   ├── drawable-xhdpi/              # Vector icons & assets
│   │   │   └── mipmap-*/                    # Launcher icons (hdpi–xxxhdpi)
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
├── gradle.properties
└── README.md
```

---

## Architecture

```
┌──────────────────┐     ┌─────────────────────────────────────┐
│                  │     │                                     │
│   MainActivity   │────>│          DashboardActivity           │
│   (Splash)       │     │                                     │
│                  │     │  ┌──────────────┐  ┌──────────────┐ │
└──────────────────┘     │  │   ListView    │  │   WebView    │ │
                         │  │  (Medicines)  │  │  (Website)   │ │
                         │  └──────┬───────┘  └──────┬───────┘ │
                         │         │                 │         │
                         │  ┌──────┴───────┐         │         │
                         │  │  Search Bar  │         │         │
                         │  └──────────────┘         │         │
                         │         │                 │         │
                         │         └─── toggle ──────┘         │
                         └─────────────────────────────────────┘
```

### Data Flow

```
 ┌─────────────┐     ┌──────────────┐     ┌──────────────────┐
 │ App Launch  │────>│ Load Cache   │────>│ Network Request  │
 └─────────────┘     │ (if exists)  │     └────────┬─────────┘
                     └──────────────┘              │
                                          ┌────────┴────────┐
                                          ▼                 ▼
                                   ┌────────────┐   ┌──────────────┐
                                   │  Success   │   │   Failure    │
                                   └──────┬─────┘   └──────┬───────┘
                                          ▼                 ▼
                                   ┌────────────┐   ┌──────────────┐
                                   │ Save Cache │   │ Load Cache   │
                                   │ Update UI  │   │ (fallback)   │
                                   └────────────┘   └──────────────┘
```

<details>
<summary><b>Detailed Flow</b></summary>

1. App launches &rarr; immediately loads cached data from internal storage (if available)
2. Fires a **GET** request to the JSON API endpoint
3. **On success:** parses the response, updates the ListView, and persists the raw JSON to `pharma_cache.json`
4. **On failure (no internet):** falls back to the cached JSON and displays a **"Loaded from offline cache"** toast
5. User can search by name (TextWatcher filters in real-time)
6. Back button toggles between medicine list and WebView

</details>

---

## Contributing

Contributions are welcome! Here's how you can help:

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
