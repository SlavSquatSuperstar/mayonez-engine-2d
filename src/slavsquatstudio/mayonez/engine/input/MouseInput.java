package slavsquatstudio.mayonez.engine.input;

import java.awt.event.MouseEvent;
import java.util.Observer;

import slavsquatstudio.mayonez.engine.event.MouseInputEvent;

public class MouseInput extends InputListener {

	public MouseInput(Observer o) {
		super(o);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		publish(new MouseInputEvent(e.getButton(), e.getX(), e.getY()));
	}

}
