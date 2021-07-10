import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.primitives.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.Collider2D;

import java.util.ArrayList;

public class MainTest {

    public static void main(String[] args) throws Exception {
        ArrayList<String> colors = new ArrayList<>();
        colors.add("red");
        colors.add("yellow");
        colors.add("green");
        colors.add("blue");
        for (int i = 0; i < colors.size(); i++) {
            if (i == 0)
                colors.add("three");
            System.out.println(colors.get(i));
        }

        Collider2D aabb = new AlignedBoxCollider2D(new Vector2(1, 1)).setTransform(new Transform(new Vector2()));
        System.out.println(aabb.getClass());
    }

}
