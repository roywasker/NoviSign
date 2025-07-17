# NoviSign Slideshow App

A simple Android app that fetches media (images & videos) from the NoviSign API and displays them in a looping slideshow with cross‑fade transitions.

---

## Features

- Fetches playlist metadata from NoviSign  
- Loads images via Coil and videos via ExoPlayer  
- Cross‑fade transitions between items  
- Modular MVVM architecture with Hilt for DI  

---

## Architecture

```text
com.example.slideshow
├── data
│   ├── remote           # Ktor service & DTOs
│   └── repository       # Implements domain contracts
├── domain
│   ├── model            # `MediaItem` sealed class
│   └── usecase          # `FetchPlaylist` interface
├── di                   # Hilt modules
└── presentation         # Composable + ViewModel
└── work_manager         # work manager task

```

## Tech Stack

- **Kotlin** & **Jetpack Compose**  
- **Hilt** (Dagger) for dependency injection  
- **Ktor** for networking & JSON  
- **Coil** for image loading  
- **ExoPlayer** for video playback  
- **Coroutines & Flow** for asynchronous streams
---

## Setup

1. Clone the repo:  
   ```bash
   git clone https://github.com/your‑username/slideshow‑app.git
   cd slideshow-app
   ```
2. Open in Android Studio.  
3. Verify Compose and Hilt versions in `build.gradle`.  
4. Connect an Android device or start an emulator.  

---

## Running

- Hit **Run** in Android Studio (or use `Shift+F10`).  
- The app will fetch the default screen key `e490b14d-987d-414f-a822-1e7703b37ce4` and start the slideshow.

---
