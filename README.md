# Deliverr

A modern food delivery platform built with cutting-edge Android technologies.

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

### Architecture Diagram

![Architecture Diagram](https://via.placeholder.com/800x500?text=Deliverr+Architecture+Diagram)

The above diagram illustrates the application's architecture, showcasing:

- **Presentation Layer**: UI components (Compose), ViewModels, and UI States
- **Domain Layer**: Use cases, domain models, and repository interfaces
- **Data Layer**: Repository implementations, data sources (local and remote)
- **Common**: Shared utilities, extensions, and base components
- **Module Relationships**: Dependencies between different modules and components
- **Data Flow**: How data flows through the system from user actions to data persistence

This architecture follows clean architecture principles with clear separation of concerns, making the codebase maintainable, testable, and scalable.

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

## Security

Deliverr implements several security measures to protect user data and ensure secure transactions:

- **Data Encryption**: All sensitive data is encrypted using industry-standard encryption protocols
- **Secure API Communication**: All API communications use HTTPS with certificate pinning
- **Authentication Security**: Multi-factor authentication options and secure token management
- **PCI Compliance**: Payment processing follows PCI DSS guidelines
- **Data Minimization**: Only essential user data is collected and stored
- **Regular Security Audits**: Continuous security testing and vulnerability assessments
- **Privacy Controls**: Users have full control over their data sharing preferences
- **Secure Storage**: Sensitive information is stored using Android's secure storage mechanisms
- **Input Validation**: All user inputs are validated to prevent injection attacks
- **Session Management**: Secure session handling with automatic timeouts

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

This repo is inspired by [furqanullah717's Shopper-compose](https://github.com/furqanullah717/foodhub-compose)

## Contact

**Kaan Enes Kapıcı**
- LinkedIn: [Kaan Enes Kapıcı](https://www.linkedin.com/in/kaaneneskpc/)
- GitHub: [@kaaneneskpc](https://github.com/kaaneneskpc)
- Email: kaaneneskpc1@gmail.com

Feel free to reach out for any questions, suggestions, or collaboration opportunities! 
