package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * Dynamically displays the player's current health.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerHealthBar extends Script {

    private final Vec2 position, size;

    public PlayerHealthBar(Vec2 position, Vec2 size) {
        this.position = position;
        this.size = size;
    }

    @Override
    public void start() {
        getScene().addObject(new GameObject("Health Bar Red") {
            @Override
            protected void init() {
                setZIndex(SpaceGameZIndex.UI);
                var box = new UIBox(position, size, Colors.RED);
                addComponent(box);
            }
        });

        getScene().addObject(new GameObject("Health Bar Green") {
            @Override
            protected void init() {
                // TODO health bar script
                setZIndex(SpaceGameZIndex.UI + 1);
                UIBox box = new UIBox(position, size, Colors.GREEN);
                addComponent(box);

                addComponent(new Script() {
                    @Override
                    protected void update(float dt) {
                        // TODO UI set anchor
                        var healthPercent = getPlayerHealthPercent();
                        box.setSize(size.mul(new Vec2(healthPercent, 1f)));
                        box.setPosition(position.sub(size.mul(new Vec2((1f - healthPercent) * 0.5f, 0f))));
                    }

                    private float getPlayerHealthPercent() {
                        var player = getScene().getObject("Player Spaceship");
                        if (player == null) return 0f;
                        var playerHealth = player.getComponent(Damageable.class);
                        if (playerHealth == null) return 0f;
                        return playerHealth.getHealth() / playerHealth.getMaxHealth();
                    }
                });
            }
        });
    }
}
