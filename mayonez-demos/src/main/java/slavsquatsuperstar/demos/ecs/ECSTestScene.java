package slavsquatsuperstar.demos.ecs;

import mayonez.*;
import mayonez.graphics.font.TextAlignment;
import mayonez.graphics.font.TextLabel;
import mayonez.input.KeyInput;
import mayonez.math.Random;
import mayonez.math.Vec2;
import slavsquatsuperstar.demos.DemoScene;

import java.util.ArrayList;
import java.util.List;

/**
 * A scene for testing the entity-component-system (Scenes and Nodes) architecture.
 *
 * @author SlavSquatSuperstar
 */
public class ECSTestScene extends DemoScene {

    private TextLabel objCount, compCount;
    private List<Node> testObjects, testComponents;

    // Make sure only one addition/removal per press
    private boolean removedObject;
    private boolean addedComponent, removedComponent;

    public ECSTestScene(String name) {
        super(name);
        setUniqueNodeNames(true);
    }

    @Override
    protected void init() {
        testObjects = new ArrayList<>();
        testComponents = new ArrayList<>();

        removedObject = false;
        addedComponent = false;
        removedComponent = false;

        addNode(new GameObject("Spawn Manager") {
            @Override
            protected void init() {
                var fontSize = 20;
                objCount = new TextLabel("GameObjects:", new Vec2(80, 60))
                        .setFontSize(fontSize)
                        .setAlignment(TextAlignment.LEFT);
                compCount = new TextLabel("Components:", new Vec2(80, 20))
                        .setFontSize(fontSize)
                        .setAlignment(TextAlignment.LEFT);
                addChild(objCount);
                addChild(compCount);
            }

            @Override
            protected void update(float dt) {
                if (KeyInput.keyPressed("=")) {
                    // Add object with components
                    testObjects.addLast(new TestObject());
                    getScene().addNode(testObjects.getLast());
                }
            }

            @Override
            protected void debugRender() {
                var objects = getTopLevelNodes().stream()
                        .filter(n -> n instanceof TestObject)
                        .toList();
                var numComponents = objects.stream()
                        .map(Node::numChildren)
                        .reduce(0, Integer::sum);
                objCount.setMessage("Parents: " + objects.size());
                compCount.setMessage("Children: " + numComponents);

                // Clean up empty objects
                objects.stream()
                        .filter(obj -> obj instanceof TestObject && obj.numChildren() == 0)
                        .forEach(obj -> {
                            if (Random.randomBoolean()) obj.setDestroyed();
                            else getScene().removeNode(obj);
                            testObjects.remove(obj);
                        });
                removedObject = false;
                addedComponent = false;
                removedComponent = false;
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
                addChild(testComponents.getLast());
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
            var parent = getParent();
            if (KeyInput.keyPressed("-")) {
                // Remove object and components
                if (!removedObject && parent.equals(testObjects.getFirst())) {
                    testObjects.removeFirst();
                    parent.getChildren().forEach(testComponents::remove);
                    parent.setDestroyed();
                    Logger.log("Removed %s with %d components", parent, parent.numChildren());
                    removedObject = true;
                }
            }

            if (KeyInput.keyPressed("]")) {
                // Add component to object
                if (parent.equals(testObjects.getFirst()) && !addedComponent) {
                    var comp = new TestComponent();
                    testComponents.addLast(comp);
                    parent.addChild(comp);
                    Logger.log("Added %s to %s", comp, parent);
                    addedComponent = true;
                }
            }
            if (KeyInput.keyPressed("[")) {
                // Remove component from object
                if (this.equals(testComponents.getFirst()) && !removedComponent) {
                    testComponents.removeFirst();
                    if (Random.randomBoolean()) parent.removeChild(this);
                    else this.setDestroyed();
                    removedComponent = true;
                }
            }
        }

        @Override
        protected void onDestroy() {
            Logger.log("Removed %s from %s", this, getParent());
        }

    }

}
