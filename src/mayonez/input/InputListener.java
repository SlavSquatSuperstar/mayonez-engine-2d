package mayonez.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Observable;
import java.util.Observer;

import mayonez.event.Event;

public abstract class InputListener extends Observable
		implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	public InputListener(Observer o) {
		addObserver(o);
	}

	protected void publish(String msg) {
		setChanged();
		notifyObservers(msg);
	}
	
	protected void publish(Object arg) {
		setChanged();
		notifyObservers(arg);
	}
	
	protected void publish(Event e) {
		setChanged();
		notifyObservers(e);
	}

	// Key Events
	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	// Mouse Events
	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	// Mouse Motion Events
	public void mouseMoved(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	// Mouse Wheel Events
	public void mouseWheelMoved(MouseWheelEvent e) {
	}

}
