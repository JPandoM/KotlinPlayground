import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import kotlin.math.abs

/**
 * Represents a bouncing ball that changes color when hitting borders
 */
class Ball(
    private val screenWidth: Float,
    private val screenHeight: Float
) {
    // Ball properties
    private val radius = 20f
    private var posX = screenWidth / 2
    private var posY = screenHeight / 2
    private var velocityX = 200f
    private var velocityY = 200f
    private var currentColor = Color.WHITE

    // Available colors for the ball to cycle through
    private val colors = arrayOf(
        Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, 
        Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE
    )
    
    // Random generator for colors
    private val random = java.util.Random()
    
    /**
     * Updates the ball's position and handles bouncing
     */
    fun update(delta: Float) {
        // Update position based on velocity
        posX += velocityX * delta
        posY += velocityY * delta
        
        // Check horizontal bounds and handle bouncing
        if (posX - radius < 0) {
            posX = radius // Correct position to prevent sticking
            velocityX = abs(velocityX) // Bounce by reversing velocity
            changeColor() // Change color on bounce
        } else if (posX + radius > screenWidth) {
            posX = screenWidth - radius
            velocityX = -abs(velocityX)
            changeColor()
        }
        
        // Check vertical bounds and handle bouncing
        if (posY - radius < 0) {
            posY = radius
            velocityY = abs(velocityY)
            changeColor()
        } else if (posY + radius > screenHeight) {
            posY = screenHeight - radius
            velocityY = -abs(velocityY)
            changeColor()
        }
    }
    
    /**
     * Changes the ball color to a random different color
     */
    private fun changeColor() {
        // Select a new random color different from current
        var newColorIndex: Int
        do {
            newColorIndex = random.nextInt(colors.size)
        } while (colors[newColorIndex] == currentColor)
        
        currentColor = colors[newColorIndex]
    }
    
    /**
     * Renders the ball using the shape renderer
     */
    fun render(shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = currentColor
        shapeRenderer.circle(posX, posY, radius)
        shapeRenderer.end()
    }
}
