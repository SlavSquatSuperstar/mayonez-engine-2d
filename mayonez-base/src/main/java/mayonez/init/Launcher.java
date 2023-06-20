package mayonez.init;

import mayonez.*;
import mayonez.util.Record;

/**
 * Initializes all engine components, parses the main method program arguments,
 * and starts the application.
 * <p>
 * Usage: Construct a {@link mayonez.init.Launcher} object and optionally pass in
 * command-line arguments. First, call {@link #setRunConfig()} to parse the {@link mayonez.init.RunConfig}
 * from the arguments or manually enter them through {@link #setRunConfig(RunConfig)}.
 * Then, preload in any number of scenes using {@link #loadScenesToManager(Scene...)}
 * and then start the game with {@link #startGame(Scene)} or {@link #startGame(String)}.
 *
 * @author SlavSquatSuperstar
 */
public class Launcher {

    private final Record programArgs;

    /**
     * Create a launcher for the application without passing in any program arguments.
     */
    public Launcher() {
        this(new String[0]);
    }

    /**
     * Create a launcher for the application and pass in the program arguments.
     *
     * @param args a string array
     */
    public Launcher(String[] args) {
        programArgs = new ArgumentsParser(args).getProgramArgs();
    }

    // Run Config Methods

    /**
     * Set the run config after constructing the launcher.
     *
     * @param config the config
     * @return this object
     */
    public Launcher setRunConfig(RunConfig config) {
        Mayonez.setConfig(config);
        return this;
    }

    /**
     * Automatically set the run config using the provided arguments
     * after constructing the launcher.
     *
     * @return this object
     */
    public Launcher setRunConfig() {
        Mayonez.setConfig(getRunConfigFromArgs());
        return this;
    }

    private RunConfig getRunConfigFromArgs() {
        return new RunConfig(getUseGL());
    }

    boolean getUseGL() throws IllegalArgumentException {
        if (!programArgs.contains("engine")) return RunConfig.DEFAULT_USE_GL;

        var engineArg = programArgs.getString("engine");
        return switch (engineArg) {
            case "" -> throw new IllegalArgumentException("Missing value for option \"engine\"");
            case "gl" -> true;
            case "awt" -> false;
            default -> throw new IllegalArgumentException("Invalid value for option \"engine\"");
        };
    }

    // Scene Manager Methods

    /**
     * Preload one or multiple scenes to the scene manager.
     *
     * @param scenes the scenes to add
     */
    public void loadScenesToManager(Scene... scenes) {
        for (var scene : scenes) {
            SceneManager.addScene(scene);
        }
    }

    /**
     * Start the game with the given scene. Will crash the program if the scene
     * is null.
     *
     * @param scene the starting scene
     */
    public void startGame(Scene scene) {
        Mayonez.start(scene);
    }

    /**
     * Start the game with the scene stored under the given name. Will crash
     * the program if no scene exists with such a name.
     *
     * @param sceneName the starting scene's name
     */
    public void startGame(String sceneName) {
        Mayonez.start(SceneManager.getScene(sceneName));
    }

    // TODO auto start with first loaded scene

}
