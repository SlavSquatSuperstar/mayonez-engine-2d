package tests;

import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.mayonez.physics2d.CircleCollider;
import com.slavsquatsuperstar.mayonez.physics2d.Collider2D;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainTest {

	public static void main(String[] args) {
//		File file = new File(System.getProperty("user.dir"));
//		System.out.println(file.getAbsoluteFile());
//		System.out.println(Arrays.toString(file.listFiles()));

		String[] names = {"Kai", "Jay", "Zane", "Cole", "Lloyd", "Nia", "Wu", "Skylor"};
		List<String> ninjas = Arrays.asList(names);
		List<String> shortNames = ninjas.stream().filter(s -> s.length() <= 4).collect(Collectors.toList());
		shortNames.forEach(System.out::println);
	}

}
