package tests;

import java.io.File;
import java.util.Arrays;

public class MainTest {

	public static void main(String[] args) {
		File file = new File(System.getProperty("user.dir"));
		System.out.println(file.getAbsoluteFile());
		System.out.println(Arrays.toString(file.listFiles()));

//        InputStream in = MainTest.class.getResourceAsStream("/src/test/resources/properties.txt");
	}

}
