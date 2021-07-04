import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashMap;

public class MainTest {

    public static void main(String[] args) throws Exception {
        HashMap<Integer, String> roster = new HashMap<>();
        roster.put(0, "Bob");
        roster.put(1, "Jake");
        roster.put(2, "Tim");
        roster.put(3, "Pete");
        roster.put(4, "Ron");
        for (int num : roster.keySet()) {
            System.out.println(new ImmutablePair<Integer, String>(num, roster.get(num)));
        }
    }

}
