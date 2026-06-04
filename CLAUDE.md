# Project: GitHub Repo Browser (native Android)

A 3-screen Android app: search a GitHub username → list their public repos →
tap a repo for detail. Built to demonstrate foundational native Kotlin.

## Stack
Kotlin, Jetpack Compose, ViewModel + StateFlow, Coroutines, Retrofit 2 + Moshi,
Navigation Compose. MVVM. No auth, no API key — public GitHub REST API.

## Architecture
Compose UI -> ViewModel (StateFlow<UiState>) -> UserRepository (interface)
-> Retrofit GitHubApi -> api.github.com

## Rules
- UiState is a sealed interface: Loading / Success(data) / Error(message).
- UserRepository MUST be an interface so the ViewModel is testable with a fake.
- Keep it simple. No pagination, no database, no DI framework on this pass.
- Conventional, readable Kotlin. Small commits.

## API
GET https://api.github.com/users/{username}/repos
GET https://api.github.com/repos/{owner}/{repo}

## Build / test
- Open in Android Studio (latest stable), let Gradle sync, Run on an emulator.
- Unit tests: `./gradlew test` (or `gradlew.bat test` on Windows).
- compileSdk/targetSdk 35, minSdk 24. JDK 11 bytecode target.
