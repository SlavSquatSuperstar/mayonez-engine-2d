package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.graphics.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.scripts.*;

/**
 * Shrinks and fades an asteroid fragment until it automatically despawns.
 */
public class DespawnAsteroid extends DestroyAfterDuration {

    private final Color color;
    private Sprite sprite;
    private Vec2 startScale;

    public DespawnAsteroid(float lifetime, Color color) {
        super(lifetime);
        this.color = color;
    }

    @Override
    protected void start() {
        sprite = gameObject.getComponent(Sprite.class);
        startScale = transform.getScale();
    }

    @Override
    protected void update(float dt) {
        super.update(dt);

        /*
         * Shrink the fragment quadratically until it disappears.
         *
         * S(l) = s_0(1 - (1 - l/l_0)^2)
         * S = scale, S_0 = start scale
         * l = remaining lifetime, l_0 = max lifetime
         */
        var lifetimeRemaining = this.getLifetime() / this.getMaxLifetime();
        var scaleFunction = 1 - MathUtils.squared(1 - lifetimeRemaining);

        transform.setScale(startScale.mul(scaleFunction));

        if (sprite != null) {
            // Fade the fragment until it disappears
            sprite.setColor(new Color(color, (int) (255 * lifetimeRemaining)));
        }
    }

}
