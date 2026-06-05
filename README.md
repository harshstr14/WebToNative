# 🌐 𝗪𝗲𝗯𝗧𝗼𝗡𝗮𝘁𝗶𝘃𝗲

**WebToNative** is a modern Android application built with **Jetpack Compose** that seamlessly transforms web content into a native Android experience. The app combines secure Google Authentication, local browsing history management, and background notifications to provide a smooth and user-friendly browsing experience.

&nbsp;

## 🚀 𝟭. 𝗣𝗿𝗼𝗷𝗲𝗰𝘁 𝗦𝗲𝘁𝘂𝗽

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-repo/WebToNative.git
```

### 2️⃣ Open in Android Studio

Open the project using **Android Studio Ladybug (or newer)**.

### 3️⃣ Configure Firebase

#### 🔹 Create a Firebase Project
- Visit the Firebase Console.
- Create a new Firebase project.

#### 🔹 Register Android App
- Add an Android application with package name:

```text
com.example.webtonative
```

#### 🔹 Download Configuration File
- Download `google-services.json`.
- Place it inside the `app/` directory.

#### 🔹 Enable Firebase Services
- ✅ Google Authentication (Google Sign-In)
- ✅ Firebase Realtime Database

### 4️⃣ Configure Google Web Client ID

Update the `web_client_id` inside:

```xml
app/src/main/res/values/strings.xml
```

with your Firebase Web Client ID.

### 5️⃣ Build & Run

- Sync Gradle files.
- Run the application on an emulator or physical Android device.

&nbsp;

## 🔥 𝟮. 𝗙𝗶𝗿𝗲𝗯𝗮𝘀𝗲 𝗜𝗻𝘁𝗲𝗴𝗿𝗮𝘁𝗶𝗼𝗻

Firebase powers one core features of the application :

### 🔐 Authentication

The app uses **Google Sign-In** through `GoogleSignInManager`.

#### Features
- Secure Google account authentication
- Firebase credential exchange using Google ID tokens
- Persistent login sessions

&nbsp;

## 🏗️ 𝟯. 𝗔𝗿𝗰𝗵𝗶𝘁𝗲𝗰𝘁𝘂𝗿𝗲

The application follows the **MVVM (Model–View–ViewModel)** architecture pattern to maintain scalability and clean code separation.

### 🎨 View Layer (Jetpack Compose)

Responsible for rendering UI and observing ViewModel state.

#### Screens
- HomeScreen
- WebViewScreen
- HistoryScreen

### 🧠 ViewModel Layer

Handles business logic and UI state management using `StateFlow`.

#### ViewModels
- WebViewViewModel
- HistoryViewModel

### 📦 Repository Layer

Acts as the single source of truth for data operations.

#### Repository
- HistoryRepository

#### Responsibilities
- Room database interactions
- Data abstraction from UI layer

### 💉 Dependency Injection

Implemented using **Hilt**.

#### Benefits
- Cleaner code
- Easier testing
- Lifecycle-aware dependency management

&nbsp;

## 🗄️𝟰. 𝗗𝗮𝘁𝗮𝗯𝗮𝘀𝗲 𝗦𝗰𝗵𝗲𝗺𝗮

### 📱 Local Database (Room)

Used to store browsing history on the device.

#### Table : `history`

| Field | Description |
|---------|-------------|
| id | Auto-generated Primary Key |
| url | Visited website URL |
| title | Web page title |
| visit_count | Number of visits |
| last_visited_time | Last visit timestamp |

&nbsp;

## 🔔 𝟱. 𝗡𝗼𝘁𝗶𝗳𝗶𝗰𝗮𝘁𝗶𝗼𝗻 𝗙𝗹𝗼𝘄

The application includes a **"Welcome Back"** notification system that encourages users to return after leaving the app.

### 📌 Flow Overview

#### 1️⃣ Lifecycle Monitoring

`AppLifecycleObserver` monitors app state using `DefaultLifecycleObserver`.

When the app enters the background:

```kotlin
onStop()
```

is triggered.

#### 2️⃣ Notification Scheduling

A `WelcomeNotificationWorker` is scheduled using **WorkManager** with a **1-minute delay**.

#### 3️⃣ Validation Checks

Before displaying a notification, the worker verifies:

- Notification hasn't already been shown today
- App is still running in the background

#### 4️⃣ Notification Delivery

If all conditions are met:

```text
NotificationHelper
```

creates and displays the notification.

&nbsp;

## 🌍 𝟲. 𝗪𝗲𝗯𝗩𝗶𝗲𝘄 𝗟𝗶𝗳𝗲𝗰𝘆𝗰𝗹𝗲 𝗛𝗮𝗻𝗱𝗹𝗶𝗻𝗴

Managing a WebView inside Jetpack Compose requires special handling to survive configuration changes.

### 💾 State Preservation

`WebViewViewModel` stores a :

```kotlin
Bundle webViewState
```

#### containing :

- Navigation history
- Scroll position
- Current page state

### 🔄 Save & Restore Process

#### Saving State

Inside `DisposableEffect` :

```kotlin
webView.saveState(bundle)
```

is called when the composable is removed.

#### Restoring State

When the WebView is recreated :

```kotlin
webView.restoreState(bundle)
```

is executed instead of :

```kotlin
loadUrl()
```

#### This preserves :

- Back stack history
- Current webpage
- Scroll position

without reloading the page.

&nbsp;

## 📱 𝟳. 𝗔𝗽𝗽 𝗦𝘁𝗮𝘁𝗲 𝗛𝗮𝗻𝗱𝗹𝗶𝗻𝗴

The application properly handles :

### 🔄 Screen Rotation

- Preserves WebView state
- Prevents duplicate navigation
- Maintains UI consistency

### ⚙️ System Process Death

- Restores critical application state
- Recovers browsing session

### 🌗 Dark / Light Theme Changes

- Updates UI dynamically
- Retains current screen state
- Prevents unnecessary reloads

### ✅ Expected Results

- No crashes
- Correct state restoration
- No duplicate navigation events

&nbsp;

## ⚡ 𝟴. 𝗖𝗵𝗮𝗹𝗹𝗲𝗻𝗴𝗲𝘀 𝗙𝗮𝗰𝗲𝗱

### 🌐 WebView State Persistence

Jetpack Compose does not automatically preserve WebView state like traditional Fragments.

#### Solution

- Custom save/restore implementation using ViewModel and Bundle.

### 🔐 Notification Permission Handling

Android 13 introduced :

```text
POST_NOTIFICATIONS
```

permission requirements.

#### Solution

- Implemented a dedicated permission request flow in MainActivity.

### ⏳ Background Task Constraints

Notifications should not appear while the app is open.

#### Solution

- Synchronized WorkManager with ProcessLifecycleOwner state.

&nbsp;

## 🚀 𝟵. 𝗙𝘂𝘁𝘂𝗿𝗲 𝗜𝗺𝗽𝗿𝗼𝘃𝗲𝗺𝗲𝗻𝘁𝘀

### 📩 Firebase Cloud Messaging (FCM)

Replace local notifications with server-driven push notifications.

### ⭐ Bookmarks & Collections

Allow users to :
- Save favorite websites
- Organize bookmarks into folders

### 📶 Offline Browsing

Implement caching mechanisms for previously visited pages.

### 🔍 Advanced History Search

Add :
- Search functionality
- URL filtering
- History categorization

### 🌍 Multi-Tab Browsing

Support multiple active browsing sessions.

### 📊 Analytics Dashboard

Provide user insights such as :
- Most visited websites
- Daily browsing activity
- Usage statistics

&nbsp;

## ❤️ 𝗕𝘂𝗶𝗹𝘁 𝗪𝗶𝘁𝗵

- 🎨 Jetpack Compose
- 🔥 Firebase Authentication
- ☁️ Firebase Realtime Database
- 🗄️ Room Database
- 💉 Hilt Dependency Injection
- 🔄 StateFlow
- ⚙️ WorkManager
- 🌐 Android WebView
- 🧭 Navigation Compose
  
&nbsp;

## 🌟 𝗞𝗲𝘆 𝗙𝗲𝗮𝘁𝘂𝗿𝗲𝘀

- 🔐 Google Sign-In Authentication
- ☁️ Firebase User Profile Synchronization
- 🌐 Native WebView Experience
- 📜 Local Browsing History
- 🔔 Smart Welcome Back Notifications
- 🌗 Dynamic Dark & Light Theme Support
- 🔄 Configuration Change Handling
- 📱 Process Death Recovery
- ⚡ Modern MVVM Architecture

&nbsp;

**🚀 WebToNative — Bringing Web Experiences Closer to Native Android**
