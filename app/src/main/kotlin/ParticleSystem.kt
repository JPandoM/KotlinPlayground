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
    var size: Float,
    var life: Float,
    var maxLife: Float
) {
    // Constants
    companion object {
        private const val MIN_LIFE = 0f
    }

    /**
     * Updates the particle state based on elapsed time
     * @param delta Time elapsed since last update in seconds
     * @return true if the particle is still alive, false if it died
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
    fun isAlive(): Boolean {
        return life > MIN_LIFE
    }

    /**
     * Returns white color with alpha adjusted for remaining life
     */
    fun getCurrentColor(): Color {
        // Simply adjust the alpha based on the remaining life percentage
        val lifePercent = life / maxLife
        return Color(1f, 1f, 1f, lifePercent)
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
    
        private const val MIN_VERTICAL_VELOCITY = 80f  // Changed to positive to move upward
        private const val MAX_VERTICAL_VELOCITY = 230f // Changed to positive to move upward
        private const val VERTICAL_VELOCITY_RANGE = MAX_VERTICAL_VELOCITY - MIN_VERTICAL_VELOCITY
    
        // Size constants
        private const val MIN_SIZE = 1f
        private const val MAX_SIZE = 3.5f
        private const val SIZE_RANGE = MAX_SIZE - MIN_SIZE
    
        // Life constants
        private const val MIN_LIFE_DURATION = 0.3f
        private const val MAX_LIFE_DURATION = 1.1f
        private const val LIFE_DURATION_RANGE = MAX_LIFE_DURATION - MIN_LIFE_DURATION
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
        // Update particles
        particles.forEach { particle ->
            particle.update(delta)
        }

        // Remove dead particles
        particles.removeAll { !it.isAlive() }
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
        val size = generateRandomSize()
        val life = generateRandomLifeDuration()
    
        particles.add(Particle(position, velocity, size, life, life))
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
     * Clears all particles
     */
    fun clear() {
        particles.clear()
    }
}
