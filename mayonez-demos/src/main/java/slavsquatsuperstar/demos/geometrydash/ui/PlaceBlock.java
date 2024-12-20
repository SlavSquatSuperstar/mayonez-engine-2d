package slavsquatsuperstar.demos.geometrydash.ui;

import mayonez.*;
import mayonez.graphics.textures.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.geometrydash.Block;

/**
 * Places tiles in the world on grid spaces.
 *
 * @author SlavSquatSuperstar
 */
// TODO don't place blocks if already exists
// TODO don't place blocks when clicking button
public class PlaceBlock extends Script {

    // Constants
    private static final float PLACE_BLOCK_DELAY = 0.2f;

    // Field
    private PlaceBlockCursor cursor;
    private Texture cursorTexture;
    private Timer timer;

    @Override
    protected void start() {
        timer = new Timer(PLACE_BLOCK_DELAY);
        timer.reset();
        cursor = new PlaceBlockCursor("Place Block Cursor");
        getScene().addObject(cursor);
    }

    @Override
    protected void update(float dt) {
        timer.countDown(dt);

        // add 0.5 to x/y to center the block
        var mousePos = MouseInput.getPosition().add(new Vec2(0.5f)).floor();
        cursor.setPosition(mousePos);

        // null blocks are still being placed when unselecting
        if (timer.isReady() && MouseInput.buttonDown("left mouse")) {
            timer.reset();
            placeBlock(mousePos);
        }
    }

    private void placeBlock(Vec2 mousePos) {
        if (cursorTexture == null) return; // only add when selecting
        getScene().addObject(new Block("Placed Block", mousePos, cursorTexture));
    }

    public void setCursorTexture(Texture cursorTexture) {
        this.cursorTexture = cursorTexture;
        cursor.setCursorTexture(cursorTexture);
    }

}
