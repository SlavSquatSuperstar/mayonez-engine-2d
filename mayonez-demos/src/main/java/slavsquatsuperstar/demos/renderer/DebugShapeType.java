package slavsquatsuperstar.demos.renderer;

import mayonez.graphics.debug.DebugDraw;
import mayonez.graphics.debug.DebugShape;
import mayonez.graphics.debug.ShapeBrush;
import mayonez.graphics.font.TextLabel;
import mayonez.math.shapes.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Defines a procedurally-generated debug shape type.
 *
 * @author SlavSquatSuperstar
 */
public class DebugShapeType {

    private final String name;
    private final Supplier<Shape> generator;
    private final List<DebugShape> shapes;
    TextLabel text;

    DebugShapeType(String name, Supplier<Shape> generator) {
        this.name = name;
        this.generator = generator;
        shapes = new ArrayList<>();
    }

    int count() {
        return shapes.size();
    }

    void draw(DebugDraw debugDraw) {
        shapes.forEach(debugDraw::drawShape);
    }

    void updateText() {
        text.setMessage("%s: %d".formatted(name, count()));
    }

    void addOne(ShapeBrush brush) {
        shapes.add(new DebugShape(generator.get(), brush));
    }

    void remove(int amount) {
        if (!shapes.isEmpty()) shapes.subList(0, amount).clear();
    }

    void clear() {
        shapes.clear();
    }

}
