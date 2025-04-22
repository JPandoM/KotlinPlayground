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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * Main menu screen for the Pong game
 */
class MainMenu(private val game: PongGame) : Screen {

    // Screen dimensions
    private val screenWidth = 800f
    private val screenHeight = 600f

    // UI components
    private val camera: OrthographicCamera = OrthographicCamera()
    private val batch: SpriteBatch = SpriteBatch()
    private var font: BitmapFont
    private var titleFont: BitmapFont
    private var instructionFont: BitmapFont
    private val glyphLayout = GlyphLayout()

    // Menu options
    private val menuItems = listOf("Play Game", "Settings", "Exit")
    private var selectedItem = 0

    // Menu positioning
    private val menuStartY = 300f
    private val menuItemSpacing = 50f

    // Touch/mouse position
    private val touchPoint = Vector3()
    
    // Shape renderer for drawing the ball
    private val shapeRenderer = ShapeRenderer()
    
    // Bouncing ball
    private val ball = Ball(screenWidth, screenHeight)

    init {
        // Set up the camera to match our screen dimensions
        camera.setToOrtho(false, screenWidth, screenHeight)
        
        // Configure fonts using FreeType for high-quality rendering
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"))
        val parameter = FreeTypeFontParameter()
        
        // Title font - large size for the title
        parameter.size = 45
        parameter.color = Color.WHITE
        titleFont = generator.generateFont(parameter)
        
        // Menu font - medium size for menu items
        parameter.size = 30
        font = generator.generateFont(parameter)
        
        // Instruction font - small size for instructions
        parameter.size = 15
        parameter.color = Color.LIGHT_GRAY
        instructionFont = generator.generateFont(parameter)
        
        // Dispose of the generator after creating fonts
        generator.dispose()
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        // Clear the screen with a dark blue color
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        
        // Update camera
        camera.update()
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined
        
        // Update and render the bouncing ball (before drawing menu)
        ball.update(delta)
        ball.render(shapeRenderer)
        
        // Handle input
        handleInput()
        
        // Draw menu
        batch.begin()
        
        // Draw title
        glyphLayout.setText(titleFont, "PONG")
        titleFont.draw(batch, "PONG",
            (screenWidth - glyphLayout.width) / 2,
            screenHeight - 100)
        
        // Draw menu items
        for (i in menuItems.indices) {
            // Set color based on selection
            if (i == selectedItem) {
                font.color = Color.YELLOW
            } else {
                font.color = Color.WHITE
            }
            
            // Calculate position
            glyphLayout.setText(font, menuItems[i])
            val x = (screenWidth - glyphLayout.width) / 2
            val y = menuStartY - (i * menuItemSpacing)
            
            // Draw menu item
            font.draw(batch, menuItems[i], x, y)
        }
        
        // Draw instructions with a dedicated instruction font
        glyphLayout.setText(instructionFont, "Use UP/DOWN arrows to navigate, ENTER to select")
        instructionFont.draw(batch, "Use UP/DOWN arrows to navigate, ENTER to select",
            (screenWidth - glyphLayout.width) / 2,
            50f)
        
        batch.end()
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
    }

    override fun dispose() {
        // Cleanup resources (batch, fonts, shapeRenderer)
        batch.dispose()
        font.dispose()
        titleFont.dispose()
        instructionFont.dispose()
        shapeRenderer.dispose()
    }

    private fun handleInput() {
        // Keyboard controls for menu navigation
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            // Move selection up, wrapping around if at the top
            selectedItem = (selectedItem - 1 + menuItems.size) % menuItems.size
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            // Move selection down, wrapping around if at the bottom
            selectedItem = (selectedItem + 1) % menuItems.size
        }
        
        // Handle selection with an Enter key
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            selectMenuItem(selectedItem)
        }
        
        // Mouse/touch controls
        if (Gdx.input.justTouched()) {
            // Convert touch coordinates to camera coordinates
            touchPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPoint)
            
            // Check each menu item
            for (i in menuItems.indices) {
                // Calculate position and bounds
                glyphLayout.setText(font, menuItems[i])
                val x = (screenWidth - glyphLayout.width) / 2
                val y = menuStartY - (i * menuItemSpacing)
                val rect = com.badlogic.gdx.math.Rectangle(
                    x, y - glyphLayout.height,
                    glyphLayout.width, glyphLayout.height
                )
                
                // If touched, select this item
                if (rect.contains(touchPoint.x, touchPoint.y)) {
                    selectMenuItem(i)
                    break
                }
            }
        }
    }

    private fun selectMenuItem(index: Int) {
        when (menuItems[index]) {
            "Play Game" -> {
                // Launch the Pong game level
                game.setScreen(PongLevel(game))
            }
            "Settings" -> {
                // This would load the settings screen
                println("Opening settings...")
                // Eventually: game.setScreen(SettingsScreen(game))
            }
            "Exit" -> {
                // Exit the game
                Gdx.app.exit()
            }
        }
    }
}