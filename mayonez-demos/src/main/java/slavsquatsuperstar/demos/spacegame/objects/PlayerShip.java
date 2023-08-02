package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import mayonez.scripts.movement.*;
import slavsquatsuperstar.demos.spacegame.scripts.*;

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
        addComponent(new KeyRotation(120f, MoveMode.VELOCITY, "horizontal"));
//        addComponent(new ClickToMove(10f, MoveMode.VELOCITY, true));
        addComponent(new PlayerFireController(0.2f));

        addThrusters();
    }

    private void addThrusters() {
        // Sub-Objects
        var lBack = new Thruster(ThrustDirection.FORWARD);
        var rBack = new Thruster(ThrustDirection.FORWARD);
        var lFront = new Thruster(ThrustDirection.BACKWARD);
        var rFront = new Thruster(ThrustDirection.BACKWARD);
        var fLeft = new Thruster(ThrustDirection.RIGHT, ThrustDirection.TURN_RIGHT);
        var bLeft = new Thruster(ThrustDirection.RIGHT, ThrustDirection.TURN_LEFT);
        var fRight = new Thruster(ThrustDirection.LEFT, ThrustDirection.TURN_LEFT);
        var bRight = new Thruster(ThrustDirection.LEFT, ThrustDirection.TURN_RIGHT);

        // Rear Thrusters
        getScene().addObject(Thruster.createObject(lBack, "Left Rear Thruster", this,
                new Transform(new Vec2(-0.1f, -0.6f), 0f, new Vec2(0.3f))));
        getScene().addObject(Thruster.createObject(rBack, "Right Rear Thruster", this,
                new Transform(new Vec2(0.1f, -0.6f), 0f, new Vec2(0.3f))));

        // Front Thrusters
        getScene().addObject(Thruster.createObject(lFront, "Left Front Thruster", this,
                new Transform(new Vec2(-0.075f, 0.46f), 180f, new Vec2(0.1f))));
        getScene().addObject(Thruster.createObject(rFront, "Right Front Thruster", this,
                new Transform(new Vec2(0.075f, 0.46f), 180f, new Vec2(0.1f))));

        // Left Thrusters
        getScene().addObject(Thruster.createObject(fLeft, "Front Left Thruster", this,
                new Transform(new Vec2(-0.14f, 0.39f), -90, new Vec2(0.08f))));
        getScene().addObject(Thruster.createObject(bLeft, "Rear Left Thruster", this,
                new Transform(new Vec2(-0.2f, -0.36f), -90, new Vec2(0.08f))));

        // Right Thrusters
        getScene().addObject(Thruster.createObject(fRight, "Front Right Thruster", this,
                new Transform(new Vec2(0.14f, 0.39f), 90, new Vec2(0.08f))));
        getScene().addObject(Thruster.createObject(bRight, "Rear Right Thruster", this,
                new Transform(new Vec2(0.2f, -0.36f), 90, new Vec2(0.08f))));

        addComponent(new ThrustController(lBack, rBack, lFront, rFront, fLeft, bLeft, fRight, bRight));
    }
}
