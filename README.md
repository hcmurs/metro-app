<h1 align="center"><picture><source media="(prefers-color-scheme: dark)" srcset="assets/hero-dark.webp"><img src="assets/hero.webp" alt="HCMC Metro app: buy, scan, go. The HCMC Metro logo, three lines of large type reading buy, scan and go, and a tilted ticket showing a QR code with the words My ticket and QR refreshes every 60 seconds." width="100%"></picture></h1>

**Buy a ticket, show a QR code, walk through the gate. The HCMC Metro app does it from your phone, and the same app lets station staff scan tickets at the gate.**

HCMC Metro is an Android app for riding the Ho Chi Minh City metro. Passengers sign in with Google, choose a ticket, pay online and keep a QR code ready in My Ticket. Station staff sign in to the same app and land on a scanner. Around the ticket sit the small things that make a trip easier: a fare check between two stations, news, push notifications, feedback, an AI assistant, a map, and English and Vietnamese.

## From ticket to gate

<picture><source media="(prefers-color-scheme: dark)" srcset="assets/journey-dark.webp"><img src="assets/journey.webp" alt="From ticket to gate, four steps along one route line. 1, choose: pick a ticket type, or two stations for a single trip. 2, pay: card with Stripe, or PayOS for day and student tickets. 3, show: My Ticket opens a QR that refreshes every 60 seconds. 4, scan: station staff scan it on entry and on exit." width="100%"></picture>

## One app, two roles

<picture><source media="(prefers-color-scheme: dark)" srcset="assets/roles-dark.webp"><img src="assets/roles.webp" alt="Two roles in one app. Passenger: sign in with Google, buy single, day or student tickets, look up the fare between two stations, news and push notifications, feedback and the Metro AI Assistant. Station staff: sign in and land on the scanner home, pick a route and a station, scan the ticket QR for entry or exit, every scan is sent to the backend." width="100%"></picture>

## Everything in the app

### Tickets and fares

<picture><source media="(prefers-color-scheme: dark)" srcset="assets/features-tickets-dark.webp"><img src="assets/features-tickets.webp" alt="Two tiles. Buy tickets: single tickets pay with Stripe, day and student tickets also take PayOS; labels Single, Day, Student. Check the fare: pick two stations and the fare comes from the backend." width="100%"></picture>

### At the gate

<picture><source media="(prefers-color-scheme: dark)" srcset="assets/features-gate-dark.webp"><img src="assets/features-gate.webp" alt="Two tiles. Show your QR: My Ticket sorts tickets by status, and the QR refreshes every 60 seconds; labels Not used, Used, Expired. Scan at the gate: staff pick a route and a station, then scan ticket QR codes; labels ENTRY and EXIT." width="100%"></picture>

### News and alerts

<picture><source media="(prefers-color-scheme: dark)" srcset="assets/features-news-dark.webp"><img src="assets/features-news.webp" alt="Two tiles. Read the news: metro news from the backend, page by page. Get notified: push notifications, plus an in-app list you can read and delete." width="100%"></picture>

### Talk to us

<picture><source media="(prefers-color-scheme: dark)" srcset="assets/features-voice-dark.webp"><img src="assets/features-voice.webp" alt="Two tiles. Tell us: send a suggestion, report an app issue, or anything else; labels Góp ý, Sự cố ứng dụng, Khác. Ask the assistant: chat with the Metro AI Assistant once you are signed in." width="100%"></picture>

### Around the metro

<picture><source media="(prefers-color-scheme: dark)" srcset="assets/features-around-dark.webp"><img src="assets/features-around.webp" alt="Two tiles. See the line: browse the stations and the line on a map. Around the metro: links to partner services and photos of the construction; labels Go!Bus, TTGT, Công dân số, TNGo." width="100%"></picture>

### Account and language

<picture><source media="(prefers-color-scheme: dark)" srcset="assets/features-account-dark.webp"><img src="assets/features-account.webp" alt="Two tiles. Two languages: English and Vietnamese, and English shows prices in US dollars at a live rate; labels English, Tiếng Việt. Sign in with Google: sign in with your Google account, and staff accounts open the scanner home; labels Passenger, Station staff." width="100%"></picture>

## Under the hood

<picture><source media="(prefers-color-scheme: dark)" srcset="assets/tech-hero-dark.webp"><img src="assets/tech-hero.webp" alt="Under the hood. Kotlin and Jetpack Compose on the phone, REST to the metro-be backend. Chips: Kotlin, Jetpack Compose, Hilt, Retrofit, Android 8.0 and up." width="100%"></picture>

The app is a Kotlin and Jetpack Compose client of the REST backend in [metro-be](https://github.com/hcmurs/metro-be). This part shows how the pieces fit together, what the app is built with, and how to run it and ship a change.

### How it fits together

<picture><source media="(prefers-color-scheme: dark)" srcset="assets/tech-flow-dark.webp"><img src="assets/tech-flow.webp" alt="How it fits together, three tiles joined left to right. Screens: Jetpack Compose screens and their ViewModels; chips Compose, Navigation, ViewModel. Repositories: Retrofit and OkHttp call the API; chips Retrofit, OkHttp, Gson. Backend: every request goes to base.url, the REST API of metro-be; chips metro-be, Spring microservices. A bar under the first two tiles reads: Hilt wires the layers together. A row below reads: straight from the phone, Google Sign-In, Stripe, PayOS checkout, Firebase Cloud Messaging, OpenStreetMap." width="100%"></picture>

Screens call repositories, repositories call the backend through Retrofit and OkHttp, and Hilt injects each layer. Every request goes to `base.url`. Google sign-in, card payment, push messages and map tiles reach the phone from their own services, and the PayOS checkout opens in a browser.

<details>
<summary><b>API paths the app calls</b></summary>

- Sign-in: `auth/oauth2/google`, `auth/profile`
- Tickets: `ts/ticket-types`, `ts/tickets/qr`, `ts/tickets/code/{code}`, `ts/tickets/scan/entry`, `ts/tickets/scan/exit`
- Fares: `ts/fare-matrices`, `ts/fare-matrices/get-fare`
- Orders and payment: `orders/create/single`, `orders/create/days`, `orders/user`, `payment/stripe/*`, `payment/payos/*`
- Stations and routes: `stations`, `routes`, `station-routes/route/{id}`
- News, feedback, assistant, notifications: `users/blogs`, `users/feedbacks`, `users/chatbot/chat`, `notifications/*`, `user-device-tokens`

</details>

### Built with

<picture><source media="(prefers-color-scheme: dark)" srcset="assets/tech-stack-dark.webp"><img src="assets/tech-stack.webp" alt="Six tiles of what the app is built with. ui: Kotlin 2.0.21, Material 3, Jetpack Compose, Coil, Navigation Compose. wiring: Hilt, Retrofit, Gson, OkHttp. on device: CameraX, ML Kit barcode scanning, osmdroid. services: Firebase Cloud Messaging, Google Sign-In, Auth0 SDK, Stripe, PayOS. build: Gradle 8.13, JDK 17, Android Gradle Plugin 8.9.0, Spotless, GitHub Actions. android: minSdk 26, targetSdk 35, compileSdk 35." width="100%"></picture>

### Run it and ship a change

<picture><source media="(prefers-color-scheme: dark)" srcset="assets/tech-workflow-dark.webp"><img src="assets/tech-workflow.webp" alt="Two route lines. Run it, four steps. 1 clone: clone the repo and copy local.properties.sample to local.properties. 2 configure: fill in the four keys, and add your own app/google-services.json. 3 build: build a debug APK with Gradle, or press Run in Android Studio; command assembleDebug. 4 run: install on a device or emulator with Android 8.0 or later. Ship a change, four steps. 1 branch: fork the repo and branch from develop. 2 format: Kotlin is formatted by Spotless with ktlint; command spotlessApply. 3 pull request: open it against develop. 4 check: GitHub Actions runs the Spotless check on pull requests; command spotlessCheck." width="100%"></picture>

**What you need**

- Android Studio that opens an Android Gradle Plugin 8.9.0 project, and JDK 17 (the version the CI workflow uses).
- Android SDK platform 35 (the project compiles and targets 35).
- A device or emulator running Android 8.0 (API 26) or later.
- A `google-services.json` from your own Firebase project, saved as `app/google-services.json`. The file is not in the repository and git ignores it; the Google services plugin is applied, so the build expects it.
- A running backend that the app can reach, see [metro-be](https://github.com/hcmurs/metro-be).

**Clone and build**

```bash
git clone https://github.com/hcmurs/metro-app.git
cd metro-app
cp local.properties.sample local.properties
```

Fill in `local.properties` as described under Configuration, then build a debug APK:

```bash
./gradlew assembleDebug
```

On Windows use `gradlew.bat assembleDebug`. You can also open the folder in Android Studio, wait for the Gradle sync and press Run on a device or emulator.

**Configuration**

The build reads four keys from `local.properties` (the file is git-ignored). A missing file or a missing key stops the build with an error.

| Key | What it is |
|---|---|
| `sdk.dir` | Path to your Android SDK, for local builds |
| `auth0.domain` | Domain of the Auth0 tenant used by the Auth0 login |
| `auth0.client.id` | Client ID of the Auth0 application |
| `base.url` | Root URL of the backend API; end it with a slash |

`local.properties.sample` lists `sdk.dir`, `auth0.domain` and `auth0.client.id`; add `base.url` yourself. Pass the values through your own secret store in CI; never commit them. From an Android emulator the host machine is `10.0.2.2`, and cleartext HTTP is allowed only for the addresses listed in `app/src/main/res/xml/network_security_config.xml`.

**Ship a change**

1. Fork the repository and create a branch from `develop`.
2. Run `./gradlew spotlessApply` and commit your change. Kotlin is formatted with Spotless (ktlint), and every source file carries the license header from `spotless/license-header.kt`.
3. Push the branch and open a pull request to `develop`. A GitHub Actions workflow runs `spotlessCheck` on pull requests to `develop` and `main`; run it yourself with `./gradlew spotlessCheck`.

### Code layout

```
app/src/main/java/org/com/hcmurs/
├── core/            # api, common, config, constant, di (Hilt modules)
├── model/           # data models
├── oauth/           # Google sign-in
├── repositories/    # Retrofit APIs and repositories
├── security/        # token handling
├── ui/
│   ├── components/
│   ├── screens/     # login, metro (home, tickets, maps, ...), news, notification, scanqr, staffhome, stationselection
│   └── theme/
├── utils/
├── FCMService.kt    # push messages
├── MainActivity.kt
└── Navigation.kt    # every route in one place
```

<details>
<summary><b>Design tokens</b></summary>

Colours, type, shapes and the rules for the illustrations in this README are in [assets/frontdoor.md](assets/frontdoor.md).

</details>

## Contact

- Organization: [HCMURS](https://github.com/hcmurs)
- Repository: [metro-app](https://github.com/hcmurs/metro-app)
