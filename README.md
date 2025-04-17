# KotlinPlayground with LibGDX

This project uses [Gradle](https://gradle.org/) and integrates [LibGDX](https://libgdx.com/) - a cross-platform game development framework. It implements a simple bouncing ball game with a main menu navigation system.

## Build and Run

To build and run the application, use the *Gradle* tool window by clicking the Gradle icon in the right-hand toolbar,
or run it directly from the terminal:

* Run `./gradlew run` to build and run the application.
* Run `./gradlew build` to only build the application.
* Run `./gradlew check` to run all checks, including tests.
* Run `./gradlew clean` to clean all build outputs.

Note the usage of the Gradle Wrapper (`./gradlew`).
This is the suggested way to use Gradle in production projects.

[Learn more about the Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html).

[Learn more about Gradle tasks](https://docs.gradle.org/current/userguide/command_line_interface.html#common_tasks).

## Project Structure

This project consists of the following main components:

- `App.kt` - Application entry point
- `PongGame.kt` - Main game class that manages screens
- `MainMenu.kt` - Menu screen implementation
- `Ball.kt` - Bouncing ball implementation with physics
- `app` module with game code and the shared build logic in `buildSrc`

The shared build logic was extracted to a convention plugin located in `buildSrc`.

This project uses a version catalog (see `gradle/libs.versions.toml`) to declare and version dependencies
and both a build cache and a configuration cache (see `gradle.properties`).

## Development Status

### Game Core Implementation

The `PongGame` class serves as the main game controller:
* Extends LibGDX's `Game` class to manage screens
* Creates and sets the main menu as the initial screen
* Handles proper resource disposal

### Ball Physics Implementation

The `Ball` class implements a bouncing ball with:

* Position and velocity tracking with delta time-based movement
* Border collision detection with position correction
* Dynamic color changes on collision with borders
* Random color selection from a predefined palette
* Proper rendering using LibGDX's ShapeRenderer
* Configurable size and initial position

### Main Menu Implementation

The `MainMenu` class is fully implemented with:

* Three menu options (Play Game, Settings, Exit) with selected item highlighting
* Keyboard (arrow keys) and mouse/touch input handling
* Centered text rendering with proper alignment
* Separate font sizes and styles for different UI elements
* Proper resource management with disposal when the screen is closed
* Smooth color changes for selected menu items
* Centralized layout with computed positioning based on screen dimensions

### Font Rendering with FreeType

The menu implements high-quality text rendering using LibGDX's FreeType extension:

* **Multiple Font Instances**: Instead of scaling a single font (which causes blurriness), separate font instances are created:
  * Title font: 45px for the game title
  * Menu font: 30px for menu items
  * Instruction font: 15px for the footer instructions
  
* **Font Generation Process**:
  * FreeTypeFontGenerator loads the TTF file from resources
  * FreeTypeFontParameter configures size, color, and other properties
  * Each font is generated at its natural size for maximum clarity
  * The generator is disposed after all fonts are created

* **Font Asset Location**:
  * TTF font file is stored in the `app/src/main/resources/fonts/` directory
  * The Roboto font is used for all text elements

* **Resource Management**:
  * All fonts are properly disposed in the `dispose()` method
  * The font generator is disposed immediately after use

### Menu Navigation

The menu supports both keyboard and mouse/touch controls:

* **Keyboard Navigation**:
  * UP/DOWN arrow keys move the selection indicator
  * ENTER key confirms the selection
  * Selection wraps around from top to bottom and vice versa
  
* **Mouse/Touch Navigation**:
  * Click/tap directly on menu items to select them
  * Hitboxes are calculated based on the text dimensions for accurate selection

* **Visual Feedback**:
  * Selected menu item turns yellow while others remain white
  * Text remains centered regardless of length

### Menu Structure

The menu has three options:
* **Play Game** - Will start the game (implementation in progress)
* **Settings** - Will open settings screen (implementation pending)
* **Exit** - Exits the application immediately

## LibGDX Integration

The app module integrates LibGDX 1.13.1 with the following components:

* **Core LibGDX** - Cross-platform game development framework
* **LWJGL3 Backend** - Desktop rendering backend
* **FreeType Extension** - High-quality font rendering
* **Platform Bindings** - Native libraries for desktop platforms

### Dependencies

The project includes the following LibGDX dependencies:
* `gdx:1.13.1` - Core LibGDX library
* `gdx-backend-lwjgl3:1.13.1` - Desktop backend
* `gdx-platform:1.13.1:natives-desktop` - Platform bindings
* `gdx-freetype:1.13.1` - FreeType font extension
* `gdx-freetype-platform:1.13.1:natives-desktop` - FreeType native libraries

### LibGDX Resources

* [Official LibGDX Wiki](https://github.com/libgdx/libgdx/wiki)
* [LibGDX API Documentation](https://libgdx.badlogicgames.com/ci/nightlies/docs/api/)
* [LibGDX Examples](https://github.com/libgdx/libgdx/wiki/A-simple-game)
* [Community Forum](https://discord.gg/libgdx)