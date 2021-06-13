package tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MainTest {

    public static void main(String[] args) {
//		String[] names = {"Kai", "Jay", "Zane", "Cole", "Lloyd", "Nia", "Wu", "Skylor"};
//		List<String> ninjas = Arrays.asList(names);
//		List<String> shortNames = ninjas.stream().filter(s -> s.length() <= 4).collect(Collectors.toList());
//		shortNames.forEach(System.out::println);

        ArrayList<Class> classOrder = new ArrayList<>();
        classOrder.addAll(Arrays.asList(Collider.class, Component.class, Script.class));
        Component[] components = {new Collider(), new Script(), new Component(), new Sprite(), new Box(), new Script(), new Sprite(), new Component()};
        Arrays.sort(components, Comparator.comparingInt(o -> getUpdateOrder(classOrder, o.getClass())));
        System.out.println(Arrays.toString(components));
    }

    static int getUpdateOrder(ArrayList<Class> classOrder, Class cls) {
        int i = classOrder.indexOf(cls);
        if (i > -1)
            return i;
        i = getUpdateOrder(classOrder, cls.getSuperclass());
        return (i > -1) ? i : classOrder.size();

    }

    static class Component {
        @Override
        public String toString() {
            return getClass().getSimpleName();
        }
    }

    static class Sprite extends Component {

    }

    static class Script extends Component {

    }

    static class Collider extends Component {

    }

    static class Box extends Collider {

    }

}
