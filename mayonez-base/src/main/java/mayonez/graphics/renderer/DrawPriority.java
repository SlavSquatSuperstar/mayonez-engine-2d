package mayonez.graphics.renderer;

public enum DrawPriority {

    /**
     * Solid shapes, drawn first.
     */
    FILL(-5),

    /**
     * Shape boundaries, after solid shapes and before lines.
     */
    SHAPE(5),

    /**
     * Lines, drawn after shapes and before points.
     */
    LINE(8),

    /**
     * Single points, drawn last.
     */
    POINT(10);

    public final int zIndex;

    DrawPriority(int zIndex) {
        this.zIndex = zIndex;
    }
}
