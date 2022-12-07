package slavsquatsuperstar.demos.spacegame;

import mayonez.GameObject;
import mayonez.Script;
import mayonez.Transform;
import mayonez.graphics.Color;
import mayonez.graphics.Colors;
import mayonez.graphics.sprite.Sprite;
import mayonez.input.KeyInput;
import mayonez.input.MouseInput;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BallCollider;
import mayonez.physics.colliders.BoxCollider;
import mayonez.scripts.KeepInScene;
import mayonez.scripts.movement.KeyMovement;
import mayonez.scripts.movement.KeyRotation;
import mayonez.scripts.movement.MoveMode;

public class PlayerShip extends GameObject {

    private final String shipSprite;

    public PlayerShip(String name, String shipSprite) {
        super(name, Transform.scaleInstance(new Vec2(2, 2)));
        this.shipSprite = shipSprite;
    }

    @Override
    protected void init() {
//        getScene().getCamera().setSubject(this);
        addComponent(Sprite.create(shipSprite));
        addComponent(new Rigidbody(1f));
        addComponent(new BoxCollider(new Vec2(0.85f, 1f)));
        addComponent(new KeepInScene(getScene().getSize().mul(-0.25f), getScene().getSize().mul(0.25f), KeepInScene.Mode.WRAP));
        addComponent(new KeyMovement(MoveMode.FORCE, 10f, "horizontal2", "vertical").setObjectAligned(true));
        addComponent(new KeyRotation(MoveMode.FORCE, 2.5f, "horizontal"));
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
                    return createProjectile(transform, "Laser", 0.2f, Colors.RED);
                } else if (weaponChoice == 2) {
                    return createProjectile(transform, "Plasma", 0.3f, Colors.SKY_BLUE);
                } else return null;
            }
        });
        addComponent(new Script() {
            @Override
            public void update(float dt) {
                // Break
                if (KeyInput.keyDown("space")) {
                    getRigidbody().setDrag(2f);
                    getRigidbody().setAngDrag(2f);
                } else {
                    getRigidbody().setDrag(0f);
                    getRigidbody().setAngDrag(0f);
                }
            }
        });
    }

    private GameObject createProjectile(Transform transform, String name, float size, Color color) {
        return new GameObject(name, new Transform(
                transform.position.add(transform.getUp()), transform.rotation, new Vec2(size)
        )) {
            @Override
            protected void init() {
                addComponent(new Rigidbody(0.01f));
                addComponent(new BallCollider(new Vec2(1f)).setDebugDraw(color, true).setTrigger(true));
                addComponent(new KeepInScene(KeepInScene.Mode.DESTROY));
                addComponent(new Script() {
                    @Override
                    public void start() {
                        getRigidbody().setVelocity(transform.getUp().mul(20f));
                    }
                });
            }

            @Override
            public void onTriggerEnter(GameObject other) {
                setDestroyed();
            }
        };
    }

}
