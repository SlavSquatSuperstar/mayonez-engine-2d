package mayonez.input;

import java.awt.event.MouseEvent;
import java.util.Observer;

public class MouseInput extends InputListener {

	public MouseInput(Observer o) {
		super(o);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		publish(String.format("Clicked %s at %d, %d", e.getButton(), e.getX(), e.getY()));
	}

}
