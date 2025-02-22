
# Deniseshop E-commerce App

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.0-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

Modern e-commerce Android application built with Kotlin, Jetpack Compose, and modern Android architecture components. Features a clean architecture implementation with MVVM pattern.

## Key Features

**Core Functionality:**
- User authentication (Login/Register/Forgot Password)
- Product browsing & search
- Categories functionality
- Shopping cart management
- Wishlist functionality
- Order functionality
- Coupon code redemption
- Address management
- Brand exploration

## Tech Stack and Libraries

**Architecture:**
- Clean Architecture (Data ➔ Domain ➔ Presentation)
- MVVM Pattern
- Use Cases (Business Logic Separation)

**Libraries:**
- 🖼️ **UI**: Jetpack Compose, Material3
- 📡 **Networking**: Retrofit 2, OkHttp3, Moshi
- 💉 **DI**: Dagger Hilt
- 🔄 **Async**: Kotlin Coroutines, Flow
- 🖼️ **Image Loading**: Coil
- � **Navigation**: Compose Navigation
- 📦 **Persistence**: DataStore

## Installation

**Prerequisites:**
- Android Studio Koala | 2024.1.1 or later
- Android SDK 33+
- Java 17

**Steps:**
1. Clone the repository:
   ```bash
   git clone https://github.com/dennis-o-a/deniseshop.git
   ```
2. Open project in Android Studio
3. Build project (will resolve Gradle dependencies)
4. Run on emulator or physical device (min API 24)

## Project Structure

```
📦 deniseshop
├─ 📂 app
│  └─ 📂 src/main
│     ├─ 📂 common
│     │  ├─ 📂 event
│     │  └─ 📂 state 
│     ├─ 📂 data
│     │  ├─ 📂 api
│     │  ├─ 📂 datastore
│     │  ├─ 📂 models    # Api/local data classes
│     │  ├─ 📂 repository   # Implementation repositories
│     │  └─ 📂 source # Implementation datasources
│     ├─ 📂 di #dependecy modules
│     │  ├─ 📂 api   
│     │  ├─ 📂 datastore 
│     │  ├─ 📂 mappers  
│     │  ├─ 📂 repository 
│     │  ├─ 📂 source    
│     │  └─ 📂 usecase 
│     ├─ 📂 domain
│     │  ├─ 📂 models    # Data classes
│     │  └─ 📂 usecase  # Business logic cases
│     ├─ 📂 navigateion
│     │  ├─ 📂 NavGraph
│     │  └─ 📂 Routes
│     ├─ 📂 ui
│     │  ├─ 📂 components # Reusable UI components
│     │  ├─ 📂 mapper  # Data models mappers data to ui
│     │  ├─ 📂 models # Ui models classes
│     │  ├─ 📂 screens    # All application screens with viewmodels & events 
│     │  └─ 📂 theme # App theming 
│     └─ 📂 utils # contains app utils
└─ 📂 gradle
```

## Screenshots

| Home                                 | Products                              | Flash Sales                                 | Categories                                | Cart                          | Wishlist                              | Register                              | Login                              | Profile                              |
|--------------------------------------|---------------------------------------|---------------------------------------------|-------------------------------------------|-------------------------------|---------------------------------------|---------------------------------------|------------------------------------|--------------------------------------|
| ![Home Screen](screenshots/home.png) | ![Products](screenshots/products.png) | ![Flash Sales](screenshots/flash-sales.png) | ![Categories](screenshots/categories.png) | ![Cart](screenshots/cart.png) | ![Wishlist](screenshots/wishlist.png) | ![Wishlist](screenshots/register.png) | ![Wishlist](screenshots/login.png) | ![Wishlist](screenshots/profile.png) |


## API 

The Deniseshop App uses the [Deniseshop Web App](https://github.com/dennis-o-a/deniseshop-web-app.git) for RESTful api.</br>


## Security

- 🔒 JWT Authentication with refresh token flow
- 🔄 Secure token refresh mechanism
- 🚫 SSL certificate validation

## Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

Distributed under the MIT License. See [LICENSE](LICENSE) for more information.

## Acknowledgments

- Jetpack Compose Team
- Android Developers Community
- Retrofit & OkHttp Maintainers
- Dagger Hilt Team
- Coil Image Loading Library


