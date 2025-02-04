package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;

import static slavsquatsuperstar.demos.physics.SandboxObjectPrefabs.createStaticBox;

/**
 * A scene for testing projectile collision.
 *
 * @author SlavSquatSupertar
 */
public class ProjectileTestScene extends Scene {

    public ProjectileTestScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        getCamera().setCameraScale(10f);
        setGravity(new Vec2());

        // Add target
        var targetBox = createStaticBox("Target Box",
                new Vec2(50f, 0f), new Vec2(5f, 15f), 0f,
                PhysicsMaterial.DEFAULT_MATERIAL);
        addObject(targetBox);

        // Control target
        addObject(new GameObject("Target Controller") {
            @Override
            protected void init() {
                var flashCounter = new Counter(0, 5, 5);

                addComponent(new Script() {
                    @Override
                    protected void start() {
                        targetBox.getComponent(Collider.class)
                                .addCollisionCallback(event -> {
                                    flashCounter.resetToMin();
                                });
                    }

                    @Override
                    protected void update(float dt) {
                        var yInput = KeyInput.getAxis("arrows vertical");
                        targetBox.transform.move(new Vec2(0f, 20f * yInput * dt));

                        var xInput = KeyInput.getAxis("arrows horizontal");
                        targetBox.transform.rotate(-90f * xInput * dt);

                        var x2Input = KeyInput.getAxis(new KeyAxis(Key.MINUS, Key.PLUS));
                        targetBox.transform.scale(new Vec2(1f + 0.5f * x2Input * dt));

                        // Flash red when hit
                        var shapeSprite = targetBox.getComponent(ShapeSprite.class);
                        flashCounter.count(1);
                        if (!flashCounter.isAtMax()) shapeSprite.setColor(Colors.RED);
                        else shapeSprite.setColor(Colors.DARK_GRAY);
                    }
                });
            }
        });

        // Add launcher
        var launcher = new GameObject("Launcher", new Vec2(-50f, 0f)) {
            float angle, speed, size;
            final Vec2 objSize = new Vec2(4f, 2f);

            @Override
            protected void init() {
                angle = 0;
                speed = 1;
                size = 1;
                addComponent(new Script() {
                    @Override
                    protected void update(float dt) {
                        var yInput = KeyInput.getAxis("vertical");
                        speed += 5f * yInput * dt;
                        if (speed < 0f) speed = 0f;

                        var xInput = KeyInput.getAxis("horizontal");
                        angle += -90f * xInput * dt;

                        var x2Input = KeyInput.getAxis("horizontal2");
                        size *= 1 + 0.5f * x2Input * dt;

                        if (KeyInput.keyPressed("space")) {
                            var projXf = new Transform(transform.getPosition(), angle, new Vec2(size));
                            getScene().addObject(new GameObject("Test Projectile", projXf) {
                                @Override
                                protected void init() {
                                    var col = new BoxCollider(objSize);
                                    addComponent(col);
                                    col.addCollisionCallback(event -> {
                                        if (event.other.getName().equals("Target Box")) {
                                            col.getGameObject().destroy();
                                        }
                                    });

                                    var rb = new Rigidbody(1);
                                    addComponent(rb);
                                    rb.setVelocity(new Vec2(20f * speed, 0).rotate(angle));

                                    addComponent(new ShapeSprite(Colors.BLUE, false));
                                    addComponent(new DestroyAfterDuration(5f));
                                }
                            });
                        }
                    }

                    @Override
                    protected void debugRender() {
                        var velocity = new Vec2(speed, 0f).rotate(angle);
                        var objPos = transform.getPosition();

                        getScene().getDebugDraw()
                                .drawPoint(objPos, Colors.BLACK);
                        getScene().getDebugDraw()
                                .drawVector(objPos, velocity, Colors.BLACK);
                        getScene().getDebugDraw()
                                .drawShape(new Rectangle(objPos, objSize.times(size), angle),
                                        Colors.GRAY);
                    }
                });
            }
        };
        addObject(launcher);
    }

}
