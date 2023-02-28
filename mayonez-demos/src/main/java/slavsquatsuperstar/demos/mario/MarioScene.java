package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.sprites.*;
import mayonez.io.*;
import mayonez.io.image.*;
import mayonez.math.*;

/**
 * For testing the renderer, camera, and coordinate conversions.
 *
 * @author SlavSquatSupertar
 */
public class MarioScene extends Scene {

    private final SpriteSheet sprites;
    private final Texture background;

    public MarioScene(String name) {
        super(name, 1920, 1080, 32);
        sprites = SpriteSheet.create("assets/textures/mario/spritesheet.png", 16, 16, 26, 0);
        background = Assets.getTexture("assets/textures/mario/background.png"); // 1920x1080 (16:9)
    }

    @Override
    public void init() {
        setBackground(Colors.LIGHT_GRAY);
        setBackground(background);
        setGravity(new Vec2());
        getCamera().zoom(0.8f);

        addObject(new Mario(new Vec2(-16f, -12f), sprites.getSprite(0)));

        // Add enemies
        for (var i = 0; i < 16; i++) {
            switch (i % 4) {
                case 0 -> addObject(new Goomba("Goomba", sprites.getSprite(14), getRandomPosition()));
                case 1 -> addObject(new Goomba("Cool Goomba", sprites.getSprite(17), getRandomPosition()));
                case 2 -> addObject(new Goomba("Vintage Goomba", sprites.getSprite(20), getRandomPosition()));
                case 3 -> addObject(new Goomba("Vintage Cool Goomba", sprites.getSprite(23), getRandomPosition()));
            }
        }
    }

}
