package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import mayonez.scripts.movement.*;
import slavsquatsuperstar.demos.spacegame.ZIndex;
import slavsquatsuperstar.demos.spacegame.combat.PlayerFireController;
import slavsquatsuperstar.demos.spacegame.movement.PlayerThrustController;
import slavsquatsuperstar.demos.spacegame.movement.ThrusterPrefabs;

/**
 * A player-controlled spaceship that can fire projectiles.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerShip extends GameObject {

    private final String spriteName;

    public PlayerShip(String name, String spriteName) {
        super(name, Transform.scaleInstance(new Vec2(2, 2)), ZIndex.SPACESHIP);
        this.spriteName = spriteName;
    }

    @Override
    protected void init() {
        getScene().getCamera().setSubject(this).setFollowAngle(false);

        // Physics
        addComponent(new Rigidbody(1f));
        addComponent(new BoxCollider(new Vec2(0.85f, 1f)));

        // Visuals
        addComponent(Sprites.createSprite(spriteName));

        // Scripts
        addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
        addComponent(new KeyMovement(10f, MoveMode.FORCE, "horizontal2", "vertical").setObjectAligned(true));
        addComponent(new KeyRotation(180f, MoveMode.VELOCITY, "horizontal"));
//        addComponent(new ClickToMove(10f, MoveMode.VELOCITY, true));
        addComponent(new PlayerFireController(0.2f));

        var thrusters = ThrusterPrefabs.addThrustersToObject(this);
        addComponent(new PlayerThrustController(thrusters));
    }

}
