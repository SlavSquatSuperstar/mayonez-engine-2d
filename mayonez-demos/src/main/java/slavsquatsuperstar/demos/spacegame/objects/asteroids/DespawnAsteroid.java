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
    private boolean reachedHalfLife;

    public DespawnAsteroid(float lifetime, Color color) {
        super(lifetime);
        this.color = color;
    }

    @Override
    protected void start() {
        sprite = gameObject.getComponent(Sprite.class);
        startScale = transform.getScale();
        reachedHalfLife = false;
    }

    @Override
    protected void update(float dt) {
        super.update(dt);

        var lifetimeRemaining = this.getLifetime() / this.getMaxLifetime();
        if (reachedHalfLife) {
            // Shrink the fragment until it disappears
            transform.setScale(startScale.mul(2f * lifetimeRemaining));
        } else {
            if (lifetimeRemaining < 0.5f) reachedHalfLife = true;
        }

        if (sprite != null) {
            // Fade the fragment until it disappears
            sprite.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (255 * lifetimeRemaining)));
        }
    }

}
