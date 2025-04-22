import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import kotlin.math.abs

/**
 * The main game level for the Pong game
 */
class PongLevel(private val game: PongGame) : Screen {
    // Screen dimensions
    private val screenWidth = 800f
    private val screenHeight = 600f

    // Game objects
    private val leftPaddle: Paddle
    private val rightPaddle: Paddle
    private val ball: PongBall
    
    // Rendering tools
    private val camera: OrthographicCamera = OrthographicCamera()
    private val batch: SpriteBatch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    
    // Scoring
    private var leftScore = 0
    private var rightScore = 0
    private var scoreFont: BitmapFont
    private val glyphLayout = GlyphLayout()
    
    // Game state
    private var isPaused = false
    
    init {
        // Set up the camera
        camera.setToOrtho(false, screenWidth, screenHeight)
        
        // Create paddles
        leftPaddle = Paddle(screenWidth, screenHeight, true)
        rightPaddle = Paddle(screenWidth, screenHeight, false)
        
        // Create the ball with a random direction
        ball = PongBall(screenWidth, screenHeight)
        resetBall()
        
        // Set up score font
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = 36
        parameter.color = Color.WHITE
        scoreFont = generator.generateFont(parameter)
        generator.dispose()
    }
    
    private fun resetBall() {
        // Position ball in the center
        ball.reset()
        
        // Give it a random direction, but ensure it's not too vertical
        val angle = MathUtils.random(MathUtils.PI / 6, MathUtils.PI / 3)
        val direction = if (MathUtils.randomBoolean()) angle else MathUtils.PI - angle
        ball.setVelocity(
            MathUtils.cos(direction) * 400f,
            MathUtils.sin(direction) * 400f
        )
    }
    
    override fun show() {
        // Called when this screen becomes the current screen
    }
    
    override fun render(delta: Float) {
        // Clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        
        // Handle input for pause
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
            isPaused = !isPaused
        }
        
        // Exit to main menu with escape key
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            game.initializeMenu()
            return
        }
        
        if (!isPaused) {
            // Update game objects
            leftPaddle.update(delta)
            rightPaddle.update(delta)
            
            // Update ball and check for collisions
            ball.update(delta)
            checkPaddleCollision()
            
            // Check for scoring
            checkScoring()
        }
        
        // Set the projection matrix for rendering
        camera.update()
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined
        
        // Render game objects
        leftPaddle.render(shapeRenderer)
        rightPaddle.render(shapeRenderer)
        ball.render(shapeRenderer)
        
        // Draw the center line
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.WHITE
        shapeRenderer.line(screenWidth / 2, 0f, screenWidth / 2, screenHeight)
        shapeRenderer.end()
        
        // Draw scores
        batch.begin()
        
        // Left score
        glyphLayout.setText(scoreFont, leftScore.toString())
        scoreFont.draw(batch, leftScore.toString(), 
            (screenWidth / 2) - glyphLayout.width - 50, screenHeight - 50)
        
        // Right score
        glyphLayout.setText(scoreFont, rightScore.toString())
        scoreFont.draw(batch, rightScore.toString(), 
            (screenWidth / 2) + 50, screenHeight - 50)
        
        // Pause text
        if (isPaused) {
            val pauseText = "PAUSED - Tap or press SPACE to continue"
            glyphLayout.setText(scoreFont, pauseText)
            scoreFont.draw(batch, pauseText, 
                (screenWidth - glyphLayout.width) / 2, screenHeight / 2)
        }
        
        // Instructions
        val instructionsText = "W/S - Left Paddle | UP/DOWN - Right Paddle | ESC - Main Menu"
        glyphLayout.setText(scoreFont, instructionsText)
        scoreFont.draw(batch, instructionsText, 
            (screenWidth - glyphLayout.width) / 2, 30f)
            
        batch.end()
    }
    
    private fun checkPaddleCollision() {
        // Check for collision with left paddle
        if (ball.collidesWithRectangle(leftPaddle.bounds)) {
            // Ball hit left paddle - calculate bounce based on where it hit
            ball.bounceOffPaddle(true, leftPaddle.bounds)
        }
        
        // Check for collision with right paddle
        if (ball.collidesWithRectangle(rightPaddle.bounds)) {
            // Ball hit right paddle - calculate bounce based on where it hit
            ball.bounceOffPaddle(false, rightPaddle.bounds)
        }
    }
    
    private fun checkScoring() {
        // Ball went past left paddle - right scores
        if (ball.getX() < 0) {
            rightScore++
            resetBall()
        }
        
        // Ball went past right paddle - left scores
        if (ball.getX() > screenWidth) {
            leftScore++
            resetBall()
        }
    }
    
    override fun resize(width: Int, height: Int) {
        // Handle screen resize
    }
    
    override fun pause() {
        isPaused = true
    }
    
    override fun resume() {
        // Keep paused until player explicitly unpauses
    }
    
    override fun hide() {
        // Called when this screen is no longer the current screen
    }
    
    override fun dispose() {
        // Dispose of all resources
        batch.dispose()
        shapeRenderer.dispose()
        scoreFont.dispose()
    }
}

/**
 * Ball class specifically for Pong (extends the existing Ball with Pong-specific functionality)
 */
class PongBall(
    private val screenWidth: Float,
    private val screenHeight: Float
) {
    // Ball properties
    private val radius = 10f
    private var posX = screenWidth / 2
    private var posY = screenHeight / 2
    private var velocityX = 300f
    private var velocityY = 200f
    private val color = Color.WHITE
    
    // Collision bounds
    val bounds = Circle(posX, posY, radius)
    
    /**
     * Updates the ball's position and handles top/bottom bouncing
     */
    fun update(delta: Float) {
        // Update position based on velocity
        posX += velocityX * delta
        posY += velocityY * delta
        
        // Check vertical bounds and handle bouncing
        if (posY - radius < 0) {
            posY = radius
            velocityY = abs(velocityY)
        } else if (posY + radius > screenHeight) {
            posY = screenHeight - radius
            velocityY = -abs(velocityY)
        }
        
        // Update collision circle
        bounds.setPosition(posX, posY)
    }
    
    /**
     * Renders the ball
     */
    fun render(shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = color
        shapeRenderer.circle(posX, posY, radius)
        shapeRenderer.end()
    }
    
    /**
     * Resets the ball to the center of the screen
     */
    fun reset() {
        posX = screenWidth / 2
        posY = screenHeight / 2
        bounds.setPosition(posX, posY)
    }
    
    /**
     * Sets the ball's velocity
     */
    fun setVelocity(x: Float, y: Float) {
        velocityX = x
        velocityY = y
    }
    
    /**
     * Handles the ball bouncing off a paddle
     */
    fun bounceOffPaddle(isLeftPaddle: Boolean, paddleBounds: Rectangle) {
        // Calculate relative position where ball hit the paddle (0 = middle, -1 = top, 1 = bottom)
        val relativeIntersectY = (paddleBounds.y + (paddleBounds.height / 2)) - posY
        val normalizedRelativeIntersectionY = (relativeIntersectY / (paddleBounds.height / 2))
        
        // Calculate bounce angle (between -60 and 60 degrees)
        val bounceAngle = normalizedRelativeIntersectionY * (Math.PI / 3) // Pi/3 = 60 degrees
        
        // Calculate new velocity based on bounce angle
        val speed = Math.sqrt((velocityX * velocityX + velocityY * velocityY).toDouble()).toFloat() * 1.05f
        val direction = if (isLeftPaddle) 0f else MathUtils.PI.toFloat()
        
        velocityX = speed * MathUtils.cos(direction + bounceAngle.toFloat())
        velocityY = -speed * MathUtils.sin(bounceAngle.toFloat())
        
        // Ensure X velocity has the right direction and minimum speed
        val minXSpeed = 200f
        if (isLeftPaddle) {
            velocityX = maxOf(minXSpeed, abs(velocityX))
        } else {
            velocityX = minOf(-minXSpeed, -abs(velocityX))
        }
        
        // Ensure ball doesn't get stuck in paddle
        if (isLeftPaddle) {
            posX = paddleBounds.x + paddleBounds.width + radius + 1
        } else {
            posX = paddleBounds.x - radius - 1
        }
        
        // Update the bounds position
        bounds.setPosition(posX, posY)
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
        val closestX = maxOf(rect.x, minOf(posX, rect.x + rect.width))
        val closestY = maxOf(rect.y, minOf(posY, rect.y + rect.height))
        
        // Calculate the distance between the circle's center and the closest point
        val distanceX = posX - closestX
        val distanceY = posY - closestY
        
        // If the distance is less than the circle's radius, they intersect
        return (distanceX * distanceX + distanceY * distanceY) < (radius * radius)
    }
    }
