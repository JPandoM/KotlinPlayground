import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Rectangle
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Represents a bouncing ball that can be used in different game contexts
 */
class Ball(
    private val screenWidth: Float,
    private val screenHeight: Float,
    initialRadius: Float = 20f,
    private val ballColor: Color = Color.WHITE,
    initialVelocityX: Float = 200f,
    initialVelocityY: Float = 200f,
    private val horizontalBoundsEnabled: Boolean = true
) {
    // Constants
    companion object {
        private const val BOUNCE_VELOCITY_FACTOR = 0.8f
        private const val POSITION_CORRECTION_OFFSET = 1f
        private const val MAX_BOUNCE_ANGLE_DEGREES = 75f
    }
    
    // Ball properties
    var radius = initialRadius
        private set
    var posX = screenWidth / 2
        private set
    var posY = screenHeight / 2
        private set
    var velocityX = initialVelocityX
        private set
    var velocityY = initialVelocityY
        private set

    // Collision bounds
    val bounds = Circle(posX, posY, radius)
    
    /**
     * Updates the ball's position and handles bouncing against the screen edges
     */
    fun update(delta: Float) {
        updatePosition(delta)
        updateBounds()
        checkHorizontalBounds()
        checkVerticalBounds()
    }
    
    /**
     * Updates the ball's position based on its velocity
     */
    private fun updatePosition(delta: Float) {
        posX += velocityX * delta
        posY += velocityY * delta
    }
    
    /**
     * Updates the collision bounds to match the current position
     */
    private fun updateBounds() {
        bounds.set(posX, posY, radius)
    }
    
    /**
     * Checks and handles horizontal bounds collisions
     */
    private fun checkHorizontalBounds() {
        if (!horizontalBoundsEnabled) return
        
        if (posX - radius < 0) {
            handleLeftBoundCollision()
        } else if (posX + radius > screenWidth) {
            handleRightBoundCollision()
        }
    }
    
    /**
     * Handles collision with the left bound
     */
    private fun handleLeftBoundCollision() {
        posX = radius // Correct position to prevent sticking
        velocityX = abs(velocityX) // Bounce by reversing velocity
    }
    
    /**
     * Handles collision with the right bound
     */
    private fun handleRightBoundCollision() {
        posX = screenWidth - radius
        velocityX = -abs(velocityX)
    }
    
    /**
     * Checks and handles vertical bounds collisions
     */
    private fun checkVerticalBounds() {
        if (posY - radius < 0) {
            handleBottomBoundCollision()
        } else if (posY + radius > screenHeight) {
            handleTopBoundCollision()
        }
    }
    
    /**
     * Handles collision with the bottom bound
     */
    private fun handleBottomBoundCollision() {
        posY = radius
        velocityY = abs(velocityY)
    }
    
    /**
     * Handles collision with the top bound
     */
    private fun handleTopBoundCollision() {
        posY = screenHeight - radius
        velocityY = -abs(velocityY)
    }
    
    /**
     * Renders the ball using the shape renderer
     */
    fun render(shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = ballColor
        shapeRenderer.circle(posX, posY, radius)
        shapeRenderer.end()
    }
    
    /**
     * Resets the ball to the center of the screen
     */
    fun reset() {
        posX = screenWidth / 2
        posY = screenHeight / 2
        updateBounds()
    }
    
    /**
     * Sets the ball's velocity
     */
    fun setVelocity(x: Float, y: Float) {
        velocityX = x
        velocityY = y
    }
    
    /**
     * Gets the X position of the ball
     */
    fun getX(): Float = posX

    /**
     * Checks if the ball collides with a rectangle (used for paddle collision)
     */
    fun collidesWithRectangle(rect: Rectangle): Boolean {
        // Find the closest point on the rectangle to the center of the circle
        val closestX = max(rect.x, min(posX, rect.x + rect.width))
        val closestY = max(rect.y, min(posY, rect.y + rect.height))
        
        // Calculate the squared distance
        return calculateSquaredDistance(closestX, closestY) < (radius * radius)
    }
    
    /**
     * Calculates the squared distance between the ball center and a point
     */
    private fun calculateSquaredDistance(pointX: Float, pointY: Float): Float {
        val distX = posX - pointX
        val distY = posY - pointY
        return distX * distX + distY * distY
    }
    
    /**
     * Handles the ball bouncing off a paddle
     */
    fun bounceOffPaddle(isLeftPaddle: Boolean, paddleBounds: Rectangle) {
        // Calculate the bounce angle based on where the ball hits the paddle
        val bounceAngle = calculateBounceAngle(paddleBounds)
        
        // Apply the velocity changes
        applyBounceVelocity(isLeftPaddle, bounceAngle)
        
        // Position correction to prevent sticking
        correctPositionAfterPaddleCollision(isLeftPaddle, paddleBounds)
        
        // Update the bounds
        updateBounds()
    }
    
    /**
     * Calculates the bounce angle based on where the ball hits the paddle
     */
    private fun calculateBounceAngle(paddleBounds: Rectangle): Float {
        val relativeIntersectY = (paddleBounds.y + (paddleBounds.height / 2)) - posY
        return (relativeIntersectY / (paddleBounds.height / 2)) * MAX_BOUNCE_ANGLE_DEGREES
    }
    
    /**
     * Applies velocity changes after a paddle collision
     * 
     * @param isLeftPaddle Whether the ball hit the left paddle (true) or right paddle (false)
     * @param bounceAngle The angle at which the ball should bounce, determined by impact position
     */
    private fun applyBounceVelocity(isLeftPaddle: Boolean, bounceAngle: Float) {
        // Calculate current speed
        val speed = sqrt((velocityX * velocityX + velocityY * velocityY).toDouble()).toFloat()
        
        // Direction depends on which paddle was hit
        val direction = if (isLeftPaddle) 1f else -1f
        
        // Apply new velocity components
        velocityX = direction * speed * BOUNCE_VELOCITY_FACTOR
        velocityY = -bounceAngle * speed * BOUNCE_VELOCITY_FACTOR
        
        // Ensure minimum vertical velocity for gameplay
        ensureMinimumVerticalVelocity()
    }
    
    /**
     * Ensures the ball has a minimum vertical velocity for better gameplay
     */
    private fun ensureMinimumVerticalVelocity() {
        val minVelocity = 100f
        if (abs(velocityY) < minVelocity) {
            velocityY = if (velocityY < 0) -minVelocity else minVelocity
        }
    }
    
    /**
     * Corrects the ball's position after a paddle collision to prevent sticking
     */
    private fun correctPositionAfterPaddleCollision(isLeftPaddle: Boolean, paddleBounds: Rectangle) {
        posX = if (isLeftPaddle) {
            paddleBounds.x + paddleBounds.width + radius + POSITION_CORRECTION_OFFSET
        } else {
            paddleBounds.x - radius - POSITION_CORRECTION_OFFSET
        }
    }
}
