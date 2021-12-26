package slavsquatsuperstar.mayonez;

import org.jetbrains.annotations.NotNull;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.renderer.Renderable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

/**
 * The display component for the game.
 *
 * @author SlavSquatSuperstar
 */
public class JWindow extends JFrame implements Window {

    private final static AffineTransform FLIP_XF = AffineTransform.getScaleInstance(1.0, -1.0);
    private BufferStrategy bs;
    private Graphics2D g2;

    private boolean closedbyUser;
//    private KeyInput keyboard;
//    private MouseInput mouse;

    public JWindow(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closedbyUser = true; // 'x' button will notify game to exit
            }
        });
    }

    // Engine Methods

    @Override
    public void start() {
        setLocationRelativeTo(null); // center in screen
        setVisible(true);
        initGraphics(); // initialize graphics resources
    }

    @Override
    public void stop() {
        setVisible(false);
        g2.dispose();
        dispose();
    }

    // Game Loop Methods

    @Override
    public boolean notClosedByUser() {
        return !closedbyUser;
    }

    @Override
    public void beginFrame() {
        if (KeyInput.keyDown("exit"))
            closedbyUser = true;
    }

    @Override
    public void endFrame() {
        KeyInput.endFrame();
    }

    // Render Methods

    private void initGraphics() {
        if (!isVisible()) return;
        try {
            createBufferStrategy(2);
            bs = getBufferStrategy();
        } catch (IllegalStateException e) {
            Logger.log("Engine: Error initializing window graphics; trying again next frame.");
        }
    }

    @Override
    public void render(Renderable r) {
        if (bs == null) {
            initGraphics();
            return;
        }

        try {
            // Use a do-while loop to avoid losing buffer frames
            // Source: https://stackoverflow.com/questions/13590002/understand-bufferstrategy
            do {
                g2 = (Graphics2D) bs.getDrawGraphics();
                g2.clipRect(0, 0, getWidth(), getHeight()); // Render things only in the screen
                g2.clearRect(0, 0, getWidth(), getHeight()); // Clear the screen

                // Flip screen upside down
                g2.transform(FLIP_XF);
                g2.translate(0, -getHeight());

                r.draw(g2); // Draw scene
                g2.dispose(); // Flush Resources
                bs.show();
            } while (bs.contentsLost());
        } catch (IllegalStateException e) {
            Logger.trace("Engine: Error rendering current frame; trying again");
        }
    }

    // Input Methods

    @Override
    public void setKeyInput(KeyInput keyboard) {
//        this.keyboard = keyboard;
        addKeyListener(keyboard);
    }

    @Override
    public void setMouseInput(MouseInput mouse) {
//        this.mouse = mouse;
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
    }

    // Properties

    @NotNull
    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }
}
