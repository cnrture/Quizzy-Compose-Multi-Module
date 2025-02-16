![quiz-app-github](https://github.com/user-attachments/assets/14d963cf-1f91-4d54-a0a0-2bf314b3b034)

A modern **Quiz App** built with **Jetpack Compose** and organized using a **multi-module architecture**. This project demonstrates practices in Android development, emphasizing scalability, maintainability, and clean architecture.

## Tech

- Multi-Module-Architecture
- Jetpack Compose
- Material Design
- Coroutines - Flow
- Retrofit
- KotlinX Serialization
- Hilt
- Navigation
- Coil
- Detekt
- DataStore
- Chucker

## Modules

This project is organized into the following modules:

### **Build-Logic**
- **Convention**: Centralizes the project's build and configuration logic.

### **App**
- **App Module**: Serves as the entry point of the application and contains global configurations.

### **Core**
- **Common**: Contains reusable components and shared utilities used across multiple modules.
- **Connectivity**: Manages network connection checks and status handling.
- **Datasource**: Handles data sources related to user sessions and profiles.
  - **Logout**: Manages data operations for user logout functionality.
  - **Profile**: Manages data related to user profiles.
- **Datastore**: Handles persistent data storage across the app.
- **Network**: Manages network requests and API calls.
- **UI**: Provides shared UI components and styles.

### **Feature**
- **Category**: Handles categories, including data, business logic, and UI layers.
- **Detail**: Manages screens for displaying detailed information.
- **EditProfile**: Handles functionality for editing user profiles.
- **Favorites**: Manages the user’s favorite items or data.
- **Home**: Contains functionality for the home screen.
- **Leaderboard**: Handles leaderboard data and presentation.
- **Login**: Manages user login functionality.
- **Profile**: Provides access to user profile screens and features.
- **Quiz**: Handles the question-answer flow and quiz management.
- **Register**: Manages user registration functionality.
- **Search**: Handles search functionality and result display.
- **Splash**: Manages the splash screen displayed at the app start.
- **Summary**: Displays quiz summaries and results.
- **Welcome**: Manages the welcome screen of the app.

### **Navigation**
- **Navigation Module**: Handles the navigation logic for transitions between screens in the app.

## Screenshots
  
| Splash | Welcome | Register |
| - | - | - |
|<img src="https://github.com/user-attachments/assets/13b2e431-635d-4611-bab5-8a260fbbe682" width = "250"/>|<img src="https://github.com/user-attachments/assets/27f45ac8-5256-4e85-980c-3479047d0113" width = "250"/>|<img src="https://github.com/user-attachments/assets/819d13c2-d55e-4644-bab0-f698723db5db" width = "250"/>|
|<img src="https://github.com/user-attachments/assets/8b495ef6-24b1-4b43-a6be-6171e7e99b22" width = "250"/>|<img src="https://github.com/user-attachments/assets/5945488d-37eb-45dd-a4a6-bd211f1147c5" width = "250"/>|<img src="https://github.com/user-attachments/assets/b1a709ce-ccdc-4107-b45f-55f82676688f" width = "250"/>|

| Login | Forgot Password | Home |
| - | - | - |
|<img src="https://github.com/user-attachments/assets/d4573963-8545-42ee-8761-05288720ca69" width = "250"/>|<img src="https://github.com/user-attachments/assets/20be6c31-b68b-4339-9232-0a562a39c866" width = "250"/>|<img src="https://github.com/user-attachments/assets/a2c6df3a-4a1a-4d7f-8fcb-76d34b1809d0" width = "250"/>|
|<img src="https://github.com/user-attachments/assets/fa6a5bb1-1f0e-4433-b700-8f64a30e2164" width = "250"/>|<img src="https://github.com/user-attachments/assets/ee10c649-812c-4c40-bd37-f353294a7e74" width = "250"/>|<img src="https://github.com/user-attachments/assets/d0d74778-83cb-49e8-8e01-519c5c5da4bb" width = "250"/>|

| Favorites | Leaderboard | Search |
| - | - | - |
|<img src="https://github.com/user-attachments/assets/679ea085-0422-425f-a82e-be369e6f97ab" width = "250"/>|<img src="https://github.com/user-attachments/assets/6a738fb7-d63e-417f-b651-7aeb027e105b" width = "250"/>|<img src="https://github.com/user-attachments/assets/d647c5df-5ad2-4f72-b32a-6fcb3d268c0e" width = "250"/>|
|<img src="" width = "250"/>|<img src="" width = "250"/>|<img src="" width = "250"/>|

| Detail | Quiz | Summary |
| - | - | - |
|<img src="https://github.com/user-attachments/assets/71092b18-49bf-4741-b045-cbfbf166588f" width = "250"/>|<img src="https://github.com/user-attachments/assets/3dc28579-0d5d-495f-984e-e7a5d974bedb" width = "250"/>|<img src="https://github.com/user-attachments/assets/66f4173f-6ac2-4c95-8c5c-d81eed998cd6" width = "250"/>|
|<img src="" width = "250"/>|<img src="" width = "250"/>|<img src="" width = "250"/>|

| Profile | Edit Profile | Select Avatar |
| - | - | - |
|<img src="https://github.com/user-attachments/assets/bc1f28cc-661f-4b80-89ba-4794b69c1fe2" width = "250"/>|<img src="https://github.com/user-attachments/assets/6db9f4de-1286-49f8-b1b2-c7d74abdf411" width = "250"/>|<img src="https://github.com/user-attachments/assets/3647aea0-d9ef-4b9a-8cfc-ffa75585fbb2" width = "250"/>|
|<img src="" width = "250"/>|<img src="" width = "250"/>|<img src="" width = "250"/>|

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/cnrture/Quizzy-Compose-Multi-Module.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle to download all dependencies.
4. You need to provide Firebase connection, add google.services.json into the app and add client id in local.properties as follows
   ```bash
   SERVER_CLIENT_ID=YOUR_CLIENT_ID
   ```
   <img src="https://github.com/user-attachments/assets/c3ba48ea-2694-461d-9722-d0af681f0388" width = "800"/>
5. Run the app on an emulator or physical device.

## Roadmap

- [ ] Add unit tests.
- [ ] Add UI tests.
- [x] Add dark mode support.

## Contribution

Contributions are always welcome! Here’s how you can help:

1. Fork the repository.
2. Create a new branch for your feature (`feature/your-feature-name`).
3. Commit your changes and push the branch.
4. Open a pull request.
