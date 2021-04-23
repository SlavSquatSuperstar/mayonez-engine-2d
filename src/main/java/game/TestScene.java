package game;

import mayonez.Scene;

public class TestScene extends Scene {

	public TestScene(String name, int width, int height) {
		super(name, width, height);
		addObject(new TestObject("Test Object"));
	}

}
