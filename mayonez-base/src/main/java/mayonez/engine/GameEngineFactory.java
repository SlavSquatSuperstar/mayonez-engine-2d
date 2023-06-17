package mayonez.engine;

/**
 * A factory class that constructs {@link mayonez.engine.GameEngine} objects
 * depending on the run configuration.
 *
 * @author SlavSquatSuperstar
 */
public final class GameEngineFactory {

    private GameEngineFactory() {
    }

    /**
     * Creates a new game engine object with the given engine type.
     *
     * @param useGL whether to use OpenGL instead of Java's AWT library
     * @return the game engine
     */
    public static GameEngine createGameEngine(boolean useGL) {
        return useGL ? new GLGameEngine() : new JGameEngine();
    }

}
