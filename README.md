# ShelfWise - Library Management App

A modern Android application for efficient library management with real-time updates using Firebase.

## Features

- User Authentication (Sign up/Login)
- Remember Me functionality
- Book categorization and filtering
- Real-time book availability updates
- Book lending and return system
- Search functionality
- Modern Material Design 3 UI

## Setup Instructions

1. Clone the repository
2. Open the project in Android Studio Hedgehog (2023.1.1) or later

### Firebase Setup

1. Go to the [Firebase Console](https://console.firebase.google.com/)
2. Create a new project
3. Add an Android app to your Firebase project:
   - Package name: `com.example.shelflife`
   - App nickname: `ShelfWise`
4. Download the `google-services.json` file and place it in the `app` directory
5. Enable Authentication in Firebase Console:
   - Go to Authentication > Sign-in method
   - Enable Email/Password authentication
6. Enable Firestore Database:
   - Go to Firestore Database
   - Create database
   - Start in test mode
   - Choose a location closest to your users

### Build and Run

1. Sync project with Gradle files
2. Build the project
3. Run on an emulator or physical device (Android 7.0 or later)

## Technical Details

- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Language: Kotlin
- Architecture: MVVM with ViewBinding
- Build System: Gradle (Kotlin DSL)

## Dependencies

### Core Android
- AndroidX Core KTX: 1.12.0
- AppCompat: 1.6.1
- Material Design Components: 1.11.0
- ConstraintLayout: 2.1.4

### Architecture Components
- ViewModel KTX: 2.7.0
- LiveData KTX: 2.7.0
- Activity KTX: 1.8.2

### Firebase
- Authentication KTX: 22.3.1
- Analytics KTX: 21.5.1

### Image Loading
- Glide: 4.16.0

### Testing
- JUnit: 4.13.2
- AndroidX Test Ext: 1.1.5
- Espresso Core: 3.5.1

## Development Notes

- The app uses ViewBinding for view access
- Follows Material Design 3 guidelines for consistent UI/UX
- Implements MVVM architecture for clean separation of concerns
- Uses Kotlin Coroutines for asynchronous operations
- Firebase operations use transactions to ensure data consistency 