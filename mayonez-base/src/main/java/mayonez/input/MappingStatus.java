package mayonez.input;

import mayonez.annotations.*;

/**
 * Represents the status for a key or button, or whether it is pressed,
 * held, or released.
 *
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
class MappingStatus {

    /**
     * If the input listener detected this mapping to be down.
     */
    private boolean down;

    /**
     * If this mapping was newly pressed in this frame.
     */
    private boolean pressed;

    /**
     * If this mapping is continuously held down.
     */
    private boolean held;

    MappingStatus() {
        down = false;
        pressed = false;
        held = false;
    }

    boolean isDown() {
        return down;
    }

    void setDown(boolean down) {
        this.down = down;
    }

    boolean isPressed() {
        return pressed;
    }

    void setPressed() {
        pressed = true;
    }

    public boolean isHeld() {
        return held;
    }

    void setHeld() {
        pressed = false;
        held = true;
    }

    boolean isReleased() {
        return !pressed && !held;
    }

    void setReleased() {
        pressed = false;
        held = false;
    }

}
