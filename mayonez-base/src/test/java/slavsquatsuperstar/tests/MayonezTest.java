package slavsquatsuperstar.tests;

/**
 * Code sandbox for testing new features in the engine.
 *
 * @author SlavSquatSuperstar
 */
public class MayonezTest {

    public static void main(String[] args) throws Exception {
        int size = 5;
        int i = 3;
        System.out.println("i = " + i);
        int next = (i + 1) % size;
        System.out.println("next = " + next);

        i = 4;
        System.out.println("i = " + i);
        int prev = (i - 1 + size) % size;
        System.out.println("prev = " +  prev);
    }

}
