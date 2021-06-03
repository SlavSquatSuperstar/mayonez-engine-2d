package tests;

import com.slavsquatsuperstar.mayonez.KeyInput;

public class MainTest {

    public static void main(String[] args) {
//		String[] names = {"Kai", "Jay", "Zane", "Cole", "Lloyd", "Nia", "Wu", "Skylor"};
//		List<String> ninjas = Arrays.asList(names);
//		List<String> shortNames = ninjas.stream().filter(s -> s.length() <= 4).collect(Collectors.toList());
//		shortNames.forEach(System.out::println);

        System.out.println(KeyInput.KeyMapping.UP);
        System.out.println(KeyInput.KeyMapping.LEFT);
        System.out.println(KeyInput.KeyMapping.SHIFT);
        System.out.println(KeyInput.KeyMapping.EXIT);
    }

}
