import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import kotlin.random.Random

/**
 * Represents a single particle for visual effects
 */
class Particle(
    var position: Vector2,
    var velocity: Vector2,
    var color: Color,
    var size: Float,
    var life: Float,
    var maxLife: Float
) {
    // Constants
    companion object {
        private const val MIN_LIFE = 0f
    }
    
    /**
     * Updates the particle state
     * @return true if particle is still alive, false if it died
     */
    fun update(delta: Float): Boolean {
        updatePosition(delta)
        decreaseLife(delta)
        return isAlive()
    }
    
    /**
     * Moves the particle according to its velocity
     */
    private fun updatePosition(delta: Float) {
        position.add(velocity.x * delta, velocity.y * delta)
    }
    
    /**
     * Decreases the particle's remaining life
     */
    private fun decreaseLife(delta: Float) {
        life -= delta
    }
    
    /**
     * Checks if the particle is still alive
     */
    private fun isAlive(): Boolean {
        return life > MIN_LIFE
    }
    
    /**
     * Returns color with alpha adjusted for remaining life
     */
    fun getCurrentColor(): Color {
        // Create a copy of the color with alpha based on remaining life percentage
        val lifePercent = life / maxLife
        return Color(color.r, color.g, color.b, color.a * lifePercent)
    }
}

/**
 * A system for managing particles and their lifecycle
 */
class ParticleSystem(
    private val emissionRate: Float = 0.03f // Seconds between emissions
) {
    // Constants
    companion object {
        // Velocity constants
        private const val MIN_HORIZONTAL_VELOCITY = -8f
        private const val MAX_HORIZONTAL_VELOCITY = 8f
        private const val HORIZONTAL_VELOCITY_RANGE = MAX_HORIZONTAL_VELOCITY - MIN_HORIZONTAL_VELOCITY
        
        private const val MIN_VERTICAL_VELOCITY = -230f
        private const val MAX_VERTICAL_VELOCITY = -80f
        private const val VERTICAL_VELOCITY_RANGE = MAX_VERTICAL_VELOCITY - MIN_VERTICAL_VELOCITY
        
        // Color constants
        private const val MIN_ALPHA = 0.8f
        private const val MAX_ALPHA = 1.0f
        private const val ALPHA_RANGE = MAX_ALPHA - MIN_ALPHA
        
        // Size constants
        private const val MIN_SIZE = 1f
        private const val MAX_SIZE = 3.5f
        private const val SIZE_RANGE = MAX_SIZE - MIN_SIZE
        
        // Life constants
        private const val MIN_LIFE_DURATION = 0.3f
        private const val MAX_LIFE_DURATION = 1.1f
        private const val LIFE_DURATION_RANGE = MAX_LIFE_DURATION - MIN_LIFE_DURATION
        
        // Color components
        private const val COLOR_WHITE = 1.0f
    }
    
    // Collection of active particles
    private val particles = mutableListOf<Particle>()
    
    // Emission timer
    private var timeSinceLastEmission = 0f
    
    // Random generator
    private val random = Random.Default
    
    /**
     * Updates all particles and emission timing
     */
    fun update(delta: Float) {
        updateEmissionTimer(delta)
        updateParticles(delta)
    }
    
    /**
     * Updates the emission timer
     */
    private fun updateEmissionTimer(delta: Float) {
        timeSinceLastEmission += delta
    }
    
    /**
     * Updates all particles and removes dead ones
     */
    private fun updateParticles(delta: Float) {
        particles.removeAll { !it.update(delta) }
    }
    
    /**
     * Renders all particles using the provided shape renderer
     */
    fun render(shapeRenderer: ShapeRenderer) {
        if (particles.isEmpty()) return
        
        renderAllParticles(shapeRenderer)
    }
    
    /**
     * Renders all particles with the shape renderer
     */
    private fun renderAllParticles(shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        
        for (particle in particles) {
            shapeRenderer.color = particle.getCurrentColor()
            shapeRenderer.circle(particle.position.x, particle.position.y, particle.size)
        }
        
        shapeRenderer.end()
    }
    
    /**
     * Emits particles at the specified position
     */
    fun emit(x: Float, y: Float, count: Int = 1) {
        repeat(count) {
            createAndAddParticle(x, y)
        }
    }
    
    /**
     * Creates a single particle and adds it to the system
     */
    private fun createAndAddParticle(x: Float, y: Float) {
        val position = Vector2(x, y)
        val velocity = generateRandomVelocity()
        val color = generateRandomColor()
        val size = generateRandomSize()
        val life = generateRandomLifeDuration()
        
        particles.add(Particle(position, velocity, color, size, life, life))
    }
    
    /**
     * Generates a random velocity vector
     * Mostly downward with minimal horizontal variation
     */
    private fun generateRandomVelocity(): Vector2 {
        val velocityX = MIN_HORIZONTAL_VELOCITY + random.nextFloat() * HORIZONTAL_VELOCITY_RANGE
        val velocityY = MIN_VERTICAL_VELOCITY + random.nextFloat() * VERTICAL_VELOCITY_RANGE
        return Vector2(velocityX, velocityY)
    }
    
    /**
     * Generates a random white color with alpha variation
     */
    private fun generateRandomColor(): Color {
        val alpha = MIN_ALPHA + random.nextFloat() * ALPHA_RANGE
        return Color(COLOR_WHITE, COLOR_WHITE, COLOR_WHITE, alpha)
    }
    
    /**
     * Generates a random particle size
     */
    private fun generateRandomSize(): Float {
        return MIN_SIZE + random.nextFloat() * SIZE_RANGE
    }
    
    /**
     * Generates a random life duration
     */
    private fun generateRandomLifeDuration(): Float {
        return MIN_LIFE_DURATION + random.nextFloat() * LIFE_DURATION_RANGE
    }
    
    /**
     * Checks if it's time to emit particles
     */
    fun canEmit(): Boolean {
        return timeSinceLastEmission > emissionRate
    }
    
    /**
     * Resets the emission timer
     */
    fun resetEmissionTimer() {
        timeSinceLastEmission = 0f
    }
    
    /**
     * Gets the current number of active particles
     */
    fun getParticleCount(): Int {
        return particles.size
    }
    
    /**
     * Clears all particles
     */
    fun clear() {
        particles.clear()
    }
}
