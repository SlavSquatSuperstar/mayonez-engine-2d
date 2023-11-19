package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.combat.ShipDestruction;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;
import slavsquatsuperstar.demos.spacegame.objects.SpawnManager;

/**
 * A spaceship that can move, fire projectiles, and be destroyed.
 *
 * @author SlavSquatSuperstar
 */
// TODO shields
public abstract class Spaceship extends GameObject {

    private final String spriteName;
    private final float maxHealth;
    private final SpawnManager shipSpawner;

    public Spaceship(String name, String spriteName, float maxHealth, SpawnManager shipSpawner) {
        super(name, Transform.scaleInstance(new Vec2(2f)), SpaceGameZIndex.SPACESHIP);
        this.spriteName = spriteName;
        this.maxHealth = maxHealth;
        this.shipSpawner = shipSpawner;
    }

    @Override
    protected void init() {
        setLayer(getScene().getLayer(SpaceGameLayer.SHIPS));

        // Collision
        addComponent(new BoxCollider(new Vec2(0.85f, 1f)));
        addComponent(new KeepInScene(KeepInScene.Mode.WRAP));

        // Visuals
        addComponent(Sprites.createSprite(spriteName));

        // Destruction
        ShipDestruction shipDestruction;
        addComponent(shipDestruction = new ShipDestruction());
        addComponent(new Damageable(maxHealth) {
            @Override
            public void onHealthDepleted() {
                shipDestruction.startDestructionSequence();
            }

            @Override
            public void onDestroy() {
                shipSpawner.markObjectDestroyed();
            }
        });
    }

}
