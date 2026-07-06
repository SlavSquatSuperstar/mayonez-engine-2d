package slavsquatsuperstar.demos.ecs;

import mayonez.Component;
import mayonez.GameObject;
import mayonez.Logger;
import mayonez.Script;
import mayonez.graphics.font.TextAlignment;
import mayonez.graphics.font.TextLabel;
import mayonez.input.KeyInput;
import mayonez.math.Random;
import mayonez.math.Vec2;
import slavsquatsuperstar.demos.DemoScene;

import java.util.ArrayList;
import java.util.List;

/**
 * A scene for testing the entity-component-system (Scenes, GameObjects, and Components) architecture.
 *
 * @author SlavSquatSuperstar
 */
public class ECSTestScene extends DemoScene {

    private TextLabel objCount, compCount;
    private List<GameObject> testObjects;
    private List<Component> testComponents;
    private boolean refresh = true;

    public ECSTestScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        testObjects = new ArrayList<>();
        testComponents = new ArrayList<>();

        addObject(new GameObject("Display") {
            @Override
            protected void init() {
                var fontSize = 20;
                objCount = new TextLabel("GameObjects:", new Vec2(100, 60))
                        .setFontSize(fontSize)
                        .setAlignment(TextAlignment.LEFT);
                compCount = new TextLabel("Components:", new Vec2(100, 20))
                        .setFontSize(fontSize)
                        .setAlignment(TextAlignment.LEFT);
                addComponent(objCount);
                addComponent(compCount);
                addComponent(new Script() {
                    @Override
                    protected void debugRender() {
                        if (refresh) {
                            var numComponents = getObjects().stream()
                                    .map(GameObject::numComponents)
                                    .reduce(0, Integer::sum);
                            objCount.setMessage("GameObjects: " + numObjects());
                            compCount.setMessage("Components: " + numComponents);
                            refresh = false;
                        }
                    }
                });
            }
        });

        addObject(new GameObject("Spawner") {
            @Override
            protected void init() {
                addComponent(new Script() {
                    @Override
                    protected void start() {
                        refresh = true;
                    }

                    @Override
                    protected void update(float dt) {
                        if (KeyInput.keyPressed("=")) {
                            // Add object with components
                            testObjects.addLast(new TestObject());
                            getScene().addObject(testObjects.getLast());
                            refresh = true;
                        }
                        if (KeyInput.keyPressed("-")) {
                            // Remove object and components
                            if (!testObjects.isEmpty()) {
                                var obj = testObjects.removeFirst();
                                obj.getComponents().forEach(testComponents::remove);
                                obj.destroy();
                                Logger.log("Removed %s with %d components", obj, obj.numComponents());
                                refresh = true;
                            }
                        }

                        if (KeyInput.keyPressed("]")) {
                            // Add component to object
                            if (!testObjects.isEmpty()) {
                                var comp = new TestComponent();
                                var obj = testObjects.getLast();
                                testComponents.addLast(comp);
                                obj.addComponent(comp);
                                Logger.log("Added component %s to %s", comp, obj);

                            }
                            refresh = true;
                        }
                        if (KeyInput.keyPressed("[")) {
                            // Remove component from object
                            if (!testComponents.isEmpty()) {
                                var comp = testComponents.removeFirst();
                                var obj = comp.getGameObject();
                                obj.removeComponent(comp);
                                Logger.log("Removed %s from %s", comp, obj);
                                refresh = true;
                            }
                        }
                    }
                });
            }
        });
    }

    // Helper Classes

    private class TestObject extends GameObject {
        public TestObject() {
            super("Test Object");
        }

        @Override
        protected void init() {
            var numComponents = Random.randomInt(1, 5);
            for (int i = 0; i < numComponents; i++) {
                testComponents.addLast(new TestComponent());
                addComponent(testComponents.getLast());
            }
            Logger.log("Added %s with %d components", this, numComponents);
        }
    }

    private static class TestComponent extends Component {
    }

}
