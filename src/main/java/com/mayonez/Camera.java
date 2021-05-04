package com.mayonez;

import com.util.Logger;
import com.util.Vector2;

public class Camera {

	Vector2 position;
//	private int width, height;
//	private int maxXOffset, maxYOffset;
//	private GameObject subject;

	public Camera(Vector2 position, int sceneWidth, int sceneHeight) {
		this.position = position;
//		width = Preferences.SCREEN_WIDTH;
//		height = Preferences.SCREEN_HEIGHT - 20;
//		maxXOffset = sceneWidth - width;
//		maxYOffset = sceneHeight - height;
	}

	public void move(Vector2 displacement) {
		position = position.add(displacement);
	}

	void update() {
		// Center on the middle of the subject
//		if (subject != null) {
//			position.x = subject.transform.position.x + /* subject.getWidth() / 2 */ -width / 2;
//			position.y = subject.transform.position.y + /* subject.getHeight() / 2 */ -height / 2;
//		}
//
//		if (position.x < 0) {
//			position.x = 0;
//		} else if (position.x > maxXOffset) {
//			position.x = maxXOffset;
//		}
//		if (position.y < 0) {
//			position.y = 0;
//		} else if (position.y > maxYOffset) {
//			position.y = maxYOffset;
//		}
		Logger.log("Camera Offset: %.2f, %.2f", position.x, position.y);
	}

	// Getters and Setters

	public void setSubject(GameObject subject) {
//		this.subject = subject;
	}

}
