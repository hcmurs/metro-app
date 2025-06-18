# Metro App

A modern Android application for Ho Chi Minh City Metro system built with Kotlin and Jetpack Compose.

## 📱 About

Metro App is a comprehensive mobile application designed to help users navigate and utilize the Ho Chi Minh City Metro system. The app provides real-time information, route planning, ticket management, and various metro-related services.

## ✨ Features

- **🏠 Home Dashboard**: Quick access to metro services and information
- **🎫 Ticket Management**: 
  - Buy tickets digitally
  - View ticket information
  - Redeem ticket codes
  - Track your tickets
- **🗺️ Navigation & Maps**: 
  - Interactive metro maps
  - Route planning between stations
  - Real-time station information
- **📍 Station Information**: Detailed information about metro and bus stations
- **🎉 Events**: Stay updated with metro system events and announcements
- **👤 User Profile**: Manage your account and preferences
- **💬 Feedback**: Share your experience and suggestions
- **🔗 Cooperation Links**: Access partner services and integrations
- **⚙️ Settings**: Customize your app experience

## 🛠️ Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Hilt Dependency Injection
- **Navigation**: Jetpack Navigation Compose
- **Networking**: Retrofit with OkHttp
- **Authentication**: OAuth2 integration
- **Maps**: OSMDroid for offline mapping
- **Local Storage**: SharedPreferences
- **Build System**: Android Gradle Plugin

## 📋 Prerequisites

- Android Studio Arctic Fox or later
- Android SDK 24 (Android 7.0) or higher
- Kotlin 1.8.0 or later
- Gradle 7.0 or later

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/hcmurs/metro-app.git
cd metro-app
```

### 2. Open in Android Studio

1. Launch Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to the cloned repository folder
4. Click "OK" to open the project

### 3. Build and Run

1. Wait for Gradle sync to complete
2. Connect an Android device or start an emulator
3. Click the "Run" button or press `Ctrl+R` (Windows/Linux) or `Cmd+R` (Mac)

## 🏗️ Project Structure

```
app/
├── src/main/java/org/com/hcmurs/
│   ├── di/                     # Dependency Injection modules
│   │   ├── AppModule.kt
│   │   ├── AuthModule.kt
│   │   └── NetworkModule.kt
│   ├── model/                  # Data models
│   │   ├── BusStop.kt
│   │   ├── Event.kt
│   │   ├── UserProfile.kt
│   │   └── ...
│   ├── oauth/                  # OAuth2 authentication
│   ├── repositories/           # Data repositories
│   ├── security/               # Security and token management
│   ├── ui/
│   │   ├── screens/           # UI screens
│   │   └── theme/             # App theming
│   ├── utils/                 # Utility functions
│   ├── MainActivity.kt        # Main activity
│   └── Navigation.kt          # Navigation setup
```

## 🔧 Configuration

### Backend Configuration

The app connects to a backend server. Update the base URL in `NetworkModule.kt`:

```kotlin
private val BASE_URL = "http://10.0.2.2:4003/" // For emulator
// or
private val BASE_URL = "https://your-api-domain.com/" // For production
```

### OAuth2 Setup

Configure OAuth2 settings in the `AuthModule.kt` and ensure your backend supports the authentication flow.

## 🎯 Key Components

### Navigation
The app uses a comprehensive navigation system with multiple screens including:
- Login and authentication
- Home dashboard
- Ticket management
- Maps and routing
- User profile
- Settings and feedback

### Data Models
- **BusStop**: Metro and bus station information
- **Event**: Metro system events and announcements
- **UserProfile**: User account and social information
- **NoteItem**: User notes and reminders

### Architecture
- **MVVM Pattern**: Clean separation of concerns
- **Hilt DI**: Dependency injection for better testability
- **Repository Pattern**: Centralized data management
- **Compose UI**: Modern declarative UI framework

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

- **Organization**: [HCMURS](https://github.com/hcmurs)
- **Repository**: [metro-app](https://github.com/hcmurs/metro-app)

## 🙏 Acknowledgments

- Ho Chi Minh City Metro system for inspiration
- Android Jetpack Compose team for the excellent UI framework
- OSMDroid community for offline mapping capabilities
- All contributors who help improve this project

---

**Note**: This app is designed specifically for the Ho Chi Minh City Metro system. Some features may require network connectivity and backend services to function properly.
