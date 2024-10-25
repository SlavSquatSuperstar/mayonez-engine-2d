package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;
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

    private static final float SATELLITE_HEALTH = 6f;
    private static final String SATELLITE_SPRITE_NAME
            = "assets/spacegame/textures/ships/satellite.png";

    public Satellite(String name, Vec2 position) {
        super(name, new Transform(position, Random.randomAngle(), new Vec2(4f)), SpaceGameZIndex.SPACESHIP);
    }

    @Override
    protected void init() {
        setLayer(getScene().getLayer(SpaceGameLayer.SHIPS));

        // Collision
        addComponent(new BoxCollider(new Vec2(1f, 0.44f)));
        addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
        addComponent(new CollisionDamage());

        // Initial Velocity
        Rigidbody rb;
        addComponent(rb = new Rigidbody(1f, 0.01f, 0.01f));
        rb.setVelocity(transform.getUp().mul(Random.randomFloat(0f, 4f)));

        // Combat
        addComponent(new SpaceshipDestruction());
        addComponent(new Damageable(SATELLITE_HEALTH) {
            @Override
            public void onHealthDepleted() {
                var shipDestruction = gameObject.getComponent(SpaceshipDestruction.class);
                if (shipDestruction != null) shipDestruction.startDestructionSequence();
            }
        });

        // Visuals
        addComponent(Sprites.createSprite(SATELLITE_SPRITE_NAME));
    }

}
