package mayonez.input;

/**
 * A factory class that constructs {@link mayonez.input.KeyInput} and
 * {@link mayonez.input.MouseInput} objects depending on the run configuration.
 *
 * @author SlavSquatSuperstar
 */
public class InputManagerFactory {

    private InputManagerFactory() {
    }

    /**
     * Creates a new {@link mayonez.input.KeyInput} object.
     *
     * @return the key input manager
     */
    public static KeyInput createKeyInput() {
        return new KeyInput();
    }

    /**
     * Creates a new {@link mayonez.input.MouseInput} object.
     *
     * @return the mouse input manager
     */
    public static MouseInput createMouseInput() {
        return new MouseInput();
    }

}
