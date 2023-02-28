package mayonez.test;

import mayonez.math.*;
import mayonez.math.shapes.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Code sandbox for testing new features in the engine.
 *
 * @author SlavSquatSuperstar
 */
public class MayonezTest {

    public static void main(String[] args) throws Exception {
        var a = new Vec2(0.83506536f, 9.860247f);
        var b = new Vec2(7.5902224f, 15.747411f);
        var c = new Vec2(2.7950883f, 14.430385f);
        var pt = new Vec2(6.566416f, 15.466213f);

//        var tri = new Triangle(a, b, c);
//        System.out.println(Arrays.toString(tri.getVertices()));
//        System.out.println();
//        System.out.println(pt);
//        System.out.println(tri.contains(pt));

//        var tri1 = new Triangle(a, b, pt);
//        System.out.println(Arrays.toString(tri1.getVertices()));
//        System.out.println();

        var tri2 = new Triangle(b, c, pt);
        System.out.println(Arrays.toString(tri2.getVertices()));
        System.out.println();

//        var tri3 = new Triangle(c, a, pt);
//        System.out.println(Arrays.toString(tri3.getVertices()));

//        var red = new Color(255, 0, 0, 0);
//        System.out.println(red);
//        System.out.println(red.hexCode(true));

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
