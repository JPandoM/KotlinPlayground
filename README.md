# KotlinPlayground with LibGDX

This project uses [Gradle](https://gradle.org/) and integrates [LibGDX](https://libgdx.com/) - a cross-platform game development framework.

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

This project consists of the `app` module with game code and the shared build logic in `buildSrc`.

The shared build logic was extracted to a convention plugin located in `buildSrc`.

This project uses a version catalog (see `gradle/libs.versions.toml`) to declare and version dependencies
and both a build cache and a configuration cache (see `gradle.properties`).

## LibGDX Integration

The app module integrates LibGDX 1.13.1, which provides:

* Cross-platform game development capabilities
* Hardware-accelerated graphics rendering
* Audio and input handling
* Physics and collision detection

### LibGDX Resources

* [Official LibGDX Wiki](https://github.com/libgdx/libgdx/wiki)
* [LibGDX API Documentation](https://libgdx.badlogicgames.com/ci/nightlies/docs/api/)
* [LibGDX Examples](https://github.com/libgdx/libgdx/wiki/A-simple-game)
* [Community Forum](https://discord.gg/libgdx)