package slavsquatsuperstar.demos.spacegame.objects;

/**
 * Z-indices used by GameObjects in {@link slavsquatsuperstar.demos.spacegame}.
 *
 * @author SlavSquatSuperstar
 */
public final class ZIndex {

    private ZIndex() {
    }

    public static final int BACKGROUND = -10;
    public static final int PROJECTILE = -2;
    public static final int SPACESHIP = 0;
    public static final int EXHAUST = 1;
    public static final int ASTEROID = 2;

}
