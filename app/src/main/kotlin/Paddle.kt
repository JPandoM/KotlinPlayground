import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle

/**
 * Represents a paddle in the pong game
 */
class Paddle(
    private val screenWidth: Float,
    private val screenHeight: Float,
    private val isLeftPaddle: Boolean, // true for left paddle, false for right paddle
    private val paddleColor: Color = Color.WHITE
) {
    // Paddle dimensions
    private val WIDTH = 20f
    private val HEIGHT = 100f
    private val PADDING = 40f // Distance from edge of screen
    
    // Paddle movement speed
    private val SPEED = 500f
    
    // Paddle position and collision rectangle
    private var posX: Float = if (isLeftPaddle) PADDING else screenWidth - PADDING - WIDTH
    private var posY: Float = (screenHeight - HEIGHT) / 2
    val bounds = Rectangle(posX, posY, WIDTH, HEIGHT)
    
    /**
     * Updates the paddle position based on input
     */
    fun update(delta: Float) {
        if (isLeftPaddle) {
            // Left paddle uses W/S keys
            if (Gdx.input.isKeyPressed(Input.Keys.W) && posY + HEIGHT < screenHeight) {
                posY += SPEED * delta
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) && posY > 0) {
                posY -= SPEED * delta
            }
        } else {
            // Right paddle uses UP/DOWN keys
            if (Gdx.input.isKeyPressed(Input.Keys.UP) && posY + HEIGHT < screenHeight) {
                posY += SPEED * delta
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && posY > 0) {
                posY -= SPEED * delta
            }
        }
        
        // Keep paddle within screen bounds
        if (posY < 0) posY = 0f
        if (posY + HEIGHT > screenHeight) posY = screenHeight - HEIGHT
        
        // Update collision rectangle
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
