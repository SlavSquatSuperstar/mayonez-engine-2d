package com.mayonez;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

	// Mouse Fields
	private int mouseX, mouseY;
	private int dx, dy; // displacement of drag
	private boolean mousePressed, mouseDragged;
	private int button;
//	private boolean[] buttons = new boolean[4];

	// MouseListener Methods

	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed = true;
		button = e.getButton();
//		buttons[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
		mouseDragged = false;
		dx = 0;
		dy = 0;
//		buttons[e.getButton()] = false;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDragged = true;
		dx = e.getX() - mouseX;
		dy = e.getX() - mouseY;

	}

	// Getters and Setters

//	public boolean buttonDown(String buttonName) {
//		for (MouseMapping b : MouseMapping.values())
//			if (b.toString().equalsIgnoreCase(buttonName))
//				return buttons[b.button() - 1];
//		return false;
//	}

	public int getX() {
		return mouseX;
	}

	public int getY() {
		return mouseY;
	}

	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}

	public int getButton() {
		return button;
	}

	public boolean mousePressed() {
		return mousePressed;
	}

	public boolean mouseDragged() {
		return mouseDragged;
	}

	// Enum Declaration
	enum MouseMapping {
		LEFT_MOUSE(MouseEvent.BUTTON1), RIGHT_MOUSE(MouseEvent.BUTTON3);

		private int button;

		private MouseMapping(int button) {
			this.button = button;
		}

		public int button() {
			return button;
		}

		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}

}
