# ShelfLife - Library Management App

A simple Android app for managing a library's book inventory with real-time updates using Firebase.

## Features

- User Authentication (Sign up/Login)
- Remember Me functionality
- Book categorization and filtering
- Real-time book availability updates
- Book lending and return system
- Search functionality
- Modern Material Design UI

## Setup Instructions

1. Clone the repository
2. Open the project in Android Studio Meerkat or later

### Firebase Setup

1. Go to the [Firebase Console](https://console.firebase.google.com/)
2. Create a new project
3. Add an Android app to your Firebase project:
   - Package name: `com.example.shelflife`
   - App nickname: `ShelfLife`
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
3. Run on an emulator or physical device

## Technical Details

- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Language: Kotlin
- Architecture: MVVM with ViewBinding

## Libraries Used

- Firebase Authentication
- Firebase Firestore
- Glide for image loading
- Material Design Components
- AndroidX libraries

## Notes

- The app automatically seeds some sample books on first launch
- Default book cover images are placeholder URLs and should be replaced with actual images
- All Firebase operations use transactions to ensure data consistency 