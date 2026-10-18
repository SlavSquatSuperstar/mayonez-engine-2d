package slavsquatsuperstar.demos.geometrydash;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;

/**
 * A grid-aligned tile placed in the world.
 *
 * @author SlavSquatSuperstar
 */
public class Block extends Node {

    private final Texture cursorTexture;

    public Block(String name, Vec2 mousePos, Texture cursorTexture) {
        super(name, mousePos);
        setZIndex(ZIndex.BLOCK);
        this.cursorTexture = cursorTexture;
    }

    @Override
    protected void init() {
        addChild(Sprites.createSprite(cursorTexture));
        addChild(new BoxCollider(new Vec2(1f)));
        addChild(new Rigidbody(0f).setFixedRotation(true));
    }

}
