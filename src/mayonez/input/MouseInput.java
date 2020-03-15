package mayonez.input;

import java.awt.event.MouseEvent;
import java.util.Observer;

public class MouseInput extends InputListener {

	public MouseInput(Observer o) {
		super(o);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		String button;
		
		switch (e.getButton()) {
		
		case MouseEvent.BUTTON1:
			button = "left mouse";
			break;
		case MouseEvent.BUTTON3:
			button = "right mouse";
			break;
		default:
			button = "mouse";
			break;
			
		}
		
		
		publish(String.format("Clicked %s at %d, %d", button, e.getX(), e.getY()));
	}

}
