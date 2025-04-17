import com.badlogic.gdx.Game

/**
 * Main game class that serves as the entry point and screen manager
 */
class PongGame : Game() {
    /**
     * Called when the game is first created
     */
    override fun create() {
        initializeMenu()
    }

    fun initializeMenu() {
        setScreen(MainMenu(this))
    }

    /**
     * Clean up resources when the game is closed
     */
    override fun dispose() {
        // The screen's dispose method will be called automatically
        super.dispose()
    }
}