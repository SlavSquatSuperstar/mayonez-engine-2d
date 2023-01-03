package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.GameObject;
import mayonez.Script;
import mayonez.Transform;
import mayonez.graphics.sprites.Animator;
import mayonez.graphics.sprites.Sprite;
import mayonez.input.KeyInput;
import mayonez.input.MouseInput;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BoxCollider;
import mayonez.scripts.KeepInScene;
import mayonez.scripts.movement.KeyMovement;
import mayonez.scripts.movement.KeyRotation;
import mayonez.scripts.movement.MoveMode;
import slavsquatsuperstar.demos.spacegame.scripts.FireProjectile;

/**
 * A player-controlled spaceship that can fire projectiles.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerShip extends GameObject {

    private final String spriteName;

    public PlayerShip(String name, String spriteName) {
        super(name, Transform.scaleInstance(new Vec2(2, 2)));
        this.spriteName = spriteName;
        this.setZIndex(1);
    }

    @Override
    protected void init() {
        getScene().getCamera().setSubject(this).setFollowAngle(false);

        // Physics
        addComponent(new Rigidbody(1f));
        addComponent(new BoxCollider(new Vec2(0.85f, 1f)));

        // Visuals
        addComponent(Sprite.create(spriteName));

        GameObject lExhaust, rExhaust; // main (rear) engines
        getScene().addObject(lExhaust = new Exhaust("Left Exhaust", this.transform,
                new Transform(new Vec2(-0.1f, -0.6f), 0f, new Vec2(0.3f))));
        getScene().addObject(rExhaust = new Exhaust("Right Exhaust", this.transform,
                new Transform(new Vec2(0.1f, -0.6f), 0f, new Vec2(0.3f))));

        GameObject lExhaust2, rExhaust2; // RCS (front) engines
        getScene().addObject(lExhaust2 = new Exhaust("Left Exhaust", this.transform,
                new Transform(new Vec2(-0.1f, 0.45f), 225f, new Vec2(0.1f))));
        getScene().addObject(rExhaust2 = new Exhaust("Right Exhaust", this.transform,
                new Transform(new Vec2(0.1f, 0.45f), 135f, new Vec2(0.1f))));

        // Scripts
        addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
        addComponent(new KeyMovement(MoveMode.FORCE, 10f, "horizontal2", "vertical").setObjectAligned(true));
        addComponent(new KeyRotation(MoveMode.VELOCITY, 120f, "horizontal"));
//        addComponent(new FollowMouse(MoveMode.POSITION, 1f, true));
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
        });
        addComponent(new Script() {
            Animator lExhaustAnim, rExhaustAnim, lExhaustAnim2, rExhaustAnim2;
            KeyMovement keyMovement;
            KeyRotation keyRotation;

            @Override
            public void start() {
                keyMovement = gameObject.getComponent(KeyMovement.class);
                keyRotation = gameObject.getComponent(KeyRotation.class);

                lExhaustAnim = lExhaust.getComponent(Animator.class);
                rExhaustAnim = rExhaust.getComponent(Animator.class);
                lExhaustAnim2 = lExhaust2.getComponent(Animator.class);
                rExhaustAnim2 = rExhaust2.getComponent(Animator.class);
            }

            @Override
            public void update(float dt) {
                // Brake
                boolean brake = KeyInput.keyDown("space");
                if (brake) {
                    getRigidbody().setDrag(2f);
                    getRigidbody().setAngDrag(2f);
                } else {
                    getRigidbody().setDrag(0f);
                    getRigidbody().setAngDrag(0f);
                }

                // Enable/Disable Exhaust
                if (keyMovement != null) {
                    boolean forward = keyMovement.getRawInput().dot(new Vec2(0, 1)) > 0;
                    boolean back = keyMovement.getRawInput().dot(new Vec2(0, 1)) < 0;
                    boolean left = keyRotation.getRawInput().x < 0;
                    boolean right = keyRotation.getRawInput().x > 0;
                    if (lExhaustAnim != null) lExhaustAnim.setEnabled(forward || left);
                    if (rExhaustAnim != null) rExhaustAnim.setEnabled(forward || right);
                    if (lExhaustAnim2 != null) lExhaustAnim2.setEnabled(back || left);
                    if (rExhaustAnim2 != null) rExhaustAnim2.setEnabled(back || right);
                }
            }
        });
    }

}
