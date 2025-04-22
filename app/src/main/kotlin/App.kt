import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

/**
 * Entry point for the Pong game application
 * Sets up the LibGDX application with appropriate configuration
 */
fun main() {
    val config = Lwjgl3ApplicationConfiguration().apply {
        setTitle("Kotlin Pong")
        setWindowedMode(800, 600)
        setForegroundFPS(60)
        useVsync(true)
        setResizable(false)
    }

    Lwjgl3Application(PongGame(), config)
}
