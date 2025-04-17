plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")

    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

dependencies {
    // LibGDX core library
    implementation("com.badlogicgames.gdx:gdx:1.13.1")
    // Desktop (LWJGL3) backend for LibGDX
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.13.1")
    // Native platform bindings for desktop
    implementation("com.badlogicgames.gdx:gdx-platform:1.13.1:natives-desktop")
    // FreeType extension for high-quality fonts
    implementation("com.badlogicgames.gdx:gdx-freetype:1.13.1")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:1.13.1:natives-desktop")
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `App.kt` to a class with FQN `com.example.app.AppKt`.)
    mainClass = "org.example.app.AppKt"
}