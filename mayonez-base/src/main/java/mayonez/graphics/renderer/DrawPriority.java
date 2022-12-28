package mayonez.graphics.renderer;

public enum DrawPriority {

    /**
     * Solid shapes, drawn first.
     */
    FILL(-1),

    /**
     * Shape boundaries, after solid shapes and before lines.
     */
    SHAPE(1),

    /**
     * Lines, drawn after shapes and before points.
     */
    LINE(2),

    /**
     * Single points, drawn last.
     */
    POINT(3);

    public final int zIndex;

    DrawPriority(int zIndex) {
        this.zIndex = zIndex;
    }
}
