# GitHub Repo Browser
 
A small native Android app for browsing any GitHub user's public repositories.
Search a username, see their repos, tap one for detail. Built to practise modern
native Android development.
 
<!-- Add 1–2 emulator screenshots here once the app runs:
![Search screen](docs/search.png)
![Repo list](docs/list.png)
-->
 
## Features
 
- Search any GitHub username
- List their public repositories with star count, language, and description
- Tap a repo for detail: forks, open issues, primary language, last updated
- Loading and error states (handles unknown users and network failures gracefully)
## Built with
 
- **Kotlin** + **Jetpack Compose** for the UI
- **ViewModel** + **StateFlow** for state (MVVM)
- **Kotlin Coroutines** for asynchronous work
- **Retrofit 2** + **Moshi** for networking and JSON parsing
- **Navigation Compose** for screen navigation
- **JUnit** + **kotlinx-coroutines-test** for unit testing
No API key or login required — it uses the public GitHub REST API.
 
## Architecture
 
The app follows a simple MVVM structure. Compose screens observe a `ViewModel`
that exposes a `StateFlow<UiState>` (`Loading` / `Success` / `Error`). The
ViewModel talks to a `UserRepository` interface, which is implemented by a
Retrofit-backed data source calling `api.github.com`. Because the repository is an
interface, the ViewModel can be unit-tested with a fake that needs no network.
 
```
Compose UI → ViewModel (StateFlow) → UserRepository → Retrofit → GitHub API
```
 
## API
 
```
GET https://api.github.com/users/{username}/repos
GET https://api.github.com/repos/{owner}/{repo}
```
 
Unauthenticated requests are rate-limited to about 60 per hour, which is fine for
local use.
 
## Running it
 
1. Clone the repo:
   ```bash
   git clone https://github.com/<your-username>/github-repo-browser.git
   ```
2. Open the project in **Android Studio** (latest stable).
3. Let Gradle sync, then Run ▶ on an emulator or a connected device (min SDK 24).
## Testing
 
```bash
./gradlew test
```
 
The unit test covers the list ViewModel: given a repository that returns a known
list, state ends in `Success` with that list; given one that throws, state ends in
`Error`. It uses a fake repository, so no network access is needed.
 
## What I'd add next
 
- Pagination for users with many repositories
- Local caching so previously viewed users load offline
- Pull-to-refresh and a dark theme
- A repository search field (search across GitHub, not just one user)
- Instrumented UI tests
## License
 
MIT
