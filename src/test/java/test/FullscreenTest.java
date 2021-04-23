package test;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class FullscreenTest {

	public static void main(String[] args) {

		JFrame window = new JFrame();
		window.setSize(1080, 720);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = graphics.getDefaultScreenDevice();
		//device.setFullScreenWindow(window);
		System.out.println(device.getFullScreenWindow());

	}

}
