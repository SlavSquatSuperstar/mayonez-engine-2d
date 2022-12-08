package slavsquatsuperstar.demos.spacegame;

import mayonez.GameObject;
import mayonez.Script;
import mayonez.Transform;
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
import slavsquatsuperstar.demos.spacegame.scripts.FireProjectile;
import slavsquatsuperstar.demos.spacegame.scripts.Projectile;

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
        addComponent(new KeepInScene(getScene().getSize().mul(-0.5f), getScene().getSize().mul(0.5f), KeepInScene.Mode.WRAP));
        addComponent(new KeyMovement(MoveMode.FORCE, 10f, "horizontal2", "vertical").setObjectAligned(true));
        addComponent(new KeyRotation(MoveMode.VELOCITY, 1f, "horizontal"));
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
                    return Projectile.createPrefab(
                            new Projectile(gameObject, 1, 25f),
                            "Laser", 0.2f,
                            new BallCollider(new Vec2(1f)).setDebugDraw(Colors.RED, true).setTrigger(true)
                    ).setZIndex(0);
                } else if (weaponChoice == 2) {
                    return Projectile.createPrefab(
                            new Projectile(gameObject, 1.5f, 20f),
                            "Plasma", 0.3f,
                            new BallCollider(new Vec2(1f)).setDebugDraw(Colors.SKY_BLUE, true).setTrigger(true)
                    ).setZIndex(0);
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

}
