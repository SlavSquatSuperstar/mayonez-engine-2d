package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;
import slavsquatsuperstar.demos.spacegame.combat.CollisionDamage;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * A free-floating satellite that can be destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class Satellite extends GameObject {

    private final SatelliteProperties properties;

    public Satellite(String name, Vec2 position, SatelliteProperties properties) {
        super(name, new Transform(position, Random.randomAngle(), properties.scale()),
                SpaceGameZIndex.SPACESHIP);
        this.properties = properties;
    }

    @Override
    protected void init() {
        setLayer(getScene().getLayer(SpaceGameLayer.SHIPS));

        // Collision
        addComponent(new BoxCollider(new Vec2(1f, 0.44f)));
        addComponent(new KeepInScene(SpaceGameScene.SCENE_HALF_SIZE.mul(-1f),
                SpaceGameScene.SCENE_HALF_SIZE, KeepInScene.Mode.WRAP));
        addComponent(new CollisionDamage());

        // Initial Velocity
        Rigidbody rb;
        addComponent(rb = new Rigidbody(1f, 0.01f, 0.01f));
        rb.setVelocity(transform.getUp().mul(Random.randomFloat(0f, 4f)));

        // Combat
        addComponent(new SpaceshipDestruction());
        addComponent(new Damageable(properties.maxHull()) {
            @Override
            public void onHealthDepleted() {
                var shipDestruction = gameObject.getComponent(SpaceshipDestruction.class);
                if (shipDestruction != null) shipDestruction.startDestructionSequence();
            }
        });
        addComponent(new CollisionDamage());

        // Visuals
        addComponent(Sprites.createSprite(properties.texture()));
    }

}
