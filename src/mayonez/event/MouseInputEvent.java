package mayonez.event;

public class MouseInputEvent extends Event {

	private int button;
	private int x, y;

	public MouseInputEvent(int button, int x, int y) {
		this.button = button;
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return String.format("Button %d clicked at %d, %d", button, x, y);
	}

}
