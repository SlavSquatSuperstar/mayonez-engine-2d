package mayonez.test;

import mayonez.graphics.Color;
import mayonez.math.Vec2;
import mayonez.physics.shapes.Circle;
import mayonez.physics.shapes.Ellipse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Code sandbox for testing new features in the engine.
 *
 * @author SlavSquatSuperstar
 */
public class MayonezTest {

    public static void main(String[] args) throws Exception {
        var c = new Circle(new Vec2(1, 1), 2);
        var e = new Ellipse(new Vec2(1f, 1), new Vec2(4.1f, 4));
        var pc = c.toPolygon();
        var pe = e.toPolygon();
        System.out.println(pc.center());
        System.out.println(pe.center());
        System.out.println(pc.getNumVertices());
        System.out.println(pe.getNumVertices());
        System.out.println(pc.area());
        System.out.println(pe.area());
        System.out.println(pc.angularMass(1f));
        System.out.println(pe.angularMass(1f));
        System.out.println(Arrays.toString(pc.getVertices()));
        System.out.println(Arrays.toString(pe.getVertices()));

        var red = new Color(255, 0, 0, 0);
        System.out.println(red);
        System.out.println(red.hexCode(true));

//        System.out.println(scanDirectory1("."));
//        System.out.println(scanDirectory2("."));
//
//        System.out.println(scanDirectory1("slavsquatsuperstar"));
//        System.out.println(scanDirectory2("slavsquatsuperstar"));

//        System.out.println(scanDirectory1("assets/shaders/default.glsl"));
//        System.out.println(scanDirectory2("assets/shaders/default.glsl"));

//        System.out.println(scanDirectory1("testassets"));
//        System.out.println(scanDirectory2("testassets"));
    }

    private static List<String> scanDirectory1(String path) {
        var files = new ArrayList<String>();
        var cl = Thread.currentThread().getContextClassLoader();
        try (var in = cl.getResourceAsStream(path);
             var br = new BufferedReader(new InputStreamReader(in))
        ) {
            String resource;
            while ((resource = br.readLine()) != null) {
                if (resource.endsWith(".class")) continue;
                if (path.equals("") || path.equals(".")) files.add(resource);
                else {
                    var fullPath = String.format("%s/%s", path, resource);
                    files.add(fullPath);
                }
            }
        } catch (Exception ignored) {
        }
        return files;
    }

    private static List<String> scanDirectory2(String path) {
        var files = new ArrayList<String>();
        try (var in = ClassLoader.class.getResourceAsStream(path);
             var br = new BufferedReader(new InputStreamReader(in))
        ) {
            String resource;
            while ((resource = br.readLine()) != null) {
                if (resource.endsWith(".class")) continue;
                if (path.equals("") || path.equals(".")) files.add(resource);
                else {
                    var fullPath = String.format("%s/%s", path, resource);
                    files.add(fullPath);
                }
            }
        } catch (Exception ignored) {
        }
        return files;
    }

}
