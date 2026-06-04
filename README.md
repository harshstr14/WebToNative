# WebToNative

WebToNative is a modern Android application built with Jetpack Compose that seamlessly converts web content into a native app experience. It features Google Authentication, real-time user profile synchronization, local browsing history, and intelligent background notifications.

## 1. Project Setup Steps

1.  **Clone the Repository**:
    ```bash
    git clone https://github.com/your-repo/WebToNative.git
    ```
2.  **Open in Android Studio**:
    Open the project in Android Studio Ladybug (or newer).
3.  **Firebase Configuration**:
    -   Create a new project in the [Firebase Console](https://console.firebase.google.com/).
    -   Add an Android App with the package name `com.example.webtonative`.
    -   Download `google-services.json` and place it in the `app/` directory.
    -   Enable **Google Sign-In** in the Firebase Authentication settings.
    -   Enable **Realtime Database**.
4.  **Google Web Client ID**:
    -   Update the `web_client_id` in `app/src/main/res/values/strings.xml` with your Firebase Web Client ID.
5.  **Build and Run**:
    -   Sync the project with Gradle files.
    -   Run the app on an emulator or a physical device.

## 2. Firebase Setup Explanation

The app utilizes Firebase for two primary purposes:
-   **Authentication**: Implements Google Sign-In via `GoogleSignInManager`. It handles the exchange of Google ID tokens for Firebase credentials, allowing secure user access.
-   **Realtime Database**: Automatically creates and maintains user profiles. When a user signs in for the first time, their name, email, and high-quality profile picture URL (processed from the Google account) are stored under a unique `Users/$userID` node.

## 3. Architecture Explanation

The project follows the **MVVM (Model-View-ViewModel)** architecture pattern, ensuring a clean separation of concerns:
-   **View (Jetpack Compose)**: UI components like `HomeScreen`, `WebViewScreen`, and `HistoryScreen` observe state from ViewModels.
-   **ViewModel**: `WebViewViewModel` and `HistoryViewModel` manage the business logic and expose state using `StateFlow`.
-   **Repository**: `HistoryRepository` acts as a single source of truth for browsing data, abstracting the Room database.
-   **Dependency Injection**: Powered by **Hilt**, which simplifies the management of singleton instances like the database and repositories.

## 4. Database Schema Explanation

### Local Database (Room)
Used for storing browsing history locally on the device.
-   **Table**: `history`
-   **Fields**:
    -   `id`: Primary Key (Auto-generated)
    -   `url`: The visited web address.
    -   `title`: The page title.
    -   `visit_count`: Number of times the URL was visited.
    -   `last_visited_time`: Timestamp of the most recent visit.

### Remote Database (Firebase RTDB)
Used for cross-device user profile persistence.
-   **Root Node**: `Users`
-   **Child Node**: `userID` (Firebase UID)
    -   `name`: User's display name.
    -   `mail`: User's email address.
    -   `photoUrl`: Link to the user's Google profile picture.

## 5. Notification Flow Explanation

The app implements a "Welcome Back" notification system that triggers after the user leaves the app:
1.  **Lifecycle Observation**: An `AppLifecycleObserver` (using `DefaultLifecycleObserver`) monitors when the app enters the background (`onStop`).
2.  **Scheduling**: When the app is stopped, a `WelcomeNotificationWorker` is scheduled using **WorkManager** with a 1-minute delay.
3.  **Verification**: The worker checks if a notification has already been shown today and ensures the app is still in the background before posting.
4.  **Posting**: If conditions are met, a notification is displayed via `NotificationHelper`.

## 6. WebView Lifecycle Handling Explanation

Managing a `WebView` within Jetpack Compose requires careful handling of its state during configuration changes (like screen rotation):
-   **State Preservation**: The `WebViewViewModel` holds a `Bundle` named `webViewState`.
-   **Save/Restore**: 
    -   In `WebViewScreen`, a `DisposableEffect` is used to call `webView.saveState(bundle)` when the composable is disposed.
    -   When the `AndroidView` is (re)created, it checks `webViewViewModel.webViewState`. If present, it calls `restoreState()` instead of `loadUrl()`, preserving the back stack and scroll position.

## 7. Challenges Faced

-   **WebView State Persistence**: Unlike native Fragments/Activities, Compose doesn't automatically preserve `WebView` state on configuration changes. Implementing a manual save/restore mechanism via the ViewModel was necessary.
-   **Permission Handling**: Managing the `POST_NOTIFICATIONS` permission introduced in Android 13 required a robust request flow in `MainActivity` to ensure background workers could actually notify the user.
-   **Background Task Constraints**: Ensuring the `WorkManager` didn't fire notifications while the app was still in the foreground required careful synchronization with the `ProcessLifecycleOwner`.

## 8. Future Improvements

-   **FCM Integration**: Replace local scheduled notifications with Firebase Cloud Messaging for server-side push notifications.
-   **Bookmarks & Folders**: Add functionality to save favorite URLs into organized folders within the local Room database.
-   **Offline Support**: Implement web caching strategies to allow users to view previously visited pages without an active internet connection.
-   **Enhanced Search**: Add a search bar to the History screen to filter through large sets of browsing data.
