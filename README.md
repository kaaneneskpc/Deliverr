# Deliverr

A modern food delivery platform built with cutting-edge Android technologies.

![Deliverr](https://via.placeholder.com/800x400?text=Deliverr+App)

## Overview

Deliverr is a comprehensive food delivery ecosystem that connects customers, restaurants, and delivery riders through dedicated applications. Built with modern Android development practices, the platform offers a seamless experience for ordering food, managing restaurants, and delivering orders.

## Key Features

### Customer App
- Browse restaurants and menus
- Search for specific food or restaurants
- Order customization and tracking
- Multiple payment methods
- Address management
- Order history
- Real-time delivery updates
- Push notifications

### Restaurant App
- Order management dashboard
- Menu management
- Restaurant profile customization
- Order status updates
- Sales analytics
- Notification system for new orders

### Rider App
- Order pickup and delivery management
- Route optimization
- Delivery status updates
- Earnings tracking
- Real-time navigation

## Architecture

Deliverr is built using:
- **MVVM Architecture**: Clear separation of UI, business logic, and data layers
- **Clean Architecture**: Domain-centric approach with clear boundaries
- **Jetpack Compose**: Modern declarative UI framework
- **Multi-module Structure**: Separate modules for different app variants

## Tech Stack

- **UI**: Jetpack Compose, Material 3 Design, Compose Navigation, Animations
- **Asynchronous Operations**: Kotlin Coroutines, Flow
- **Dependency Injection**: Dagger Hilt
- **Networking**: Retrofit, OkHttp, Interceptors
- **Image Loading**: Coil
- **Maps & Location**: Google Maps Compose, Play Services Location
- **Authentication**: Google Identity, Facebook Login, Credentials
- **Payment Processing**: Stripe
- **Data Serialization**: Kotlinx Serialization
- **Push Notifications**: Firebase Cloud Messaging

## Project Setup

### Requirements
- Android Studio Hedgehog (2023.1.1) or higher
- JDK 11 or above
- Minimum SDK: 24
- Target SDK: 35

### Configuration
1. Clone the repository
   ```
   git clone https://github.com/yourusername/deliverr.git
   ```

2. API Keys
   The following API keys are required to run the project:
   - Google Maps API key
   - Firebase configuration
   - Facebook App ID
   - Stripe API key

3. Build Variants
   The project has three build variants:
   - `customer`: The main customer-facing application
   - `restaurant`: Restaurant management application
   - `rider`: Delivery personnel application

   Select the appropriate build variant in Android Studio before running the app.

4. Run the application
   Connect a device or use an emulator to run the application.

## Screenshots

| Home Screen | Restaurant Detail | Cart | Order Tracking |
|-------------|-------------------|------|---------------|
| ![Home](https://via.placeholder.com/200?text=Home) | ![Restaurant](https://via.placeholder.com/200?text=Restaurant) | ![Cart](https://via.placeholder.com/200?text=Cart) | ![Tracking](https://via.placeholder.com/200?text=Tracking) |

## Future Enhancements

- Real-time order tracking
- Multi-language support
- Dark theme
- Order sharing functionality
- Enhanced analytics
- Integration with more payment gateways
- Advanced restaurant recommendations

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Credits

This repo is inspired by [furqanullah717's Shopper-compose](https://github.com/furqanullah717/Shopper-compose)

## Contact

Project Link: [https://github.com/yourusername/deliverr](https://github.com/yourusername/deliverr)
