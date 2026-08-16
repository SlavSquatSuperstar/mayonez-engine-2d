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

    // Make sure only one addition/removal per press
    private boolean removedObject;
    private boolean addedComponent, removedComponent;

    public ECSTestScene(String name) {
        super(name);
        uniqueNodeNames = true;
    }

    @Override
    protected void init() {
        testObjects = new ArrayList<>();
        testComponents = new ArrayList<>();

        removedObject = false;
        addedComponent = false;
        removedComponent = false;

        addObject(new GameObject("Manager") {
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
                    protected void update(float dt) {
                        if (KeyInput.keyPressed("=")) {
                            // Add object with components
                            testObjects.addLast(new TestObject());
                            getScene().addObject(testObjects.getLast());
                        }
                    }

                    @Override
                    protected void debugRender() {
                        var objects = getNodes().stream()
                                .filter(n -> n instanceof GameObject)
                                .map(n -> (GameObject) n)
                                .toList();
                        var numComponents = objects.stream()
                                .map(GameObject::numComponents)
                                .reduce(0, Integer::sum);
                        objCount.setMessage("GameObjects: " + objects.size());
                        compCount.setMessage("Components: " + numComponents);

                        // Clean up empty objects
                        objects.stream()
                                .filter(obj -> obj.numComponents() == 0)
                                .forEach(obj -> {
                                    if (Random.randomBoolean()) obj.setDestroyed();
                                    else getScene().removeObject(obj);
                                    testObjects.remove(obj);
                                });
                        removedObject = false;
                        addedComponent = false;
                        removedComponent = false;
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
            var numComponents = Random.randomInt(1, 3);
            for (int i = 0; i < numComponents; i++) {
                testComponents.addLast(new TestComponent());
                addComponent(testComponents.getLast());
            }
            Logger.log("Added %s with %d components", this, numComponents);
        }
    }

    private class TestComponent extends Component {
        public TestComponent() {
        }

        // Test calling add/remove object within child component
        @Override
        protected void update(float dt) {
            if (KeyInput.keyPressed("-")) {
                // Remove object and components
                if (!removedObject && gameObject.equals(testObjects.getFirst())) {
                    testObjects.removeFirst();
                    gameObject.getComponents().forEach(testComponents::remove);
                    gameObject.setDestroyed();
                    Logger.log("Removed %s with %d components", gameObject, gameObject.numComponents());
                    removedObject = true;
                }
            }

            if (KeyInput.keyPressed("]")) {
                // Add component to object
                if (gameObject.equals(testObjects.getFirst()) && !addedComponent) {
                    var comp = new TestComponent();
                    testComponents.addLast(comp);
                    gameObject.addComponent(comp);
                    Logger.log("Added %s to %s", comp, gameObject);
                    addedComponent = true;
                }
            }
            if (KeyInput.keyPressed("[")) {
                // Remove component from object
                if (this.equals(testComponents.getFirst()) && !removedComponent) {
                    testComponents.removeFirst();
                    if (Random.randomBoolean()) gameObject.removeComponent(this);
                    else this.setDestroyed();
                    removedComponent = true;
                }
            }
        }

        @Override
        protected void onDestroy() {
            Logger.log("Removed %s from %s", this, gameObject);
        }

    }

}
