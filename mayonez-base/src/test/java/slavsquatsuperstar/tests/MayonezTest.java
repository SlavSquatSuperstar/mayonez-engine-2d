package slavsquatsuperstar.tests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Code sandbox for testing new features in the engine.
 *
 * @author SlavSquatSuperstar
 */
public class MayonezTest {

    public static void main(String[] args) throws Exception {
//        System.out.println(scanDirectory1("."));
//        System.out.println(scanDirectory2("."));
//
//        System.out.println(scanDirectory1("slavsquatsuperstar"));
//        System.out.println(scanDirectory2("slavsquatsuperstar"));

        System.out.println(scanDirectory1("assets/shaders/default.glsl"));
        System.out.println(scanDirectory2("assets/shaders/default.glsl"));

//        System.out.println(scanDirectory1("testassets"));
//        System.out.println(scanDirectory2("testassets"));
    }

    private static List<String> scanDirectory1(String path) {
        List<String> files = new ArrayList<>();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try (InputStream in = cl.getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(in))
        ) {
            String resource;
            while ((resource = br.readLine()) != null) {
                if (resource.endsWith(".class")) continue;
                if (path.equals("") || path.equals(".")) files.add(resource);
                else {
                    String fullPath = String.format("%s/%s", path, resource);
                    files.add(fullPath);
                }
            }
        } catch (Exception e) {

        }
        return files;
    }

    private static List<String> scanDirectory2(String path) {
        List<String> files = new ArrayList<>();
        try (InputStream in = ClassLoader.class.getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(in))
        ) {
            String resource;
            while ((resource = br.readLine()) != null) {
                if (resource.endsWith(".class")) continue;
                if (path.equals("") || path.equals(".")) files.add(resource);
                else {
                    String fullPath = String.format("%s/%s", path, resource);
                    files.add(fullPath);
                }
            }
        } catch (Exception e) {

        }
        return files;
    }

}
