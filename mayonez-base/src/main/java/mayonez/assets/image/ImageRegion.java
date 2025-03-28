package mayonez.assets.image;

import mayonez.math.*;

/**
 * An axis-aligned rectangular region of an image with the top left as the
 * origin (0, 0).
 *
 * @author SlavSquatSuperstar
 */
public record ImageRegion(Vec2 origin, Vec2 size) {

    public int getX() {
        return (int) origin.x;
    }

    public int getY() {
        return (int) origin.y;
    }

    public int getWidth() {
        return (int) size.x;
    }

    public int getHeight() {
        return (int) size.y;
    }

    @Override
    public String toString() {
        return "Region (%s, %s)".formatted(origin, size);
    }

}
