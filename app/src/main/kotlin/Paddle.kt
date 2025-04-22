import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle

/**
 * Represents a paddle in the pong game
 */
class Paddle(
    screenWidth: Float,
    private val screenHeight: Float,
    private val isLeftPaddle: Boolean, // true for left paddle, false for right paddle
    private val paddleColor: Color = Color.WHITE
) {
    // Constants
    companion object {
        private const val WIDTH = 20f
        private const val HEIGHT = 100f
        private const val PADDING = 40f // Distance from edge of screen
        private const val SPEED = 500f
    }
    
    // Paddle position and collision rectangle
    private var posX: Float = calculateInitialX(screenWidth)
    private var posY: Float = calculateInitialY()
    val bounds = Rectangle(posX, posY, WIDTH, HEIGHT)
    
    /**
     * Calculates the initial X position based on whether it's a left or right paddle
     */
    private fun calculateInitialX(screenWidth: Float): Float {
        return if (isLeftPaddle) {
            PADDING
        } else {
            screenWidth - PADDING - WIDTH
        }
    }
    
    /**
     * Calculates the initial Y position, centering the paddle vertically
     */
    private fun calculateInitialY(): Float {
        return (screenHeight - HEIGHT) / 2
    }
    
    /**
     * Updates the paddle position based on input
     */
    fun update(delta: Float) {
        handleInput(delta)
        constrainToScreenBounds()
        updateCollisionBounds()
    }
    
    /**
     * Processes keyboard input to move the paddle
     */
    private fun handleInput(delta: Float) {
        if (isLeftPaddle) {
            handleLeftPaddleInput(delta)
        } else {
            handleRightPaddleInput(delta)
        }
    }
    
    /**
     * Handles input for the left paddle (W/S keys)
     */
    private fun handleLeftPaddleInput(delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.W) && canMoveUp()) {
            moveUp(delta)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && canMoveDown()) {
            moveDown(delta)
        }
    }
    
    /**
     * Handles input for the right paddle (UP/DOWN keys)
     */
    private fun handleRightPaddleInput(delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && canMoveUp()) {
            moveUp(delta)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && canMoveDown()) {
            moveDown(delta)
        }
    }
    
    /**
     * Checks if the paddle can move upward
     */
    private fun canMoveUp(): Boolean {
        return posY + HEIGHT < screenHeight
    }
    
    /**
     * Checks if the paddle can move downward
     */
    private fun canMoveDown(): Boolean {
        return posY > 0
    }
    
    /**
     * Moves the paddle up
     */
    private fun moveUp(delta: Float) {
        posY += SPEED * delta
    }
    
    /**
     * Moves the paddle down
     */
    private fun moveDown(delta: Float) {
        posY -= SPEED * delta
    }
    
    /**
     * Ensures the paddle stays within the screen boundaries
     */
    private fun constrainToScreenBounds() {
        if (posY < 0) {
            posY = 0f
        }
        if (posY + HEIGHT > screenHeight) {
            posY = screenHeight - HEIGHT
        }
    }
    
    /**
     * Updates the collision rectangle to match current position
     */
    private fun updateCollisionBounds() {
        bounds.y = posY
    }
    
    /**
     * Renders the paddle
     */
    fun render(shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = paddleColor
        shapeRenderer.rect(posX, posY, WIDTH, HEIGHT)
        shapeRenderer.end()
    }
}
