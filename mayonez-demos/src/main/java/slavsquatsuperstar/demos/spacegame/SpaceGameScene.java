package slavsquatsuperstar.demos.spacegame;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.ui.*;
import mayonez.math.Random;
import mayonez.math.*;
import mayonez.math.shapes.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.objects.*;

import java.util.*;

import static slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer.*;

/**
 * The main scene for the space game.
 *
 * @author SlavSquatSuperstar
 */
public class SpaceGameScene extends Scene {

    // Constants
    private final static int SCENE_SIZE = 4;
    private final static int NUM_STARS = 2000;

    // Objects
    private final List<BackgroundObject> backgroundObjects;

    public SpaceGameScene(String name) {
        super(name, Preferences.getScreenWidth() * SCENE_SIZE,
                Preferences.getScreenHeight() * SCENE_SIZE, 32f);
        setBackground(new Color(14, 14, 14));
        backgroundObjects = new ArrayList<>();

        SpaceGameConfig.readConfig();
    }

    @Override
    protected void init() {
        setGravity(new Vec2());
        getCamera().setKeepInScene(true);
        backgroundObjects.clear();

        setLayers();
        addObject(new SpaceObjectSpawner());
        addSolarSystem();
        addBackgroundStars();

        // Health Bar
        var position = new Vec2(105, 770);
        var size = new Vec2(180, 30);
        addObject(new GameObject("Health Bar Red") {
            @Override
            protected void init() {
                addComponent(new UIBox(Colors.RED));
                setZIndex(SpaceGameZIndex.UI);
                transform.setPosition(position);
                transform.setScale(size);
            }
        });
        addObject(new GameObject("Health Bar Green") {
            @Override
            protected void init() {
                // TODO health bar script
                setZIndex(SpaceGameZIndex.UI + 1);
                transform.setPosition(position);
                transform.setScale(size);
                addComponent(new UIBox(Colors.GREEN));
                addComponent(new Script() {
                    @Override
                    protected void update(float dt) {
                        var player = getScene().getObject("Player Spaceship");
                        if (player == null) return;
                        var playerHealth = player.getComponent(Damageable.class);
                        if (playerHealth == null) return;

                        var maxHealth = playerHealth.getMaxHealth();
                        var health = playerHealth.getHealth();
                        var healthPercent = health / maxHealth;
                        transform.setScale(size.mul(new Vec2(healthPercent, 1f)));
                        transform.setPosition(position.sub(position.mul(new Vec2(1f - healthPercent, 0f))));
                    }
                });

//                transform.setPosition(new Vec2(-16, 11.5f));
//                addComponent(new BoxCollider(new Vec2(5, 1)).setEnabled(false));
//                addComponent(new ShapeSprite(Colors.GREEN, true));
            }
        });
    }

//    @Override
//    protected void onUserUpdate(float dt) {
//        // camera controls
//        getCamera().rotate(KeyInput.getAxis("arrows horizontal"));
//        getCamera().zoom(1 + 0.01f * KeyInput.getAxis("arrows vertical"));
//    }

    @Override
    protected void onUserRender() {
        backgroundObjects.forEach(obj -> obj.debugDraw(getDebugDraw()));
    }

    // Helper Methods

    private void setLayers() {
        var shipLayer = getLayer(SHIPS);
        shipLayer.setName("Ships");

        var asteroidLayer = getLayer(ASTEROIDS);
        asteroidLayer.setName("Asteroids");

        var projectileLayer = getLayer(PROJECTILES);
        projectileLayer.setName("Projectiles");
        projectileLayer.setLayerInteract(PROJECTILES, false);
    }

    private void addSolarSystem() {
        addBackgroundObject(new Circle(new Vec2(-10, -8), 12), Colors.DARK_BLUE, SpaceGameZIndex.BACKGROUND); // Earth
        addBackgroundObject(new Circle(new Vec2(12.5f, 7.5f), 2.5f), Colors.DARK_GRAY, SpaceGameZIndex.BACKGROUND); // Moon
        addBackgroundObject(new Circle(new Vec2(-20, 16), 2), Colors.YELLOW, SpaceGameZIndex.BACKGROUND); // Sun
    }

    private void addBackgroundStars() {
        // TODO parallax
        for (var i = 0; i < NUM_STARS; i++) {
            var starPos = this.getRandomPosition().mul(2);

            float starSize;
            boolean isDwarfStar = Random.randomPercent(2f / 3f);
            if (isDwarfStar) starSize = Random.randomGaussian(2.5f, 0.5f); // 1-4
            else starSize = Random.randomGaussian(7, 1); // 4-10
            if (starSize > 1) starSize = 1;

            var starDist = Random.randomFloat(20, 60);
            var starColor = Colors.randomColor(192, 255);
            addBackgroundObject(new Circle(starPos, starSize / starDist), starColor, SpaceGameZIndex.BACKGROUND_STAR);
        }
    }

    private void addBackgroundObject(Shape shape, Color color, int zIndex) {
        backgroundObjects.add(new BackgroundObject(shape, color, zIndex));
    }

}
