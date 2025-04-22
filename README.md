
# Kotlin Pong Game

A simple Pong game built using [LibGDX](https://libgdx.com/) and Kotlin. This project demonstrates the use of game development concepts such as physics, collision detection, animations, and UI rendering.

## Features

- **Classic Pong Gameplay**: Two paddles and a bouncing ball.
- **Main Menu**: Animated title, menu options, and particle effects.
- **Game Mechanics**: Paddle movement, ball collision, and scoring.
- **Pause and Resume**: Pause the game and resume at any time.
- **Custom Fonts**: High-quality fonts using FreeType.
- **Particle Effects**: Dynamic particle animations for visual enhancements.

## Project Structure

- `App.kt`: Entry point for the application. Configures the game window and launches the game.
- `PongGame.kt`: Main game class that manages screens.
- `MainMenu.kt`: Implements the main menu screen with animations and particle effects.
- `PongLevel.kt`: Core gameplay screen with paddles, ball, and scoring.
- `Paddle.kt`: Represents the paddles controlled by players.
- `Ball.kt`: Handles ball physics, movement, and collision detection.
- `ParticleSystem.kt`: Manages particle effects for animations.
- `build.gradle.kts`: Gradle build configuration for dependencies and application setup.

## Prerequisites

- **JDK**: Ensure you have JDK 21 or higher installed.
- **Gradle**: Gradle is required to build and run the project.
- **LibGDX**: The project uses LibGDX libraries for game development.

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/kotlin-pong.git
   cd kotlin-pong
   ```

2. Open the project in your favorite IDE (e.g., IntelliJ IDEA).

3. Build the project using Gradle:
   ```bash
   ./gradlew build
   ```

4. Run the game:
   ```bash
   ./gradlew run
   ```

## Controls

### Main Menu
- **Arrow Keys**: Navigate menu options.
- **Enter**: Select a menu option.

### Gameplay
- **W/S**: Move the left paddle up/down.
- **Arrow Keys (Up/Down)**: Move the right paddle up/down.
- **Space**: Pause/Resume the game.
- **Escape**: Return to the main menu.

## Dependencies

The project uses the following dependencies:
- `com.badlogicgames.gdx:gdx:1.13.1`: Core LibGDX library.
- `com.badlogicgames.gdx:gdx-backend-lwjgl3:1.13.1`: LWJGL3 backend for desktop.
- `com.badlogicgames.gdx:gdx-platform:1.13.1:natives-desktop`: Native platform bindings.
- `com.badlogicgames.gdx:gdx-freetype:1.13.1`: FreeType extension for fonts.

## Fonts and Assets

- **Fonts**: The game uses the `Roboto` font located in `assets/fonts/roboto.ttf`.
- **Audio**: Background music for the main menu is located in `assets/audio/menu_theme.wav`.
