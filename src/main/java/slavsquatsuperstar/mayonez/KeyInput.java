package slavsquatsuperstar.mayonez;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The receiver for all keyboard-related input events.
 *
 * @author SlavSquatSuperstar
 */
public class KeyInput extends KeyAdapter {

    // Key Fields
    private final boolean[] keys = new boolean[256];

    // KeyListener Methods

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        keys[code] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        keys[code] = false;
    }

    // Getters and Setters

    public boolean keyDown(int keyCode) {
        return keys[keyCode];
    }

    /**
     * Whether any of the keys associated with the specified {@link KeyMapping} are pressed.
     *
     * @param keyName The name of the {@link KeyMapping}.
     * @return Whether the specified key is pressed.
     */
    public boolean keyDown(String keyName) {
        for (KeyMapping m : KeyMapping.values()) {
            if (m.name().equalsIgnoreCase(keyName)) { // if the desired mapping exists
                for (int code : m.keyCodes)
                    if (keys[code])
                        return true;
            }
        }
        return false;
    }

    public int getAxis(String axisName) {
        for (KeyAxis a : KeyAxis.values())
            if (a.toString().equalsIgnoreCase(axisName))
                return a.value();
        return 0;
    }

    // Enum Definitions

    /**
     * Stores two keys intended to perform opposite actions.
     */
    public enum KeyAxis {
        VERTICAL(KeyMapping.DOWN, KeyMapping.UP), HORIZONTAL(KeyMapping.RIGHT, KeyMapping.LEFT);

        private String posKey, negKey;

        KeyAxis(KeyMapping posKey, KeyMapping negKey) {
            this.posKey = posKey.name();
            this.negKey = negKey.name();
        }

        /**
         * @return 1 if the positive key is pressed.<br/>
         * -1 if the negative key is pressed.<br/>
         * 0 if the both or neither key is pressed.<br/>
         */
        int value() { // TODO keyboard acceleration?
            // "vector "method to make sure keys don't override each other
            int negComp = (Game.keyboard().keyDown(negKey)) ? -1 : 0;
            int posComp = (Game.keyboard().keyDown(posKey)) ? 1 : 0;
            return negComp + posComp;
        }

        @Override
        public String toString() {
            return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
        }
    }

    /**
     * Stores any number of virtual key codes under a common name.
     */
    public enum KeyMapping {
        // TODO read from file to assign keybinds (deserialize)
        UP(KeyEvent.VK_W, KeyEvent.VK_UP), DOWN(KeyEvent.VK_S, KeyEvent.VK_DOWN), LEFT(KeyEvent.VK_A, KeyEvent.VK_LEFT),
        RIGHT(KeyEvent.VK_D, KeyEvent.VK_RIGHT), SPACE(KeyEvent.VK_SPACE), SHIFT(KeyEvent.VK_SHIFT),
        EXIT(KeyEvent.VK_ESCAPE);

        private int[] keyCodes;

        KeyMapping(int... keyCodes) {
            this.keyCodes = keyCodes;
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder(name() + " (");
            for (int i = 0; i < keyCodes.length; i++) {
                str.append(KeyEvent.getKeyText(keyCodes[i]));
                if (i < keyCodes.length - 1)
                    str.append(", ");
            }
            str.append(")");
            return str.toString();
        }
    }

}