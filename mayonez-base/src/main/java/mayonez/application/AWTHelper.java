package mayonez.application;


import java.awt.geom.*;

/**
 * Assists in graphics context creation and operations for the AWT engine.
 *
 * @author SlavSquatSuperstar
 */
final class AWTHelper {

    private final static AffineTransform FLIP_XF =
            AffineTransform.getScaleInstance(1.0, -1.0);

    private AWTHelper() {
    }

    /**
     * Creates a transform that flips the window's draw canvas vertically along
     * the middle.
     *
     * @param height the window's height
     * @return the transform
     */
    static AffineTransform getWindowFlipXf(int height) {
        var windowFlipXf = new AffineTransform(FLIP_XF);
        windowFlipXf.translate(0.0, -height);
        return windowFlipXf;
    }

}
