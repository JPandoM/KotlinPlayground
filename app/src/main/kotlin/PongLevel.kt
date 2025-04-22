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
import com.badlogic.gdx.math.MathUtils
import kotlin.math.abs

/**
 * The main game level for the Pong game
 */
class PongLevel(private val game: PongGame) : Screen {
    // Constants
    companion object {
        private const val SCREEN_WIDTH = 800f
        private const val SCREEN_HEIGHT = 600f
        
        private const val SCORE_FONT_SIZE = 36
        private const val INSTRUCTION_FONT_SIZE = 16
        
        private const val SCORE_Y_POSITION = SCREEN_HEIGHT - 50
        private const val SCORE_X_OFFSET = 50f
        private const val INSTRUCTION_Y_POSITION = 30f
        
        private const val BALL_RADIUS = 10f
        private const val BALL_INITIAL_SPEED = 400f
        
        // Angle constants for ball direction
        private const val MIN_ANGLE = MathUtils.PI / 6
        private const val MAX_ANGLE = MathUtils.PI / 3
    }

    // Game objects
    private val leftPaddle: Paddle
    private val rightPaddle: Paddle
    private val ball: Ball
    
    // Rendering tools
    private val camera: OrthographicCamera = OrthographicCamera()
    private val batch: SpriteBatch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    
    // Scoring
    private var leftScore = 0
    private var rightScore = 0
    private lateinit var scoreFont: BitmapFont
    private lateinit var instructionFont: BitmapFont
    private val glyphLayout = GlyphLayout()
    
    // Game state
    private var isPaused = false
    
    init {
        setupCamera()
        createGameObjects()
        initializeFonts()
    }
    
    /**
     * Sets up the orthographic camera
     */
    private fun setupCamera() {
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT)
    }
    
    /**
     * Creates paddles and ball game objects
     */
    private fun createGameObjects() {
        // Create paddles
        leftPaddle = Paddle(SCREEN_WIDTH, SCREEN_HEIGHT, true)
        rightPaddle = Paddle(SCREEN_WIDTH, SCREEN_HEIGHT, false)
        
        // Create the ball with a random direction
        ball = Ball(
            SCREEN_WIDTH, 
            SCREEN_HEIGHT,
            initialRadius = BALL_RADIUS,
            initialColor = Color.WHITE,
            initialVelocityX = 0f,  // Will be set in resetBall()
            initialVelocityY = 0f,  // Will be set in resetBall()
            colorChangeOnBounce = false,
            horizontalBoundsEnabled = false  // We'll handle horizontal boundaries ourselves for scoring
        )
        resetBall()
    }
    
    /**
     * Initializes fonts for score and instructions
     */
    private fun initializeFonts() {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"))
        
        // Score font (larger)
        scoreFont = generator.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply {
            size = SCORE_FONT_SIZE
            color = Color.WHITE
        })
        
        // Instruction font (smaller)
        instructionFont = generator.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply {
            size = INSTRUCTION_FONT_SIZE
            color = Color.LIGHT_GRAY
        })
        
        generator.dispose()
    }
    
    /**
     * Resets the ball to center with random direction
     */
    private fun resetBall() {
        // Position ball in the center
        ball.reset()
        
        // Give it a random direction, but ensure it's not too vertical
        val angle = MathUtils.random(MIN_ANGLE, MAX_ANGLE)
        val direction = if (MathUtils.randomBoolean()) angle else MathUtils.PI - angle
        
        ball.setVelocity(
            MathUtils.cos(direction) * BALL_INITIAL_SPEED,
            MathUtils.sin(direction) * BALL_INITIAL_SPEED
        )
    }
    
    override fun show() {
        // Called when this screen becomes the current screen
    }
    
    override fun render(delta: Float) {
        clearScreen()
        handleInput()
        updateGameState(delta)
        setupRenderingMatrices()
        renderGameObjects()
        renderUI()
    }
    
    /**
     * Clears the screen with black background
     */
    private fun clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }
    
    /**
     * Handles user input for pause and exit
     */
    private fun handleInput() {
        // Toggle pause with space or touch
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
            isPaused = !isPaused
        }
        
        // Exit to main menu with escape key
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            game.initializeMenu()
        }
    }
    
    /**
     * Updates all game objects if not paused
     */
    private fun updateGameState(delta: Float) {
        if (isPaused) return
        
        // Update game objects
        leftPaddle.update(delta)
        rightPaddle.update(delta)
        ball.update(delta)
        
        // Check for collisions and scoring
        checkPaddleCollision()
        checkScoring()
    }
    
    /**
     * Sets up rendering matrices
     */
    private fun setupRenderingMatrices() {
        camera.update()
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined
    }
    
    /**
     * Renders all game objects (paddles, ball, center line)
     */
    private fun renderGameObjects() {
        // Render paddles and ball
        leftPaddle.render(shapeRenderer)
        rightPaddle.render(shapeRenderer)
        ball.render(shapeRenderer)
        
        // Draw the center line
        renderCenterLine()
    }
    
    /**
     * Renders the center line divider
     */
    private fun renderCenterLine() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.WHITE
        shapeRenderer.line(SCREEN_WIDTH / 2, 0f, SCREEN_WIDTH / 2, SCREEN_HEIGHT)
        shapeRenderer.end()
    }
    
    /**
     * Renders score, pause text, and instructions
     */
    private fun renderUI() {
        batch.begin()
        renderScores()
        renderPauseTextIfNeeded()
        renderInstructions()
        batch.end()
    }
    
    /**
     * Renders player scores
     */
    private fun renderScores() {
        // Left score
        glyphLayout.setText(scoreFont, leftScore.toString())
        scoreFont.draw(batch, leftScore.toString(), 
            (SCREEN_WIDTH / 2) - glyphLayout.width - SCORE_X_OFFSET, SCORE_Y_POSITION)
        
        // Right score
        glyphLayout.setText(scoreFont, rightScore.toString())
        scoreFont.draw(batch, rightScore.toString(), 
            (SCREEN_WIDTH / 2) + SCORE_X_OFFSET, SCORE_Y_POSITION)
    }
    
    /**
     * Renders pause text when game is paused
     */
    private fun renderPauseTextIfNeeded() {
        if (isPaused) {
            val pauseText = "PAUSED - Tap or press SPACE to continue"
            glyphLayout.setText(scoreFont, pauseText)
            scoreFont.draw(batch, pauseText, 
                (SCREEN_WIDTH - glyphLayout.width) / 2, SCREEN_HEIGHT / 2)
        }
    }
    
    /**
     * Renders control instructions
     */
    private fun renderInstructions() {
        val instructionsText = "W/S - Left Paddle | UP/DOWN - Right Paddle | ESC - Main Menu"
        glyphLayout.setText(instructionFont, instructionsText)
        instructionFont.draw(batch, instructionsText, 
            (SCREEN_WIDTH - glyphLayout.width) / 2, INSTRUCTION_Y_POSITION)
    }
    
    /**
     * Checks for collision between ball and paddles
     */
    private fun checkPaddleCollision() {
        // Check for collision with left paddle
        if (ball.collidesWithRectangle(leftPaddle.bounds)) {
            ball.bounceOffPaddle(true, leftPaddle.bounds)
        }
        
        // Check for collision with right paddle
        if (ball.collidesWithRectangle(rightPaddle.bounds)) {
            ball.bounceOffPaddle(false, rightPaddle.bounds)
        }
    }
    
    /**
     * Checks if a player has scored
     */
    private fun checkScoring() {
        // Ball went past left edge - right scores
        if (ball.getX() < 0) {
            rightScore++
            resetBall()
        }
        
        // Ball went past right edge - left scores
        if (ball.getX() > SCREEN_WIDTH) {
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
        disposeResources()
    }
    
    /**
     * Disposes all resources used by this screen
     */
    private fun disposeResources() {
        batch.dispose()
        shapeRenderer.dispose()
        scoreFont.dispose()
        instructionFont.dispose()
    }
}
