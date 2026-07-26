<div align="center">

  <h1>🏋️ FitTracker</h1>
  
  <p><strong>Fitness & Workout Tracking App</strong> • Kotlin • Jetpack Compose</p>
  
  [![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue?logo=kotlin)](https://kotlinlang.org)
  [![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.8-blue?logo=jetpack)](https://developer.android.com/jetpack/compose)
  [![Android API](https://img.shields.io/badge/Android%20API-24%2B-green)](https://developer.android.com)
  [![Material3](https://img.shields.io/badge/Material%203-1.1.1-blue)](https://m3.material.io)
  [![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

  <p>A comprehensive fitness tracking app built with <strong>Jetpack Compose</strong> featuring workout timer, progress tracking, and health metrics integration.</p>

</div>

## ✨ Features

- **⏱️ Workout Timer** - Real-time workout session tracking with pause/resume
- **📊 Progress Monitoring** - Track fitness goals and improvements over time
- **🏋️ Exercise Database** - Pre-built exercise library with instructions
- **💪 Work History** - Complete log of completed workouts
- **🎨 Material3 Design** - Modern, accessible UI with Dark Mode
- **💾 Local Storage** - Room database for workout persistence
- **⚡ Instant Updates** - State-driven UI with Jetpack Compose
- **🔗 Health Integration** - Connect with health data repositories

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Kotlin 1.9.0 |
| **UI Framework** | Jetpack Compose (No XML) |
| **Architecture** | MVVM + Repository Pattern |
| **Dependency Injection** | Hilt |
| **Database** | Room (Local SQLite) |
| **Coroutines** | Kotlinx Coroutines |
| **Health Data** | HealthRepository Integration |
| **Min SDK** | 24 (Android 7.0+) |
| **Target SDK** | 35 |

## 📱 Screens

- **🏠 Home** - Dashboard with daily goals & quick actions
- **⏱️ Active Workout** - Live workout timer with exercises
- **📋 Exercise Library** - Browse and add exercises
- **📊 Progress** - Charts and statistics
- **📜 History** - Completed workout log
- **⚙️ Settings** - Preferences and goals

## 🚀 Architecture

```
┌─────────────────────────────────────┐
│          UI Layer (Compose)          │
│  - Screens, ViewModels, Components  │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│      Repository Layer (Data)        │
│  - FitTrackerRepository             │
│  - HealthRepository                 │
│  - Room Database (Local Storage)     │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│       Domain Layer (Business)       │
│  - WorkoutTimer, Entities, Utils     │
└─────────────────────────────────────┘
```

## 🔑 Key Components

- **WorkoutTimer** - Custom workout session timer with coroutines
- **HealthRepository** - Integration with health data APIs
- **Extensions** - Kotlin extensions for UI utilities
- **Constants** - App-wide configuration and workout defaults

## 📦 Installation

```bash
git clone https://github.com/kyva1125/android-fittracker.git
cd android-fittracker
./gradlew assembleDebug
```

## 🔑 Environment Variables

No external API keys required - fully offline capable.

## 📸 Screenshots

> **Coming Soon** - Screenshots demonstrating workout timer and progress tracking

## 🧪 Testing

```bash
./gradlew test
./gradlew connectedAndroidTest
```

## 📄 License

MIT License - see [LICENSE](LICENSE) for details

## 👤 Author

**Nick Ledesma** - [GitHub](https://github.com/kyva1125)

---

<div align="center">

**Built with ❤️ using Kotlin & Jetpack Compose**

</div>