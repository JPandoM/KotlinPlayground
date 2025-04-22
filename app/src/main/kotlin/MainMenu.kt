import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import kotlin.math.sin
import kotlin.random.Random

/**
 * Main menu screen for the Pong game
 */
class MainMenu(private val game: PongGame) : Screen {

    // Constants
    companion object {
        private const val SCREEN_WIDTH = 800f
        private const val SCREEN_HEIGHT = 600f
        private const val MENU_START_Y = 290f
        private const val MENU_ITEM_SPACING = 50f
        private const val TITLE_FONT_SIZE = 60
        private const val MENU_FONT_SIZE = 30
        private const val INSTRUCTION_FONT_SIZE = 15
        private const val TITLE_BASE_Y = SCREEN_HEIGHT - 170
        private const val PARTICLE_EMISSION_RATE = 0.02f
        private const val INSTRUCTION_Y = 50f
        private const val LETTER_SPACING = 40f
        
        // Animation constants
        private const val TITLE_AMPLITUDE = 20f
        private const val TITLE_FREQUENCY = 2f * Math.PI.toFloat() / 3.0f
        private const val MENU_AMPLITUDE = 8f
        private const val MENU_FREQUENCY = 2f * Math.PI.toFloat() / 3.0f
        
        // Menu item selection constants
        private const val PLAY_OPTION = 0
        private const val SETTINGS_OPTION = 1
        private const val EXIT_OPTION = 2
    }

    // UI components
    private val camera = OrthographicCamera()
    private val batch = SpriteBatch()
    private val glyphLayout = GlyphLayout()
    private val ball = Ball(SCREEN_WIDTH, SCREEN_HEIGHT, ballColor = Color.WHITE)
    private val shapeRenderer = ShapeRenderer()

    // Fonts
    private lateinit var font: BitmapFont
    private lateinit var titleFont: BitmapFont
    private lateinit var instructionFont: BitmapFont

    // Menu options
    private val menuItems = listOf("Play Game", "Settings", "Exit")
    private var selectedItem = 0

    // Touch/mouse position
    private val touchPoint = Vector3()

    // Background music
    private lateinit var menuMusic: com.badlogic.gdx.audio.Music

    // Animation timing
    private var totalTime: Float = 0f

    // Particle system for letter effects
    private val particleSystem = ParticleSystem(emissionRate = PARTICLE_EMISSION_RATE)
    private val random = Random.Default

    // Title details
    /**
     * Array of letters that make up the title "PONG"
     * Each letter is animated independently and has particle effects
     */
    private val titleLetters = arrayOf("P", "O", "N", "G")


    init {
        // Set up the camera to match our screen dimensions
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT)

        // Initialize fonts
        initializeFonts()
    }

    /**
     * Initializes all the fonts used in the menu
     */
    private fun initializeFonts() {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"))

        // Title font - large size for the title
        titleFont = generator.generateFont(FreeTypeFontParameter().apply {
            size = TITLE_FONT_SIZE
            color = Color.WHITE
            borderWidth = 2f
            borderColor = Color.WHITE
            shadowOffsetX = 3
            shadowOffsetY = 3
            shadowColor = Color(0.3f, 0.3f, 0.3f, 0.5f)
            borderStraight = true
            spaceX = 300 // Add spacing between characters
        })

        // Menu font - medium size for menu items
        font = generator.generateFont(FreeTypeFontParameter().apply {
            size = MENU_FONT_SIZE
            color = Color.WHITE
        })

        // Instruction font - small size for instructions
        instructionFont = generator.generateFont(FreeTypeFontParameter().apply {
            size = INSTRUCTION_FONT_SIZE
            color = Color.LIGHT_GRAY
        })

        // Dispose of the generator after creating fonts
        generator.dispose()
    }

    override fun show() {
        loadAndPlayBackgroundMusic()
    }

    /**
     * Loads and starts playing the background music
     */
    private fun loadAndPlayBackgroundMusic() {
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/menu_theme.wav"))
        menuMusic.isLooping = true
        menuMusic.volume = 0.7f
        menuMusic.play()
    }

    override fun render(delta: Float) {
        updateState(delta)
        clearScreen()
        setupRendering()
        renderGameElements(delta)
        handleInput()
        renderMenuUI()
    }

    /**
     * Updates animation time and particle system
     */
    private fun updateState(delta: Float) {
        totalTime += delta
        particleSystem.update(delta)
    }

    /**
     * Clears the screen with a black background
     */
    private fun clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    /**
     * Sets up the camera and projection matrices
     */
    private fun setupRendering() {
        camera.update()
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined
    }

    /**
     * Renders the ball and particle effects
     */
    private fun renderGameElements(delta: Float) {
        // Update and render the bouncing ball
        ball.update(delta)
        ball.render(shapeRenderer)

        // Render particles
        particleSystem.render(shapeRenderer)
    }
    
    /**
     * Renders the menu title, options and instructions
     */
    private fun renderMenuUI() {
        batch.begin()
        renderAnimatedTitle()
        renderMenuItems()
        renderInstructions()
        batch.end()
    }
    
    /**
     * Renders the animated "PONG" title with particles
     */
    private fun renderAnimatedTitle() {
        // Calculate the widths of each letter for proper centering
        val letterWidths = calculateLetterWidths()
        val totalTitleWidth = calculateTotalTitleWidth(letterWidths)
        
        // Calculate the starting X position to center the entire title
        var currentX = (SCREEN_WIDTH - totalTitleWidth) / 2
        
        // Draw each letter with its own animation
        for (i in titleLetters.indices) {
            currentX = drawAnimatedLetter(i, currentX, letterWidths[i])
        }
        
        // Reset emission timer if needed
        if (particleSystem.canEmit()) {
            particleSystem.resetEmissionTimer()
        }
    }
    
    /**
     * Calculates the width of each letter in the title
     */
    private fun calculateLetterWidths(): FloatArray {
        val letterWidths = FloatArray(titleLetters.size)
        for (i in titleLetters.indices) {
            glyphLayout.setText(titleFont, titleLetters[i])
            letterWidths[i] = glyphLayout.width
        }
        return letterWidths
    }
    
    /**
     * Calculates the total width of the title including letter spacing
     */
    private fun calculateTotalTitleWidth(letterWidths: FloatArray): Float {
        // Sum of all letter widths
        val lettersWidth = letterWidths.sum()
        
        // Add spacing between letters to the total width
        val spacingWidth = LETTER_SPACING * (titleLetters.size - 1)
        return lettersWidth + spacingWidth
    }
    
    /**
     * Draws a single animated letter of the title and handles particle emission
     * @return The X position after this letter (for the next letter)
     */
    private fun drawAnimatedLetter(index: Int, xPosition: Float, letterWidth: Float): Float {
        // Calculate animation offset
        val phaseOffset = index * 0.5f // Half a radian offset per letter
        val letterOffset = TITLE_AMPLITUDE * sin(totalTime * TITLE_FREQUENCY + phaseOffset)
        
        // Draw the letter with animation
        titleFont.draw(batch, titleLetters[index], xPosition, TITLE_BASE_Y + letterOffset)
        
        // Emit particles from the top edge of the letter
        emitParticlesForLetter(xPosition, letterWidth, letterOffset)
        
        // Return position for the next letter
        return xPosition + letterWidth + LETTER_SPACING
    }
    
    /**
     * Emits particles from the top of a letter
     */
    private fun emitParticlesForLetter(xPosition: Float, letterWidth: Float, letterOffset: Float) {
        if (particleSystem.canEmit()) {
            // Simplified letter detection - just use the index parameter from drawAnimatedLetter
            val letterIndex = titleLetters.indices.find { i -> 
                val letterPos = (SCREEN_WIDTH - calculateTotalTitleWidth(calculateLetterWidths())) / 2 +
                    calculateLetterWidths().take(i).sum() + (i * LETTER_SPACING)
                xPosition >= letterPos - 5 && xPosition <= letterPos + letterWidth + 5
            } ?: 0
            
            // Set appropriate particle counts for each letter
            val particleCount = when (letterIndex) {
                1 -> random.nextInt(1, 2)  // 'O' gets 1 particle
                2 -> random.nextInt(1, 2)  // 'N' gets 1 particle
                else -> random.nextInt(3, 6) // Normal amount for P and G
            }

            // Emit particles across the top of the letter
            repeat(particleCount) {
                // Randomize position across the letter width (but not at the very edges)
                val offsetX = letterWidth * (0.1f + random.nextFloat() * 0.8f) // 10-90% of width
                val emissionX = xPosition + offsetX
                
                // Position exactly at the top of the letter
                val emissionY = TITLE_BASE_Y + letterOffset
                
                // Emit a single particle at this position
                particleSystem.emit(emissionX, emissionY, 1)
            }
        }
    }
    
    /**
     * Renders the menu items with animation and selection highlighting
     */
    private fun renderMenuItems() {
        for (i in menuItems.indices) {
            // Set color based on selection
            font.color = if (i == selectedItem) Color.YELLOW else Color.WHITE
            
            // Calculate position
            glyphLayout.setText(font, menuItems[i])
            val x = (SCREEN_WIDTH - glyphLayout.width) / 2
            
            // Add floating effect with phase offset
            val phaseOffset = i * 0.5f 
            val menuFloatOffset = MENU_AMPLITUDE * sin(totalTime * MENU_FREQUENCY + phaseOffset)
            
            // Calculate final Y position
            val y = MENU_START_Y - (i * MENU_ITEM_SPACING) + menuFloatOffset
            
            // Draw menu item
            font.draw(batch, menuItems[i], x, y)
        }
    }
    
    /**
     * Renders the instructions text
     */
    private fun renderInstructions() {
        val instructionsText = "Use UP/DOWN arrows to navigate, ENTER to select"
        glyphLayout.setText(instructionFont, instructionsText)
        instructionFont.draw(batch, instructionsText,
            (SCREEN_WIDTH - glyphLayout.width) / 2,
            INSTRUCTION_Y)
    }

    override fun resize(width: Int, height: Int) {
        // Handle screen resize if needed
    }

    override fun pause() {
        // Called when game is paused
    }

    override fun resume() {
        // Called when game is resumed
    }

    override fun hide() {
        // Called when this screen is no longer the current screen
        menuMusic.stop()
    }

    override fun dispose() {
        disposeResources()
    }
    
    /**
     * Cleans up all resources used by the menu
     */
    private fun disposeResources() {
        batch.dispose()
        font.dispose()
        titleFont.dispose()
        instructionFont.dispose()
        shapeRenderer.dispose()
        menuMusic.dispose()
        particleSystem.clear()
    }
    
    /**
     * Handles all user input for menu navigation
     */
    private fun handleInput() {
        handleKeyboardInput()
        handleTouchInput()
    }
    
    /**
     * Handles keyboard navigation and selection
     */
    private fun handleKeyboardInput() {
        // Up/Down for navigation
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            moveSelectionUp()
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            moveSelectionDown()
        }
        
        // Enter for selection
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            selectMenuItem(selectedItem)
        }
    }
    
    /**
     * Moves selection up with wrapping
     */
    private fun moveSelectionUp() {
        selectedItem = (selectedItem - 1 + menuItems.size) % menuItems.size
    }
    
    /**
     * Moves selection down with wrapping
     */
    private fun moveSelectionDown() {
        selectedItem = (selectedItem + 1) % menuItems.size
    }
    
    /**
     * Handles touch/mouse input for menu navigation
     */
    private fun handleTouchInput() {
        if (Gdx.input.justTouched()) {
            // Convert touch coordinates to camera coordinates
            touchPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPoint)
            
            checkMenuItemsTouched()
        }
    }
    
    /**
     * Checks if any menu item was touched
     */
    private fun checkMenuItemsTouched() {
        for (i in menuItems.indices) {
            if (isMenuItemTouched(i)) {
                selectMenuItem(i)
                break
            }
        }
    }
    
    /**
     * Determines if a specific menu item was touched
     */
    private fun isMenuItemTouched(index: Int): Boolean {
        // Calculate position and bounds
        glyphLayout.setText(font, menuItems[index])
        val x = (SCREEN_WIDTH - glyphLayout.width) / 2
        val y = MENU_START_Y - (index * MENU_ITEM_SPACING)
        
        val rect = com.badlogic.gdx.math.Rectangle(
            x, y - glyphLayout.height,
            glyphLayout.width, glyphLayout.height
        )
        
        return rect.contains(touchPoint.x, touchPoint.y)
    }
    
    /**
     * Handles menu item selection and performs the appropriate action
     */
    private fun selectMenuItem(index: Int) {
        when (menuItems[index]) {
            "Play Game" -> startGame()
            "Settings" -> openSettings()
            "Exit" -> exitGame()
        }
    }
    
    /**
     * Starts the game by switching to the game screen
     */
    private fun startGame() {
        game.setScreen(PongLevel(game))
    }
    
    /**
     * Opens the settings screen (currently just logs a message)
     */
    private fun openSettings() {
        println("Opening settings...")
        // Eventually: game.setScreen(SettingsScreen(game))
    }
    
    /**
     * Exits the application
     */
    private fun exitGame() {
        Gdx.app.exit()
    }
}