package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import mayonez.scripts.combat.*;
import mayonez.scripts.movement.*;
import mayonez.util.*;
import slavsquatsuperstar.demos.spacegame.scripts.ThrustController;
import slavsquatsuperstar.demos.spacegame.scripts.ThrustDirection;
import slavsquatsuperstar.demos.spacegame.scripts.Thruster;

/**
 * A player-controlled spaceship that can fire projectiles.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerShip extends GameObject {

    private final String spriteName;

    public PlayerShip(String name, String spriteName) {
        super(name, Transform.scaleInstance(new Vec2(2, 2)), ZIndex.SPACESHIP.zIndex);
        this.spriteName = spriteName;
    }

    @Override
    protected void init() {
        getScene().getCamera().setSubject(this).setFollowAngle(false);

        // Physics
        addComponent(new Rigidbody(1f));
        addComponent(new BoxCollider(new Vec2(0.85f, 1f)));

        // Visuals
        addComponent(SpritesFactory.createSprite(spriteName));
        addComponent(new Script() { // For testing DebugRenderer refactoring
            @Override
            public void update(float dt) {
                getScene().getDebugDraw().drawShape(new Circle(transform.getPosition(), 1.5f), Colors.CYAN);
                getScene().getDebugDraw().drawVector(transform.getPosition(),
                        getRigidbody().getVelocity().mul(0.1f), Colors.LIGHT_GREEN);
            }
        });

        // Scripts
        addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
        addComponent(new KeyMovement(MoveMode.FORCE, 10f, "horizontal2", "vertical").setObjectAligned(true));
        addComponent(new KeyRotation(MoveMode.VELOCITY, 120f, "horizontal"));
//        addComponent(new ClickToMove(MoveMode.VELOCITY, 10f, true));
        addComponent(new FireProjectile(0.2f) {
            private int weaponChoice = 1;

            @Override
            public void update(float dt) {
                super.update(dt);
                if (KeyInput.keyPressed("1")) {
                    weaponChoice = 1;
                    setCooldown(0.2f);
                } else if (KeyInput.keyPressed("2")) {
                    weaponChoice = 2;
                    setCooldown(0.4f);
                }
            }

            @Override
            protected boolean readyToFire() {
                return MouseInput.buttonDown("left mouse");
            }

            @Override
            protected GameObject spawnProjectile() {
                if (weaponChoice == 1) {
                    return Projectiles.createLaser(gameObject);
                } else if (weaponChoice == 2) {
                    return Projectiles.createPlasma(gameObject);
                } else return null;
            }
        }.setEnabled(true));

        // Sub-Objects
        var lBack = new Thruster(ThrustDirection.FORWARD);
        var rBack = new Thruster(ThrustDirection.FORWARD);
        var lFront = new Thruster(ThrustDirection.BACKWARD);
        var rFront = new Thruster(ThrustDirection.BACKWARD);
        var fLeft = new Thruster(ThrustDirection.RIGHT, ThrustDirection.TURN_RIGHT);
        var bLeft = new Thruster(ThrustDirection.RIGHT, ThrustDirection.TURN_LEFT);
        var fRight = new Thruster(ThrustDirection.LEFT, ThrustDirection.TURN_LEFT);
        var bRight = new Thruster(ThrustDirection.LEFT, ThrustDirection.TURN_RIGHT);

        // aft thrusters
        getScene().addObject(Thruster.createObject(lBack, "Left Rear Thruster", this,
                new Transform(new Vec2(-0.1f, -0.6f), 0f, new Vec2(0.3f))));
        getScene().addObject(Thruster.createObject(rBack, "Right Rear Thruster", this,
                new Transform(new Vec2(0.1f, -0.6f), 0f, new Vec2(0.3f))));

        // front thrusters
        getScene().addObject(Thruster.createObject(lFront, "Left Front Thruster", this,
                new Transform(new Vec2(-0.075f, 0.46f), 180f, new Vec2(0.1f))));
        getScene().addObject(Thruster.createObject(rFront, "Right Front Thruster", this,
                new Transform(new Vec2(0.075f, 0.46f), 180f, new Vec2(0.1f))));

        // port thrusters
        getScene().addObject(Thruster.createObject(fLeft, "Front Left Thruster", this,
                new Transform(new Vec2(-0.14f, 0.39f), -90, new Vec2(0.08f))));
        getScene().addObject(Thruster.createObject(bLeft, "Rear Left Thruster", this,
                new Transform(new Vec2(-0.2f, -0.36f), -90, new Vec2(0.08f))));

        // starboard thrusters
        getScene().addObject(Thruster.createObject(fRight, "Front Right Thruster", this,
                new Transform(new Vec2(0.14f, 0.39f), 90, new Vec2(0.08f))));
        getScene().addObject(Thruster.createObject(bRight, "Rear Right Thruster", this,
                new Transform(new Vec2(0.2f, -0.36f), 90, new Vec2(0.08f))));

        addComponent(new ThrustController(lBack, rBack, lFront, rFront, fLeft, bLeft, fRight, bRight));
    }
}
