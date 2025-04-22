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

}