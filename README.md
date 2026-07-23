## 📸 Showcase

| Setup Screen | Live Stream |
| :-: | :---: |
| <video src="https://github.com/user-attachments/assets/fd1b70fb-b72a-4079-a215-d1ee59b0d6ca" /> | <video src="https://github.com/user-attachments/assets/530e0016-433b-4fff-b641-40a695138bff" width="300" />  |

# FakeIGLive

Simple Android app that simulates the Instagram Live broadcasting experience. Built with modern Android development standards, this project serves as a showcase for Clean Architecture, MVI, and high-performance UI animations.

## 🚀 Key Features
- **Simulated Live Stream:** Real-time comment feed, fluctuating viewer counts, and high-frequency heart burst animations using Kotlin Flow.
- **Camera Integration:** Seamless camera preview management using CameraX.
- **Persistence:** Local storage of user profiles using Jetpack DataStore.
- **Localization:** Full support for English and Turkish languages.
- **Theming:** Adaptive Light and Dark mode support with a custom-built Design System.

## 🛠 Tech Stack
- **UI:** Jetpack Compose with a custom Design System (Dimensions, Theme, reusable Components).
- **Architecture:** Multi-module Clean Architecture (App, Domain, Data, Core, Feature).
- **Design Pattern:** MVI (Model-View-Intent) for predictable state management.
- **Dependency Injection:** Dagger Hilt for modular and automated DI.
- **Navigation:** Jetpack Navigation with type-safe route definitions.
- **Concurrency:** Kotlin Coroutines & Flow for asynchronous stream handling.
- **Storage:** Jetpack DataStore (Preferences).
- **Hardware:** CameraX for camera lifecycle management.
- **Testing:** Comprehensive test suite including Unit Tests (MockK, Turbine) and Compose UI Tests.

## 📂 Project Structure
- `:app` - Entry point, Hilt modules, and Global Navigation.
- `:domain` - Pure Kotlin module containing Business Logic, Models, and Repository Contracts.
- `:data` - Implementation of data sources (Mock, DataStore) and Repositories.
- `:feature:setup` - Onboarding flow where users configure their profile.
- `:feature:live` - The main broadcasting simulation screen.
- `:core` - Shared UI components, Theme, Spacing, and Utilities.

## 🧪 Testing
The project maintains high code quality through:
- **Unit Tests:** Logic validation for ViewModels and Use Cases.
- **UI Tests:** Visual and interaction verification using Compose UI Test.
