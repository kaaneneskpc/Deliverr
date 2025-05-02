# Deliverr

![image](https://github.com/user-attachments/assets/8d9eaf23-327a-41b9-84f3-d2cc5dcdda41)


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

<img src="https://github.com/user-attachments/assets/b6357e72-6ccf-4dfd-8b71-908f53150d79" height = "800px"/>


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
- **Storage**: Supabase

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

### Customer App

 <img src="https://github.com/user-attachments/assets/b9bab37a-2e8e-4c57-ba71-fc4978d92cb8" height = "600px"/> | <img src="https://github.com/user-attachments/assets/ac2cd4e4-b308-4198-9788-f2770671f922" height = "600px"/> | <img src="https://github.com/user-attachments/assets/ab92e7a7-f7ed-4c55-8a74-31ce45550e28" height = "600px"/> | <img src="https://github.com/user-attachments/assets/e1943d05-e915-4c21-a5e8-063507500ad4" height = "600px"/> |  <img src="https://github.com/user-attachments/assets/88589d4b-2aba-4e99-a5cd-198164261682" height = "600px"/>  | <img src="https://github.com/user-attachments/assets/919e89c5-79c4-4b3f-a57c-8202f5f5161c" height = "600px"/> |  <img src="https://github.com/user-attachments/assets/37b9a2eb-f592-4bd7-be0b-ff31da32a388" height = "600px"/> |  <img src="https://github.com/user-attachments/assets/34fbe28e-aaeb-42a0-abee-79be0cdef40b" height = "600px"/> |  <img src="https://github.com/user-attachments/assets/07e3b70a-1e9b-4253-8060-10a7d55ea7db" height = "600px"/> |  <img src="https://github.com/user-attachments/assets/534af424-7e49-4c41-ba1b-99b391db7a2e" height = "600px"/>

### Restaurant App

 <img src="https://github.com/user-attachments/assets/b26b16ef-d4c5-4f08-a14a-737e14d8c9c7" height = "600px"/> | <img src="https://github.com/user-attachments/assets/d6c0b762-890b-49c8-8a75-910151cc3d22" height = "600px"/> | <img src="https://github.com/user-attachments/assets/3139d603-5a7f-41f9-be8e-0eeee1e08650" height = "600px"/> | <img src="https://github.com/user-attachments/assets/854dd37a-c651-4d45-985b-586dc33d484f" height = "600px"/> |  <img src="https://github.com/user-attachments/assets/69837c34-f15d-47e8-b7ff-a259a04e6cbc" height = "600px"/>  | <img src="https://github.com/user-attachments/assets/269df9e8-9d4b-44b0-b159-48aa124f2e64" height = "600px"/> 


### Rider App


| Available Orders | Navigation | Delivery Status | Earnings |
|------------------|------------|----------------|----------|
| ![Available](https://github.com/user-attachments/assets/rider_available.jpg) | ![Navigation](https://github.com/user-attachments/assets/rider_navigation.jpg) | ![Status](https://github.com/user-attachments/assets/rider_status.jpg) | ![Earnings](https://github.com/user-attachments/assets/rider_earnings.jpg) |

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
