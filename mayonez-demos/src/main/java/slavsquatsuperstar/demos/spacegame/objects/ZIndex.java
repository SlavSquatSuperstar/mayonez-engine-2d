package slavsquatsuperstar.demos.spacegame.objects;

/**
 * Z-indices used by GameObjects in {@link slavsquatsuperstar.demos.spacegame}.
 *
 * @author SlavSquatSuperstar
 */
public enum ZIndex {
    BACKGROUND(-10),
    PROJECTILE(-2),
    SPACESHIP(0),
    EXHAUST(1),
    ASTEROID(2);

    public final int zIndex;

    ZIndex(int zIndex) {
        this.zIndex = zIndex;
    }
}
