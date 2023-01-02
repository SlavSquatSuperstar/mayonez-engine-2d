# Mayonez Engine

The 2D Java game engine to fit your Slavic needs!*†

This project was developed using macOS 13.x Ventura (Intel) and Intellij IDEA. This progam has not been tested on any
other systems.

## Features

This fully-operational game engine includes:

- A physics system with dynamic movement, multiple collider shapes, linear and angular impulse resolution, and friction
- A 2D renderer that can draw sprites with both Java AWT and LWJGL OpenGL libraries
- An entity-component system that allows you to create modular, reusable game objects and components
- An asset system that can read from classpath resources and read and write to local files
- Keyboard and mouse input that supports key presses, mouse clicks and movement, and key axes
- Scene management that allows you to load multiple scenes and save their states when switching
- Logging with different message priorities and automatic log file generation
- Debug rendering capabilities for drawing and filling shapes, lines, and points
- Customizable game preferences that you can set yourself

## Quick Start Guide

### Instructions for End-Users

- Download the latest Java version: [Eclipse Temurin 17+](https://adoptium.net/temurin/releases) (select "JRE" under "
  Package Type") or [Oracle JRE 17+](https://www.oracle.com/java/technologies/downloads/)
- Extract and run the package, then check if Java is installed by entering "java --version" inside a command line
  window.
- Download the repository, drag 'run' (Mac/Linux) or 'run.bat' (Windows) from the extracted folder to a command line
  window, and press enter.

### Instructions for Developers

- Java Version: [Eclipse Temurin 17+](https://adoptium.net/) or [Oracle OpenJDK 17+](https://jdk.java.net/)
- Kotlin Version: [1.7.20+](https://github.com/JetBrains/kotlin/releases/tag/v1.7.20)
- Recommended IDE: [Intellij IDEA CE 2022.2+](https://www.jetbrains.com/idea/download/)
- Build System: [Gradle 7.6+](https://gradle.org/install/)
    - Utilizes [Gradle Shadow Plugin](https://github.com/johnrengelman/shadow) for creating fat jars
- Dependencies (bundled in JAR)
    - [LWJGL 3.3.1](https://www.lwjgl.org/customize) (user OS & architecture specific)
      with [JOML](https://joml-ci.github.io/JOML/)
        - Libraries: GLFW, OpenGL, STB
    - [Kotlin Standard Library](https://kotlinlang.org/docs/getting-started.html)
    - [JSON in Java](https://github.com/stleary/JSON-java) (org.json)
    - [Java Reflections](https://github.com/ronmamo/reflections)
    - [SLF4J Simple Binding](https://www.slf4j.org/) (dependency for Reflections)
    - [JUnit 5](https://junit.org/junit5/docs/current/user-guide/) (not bundled, for testing only)

## Additional Info

### Sprite Art

The demo game textures were pulled from GamesWithGabe's GitHub repositories or created in the program Piskel.

- [Geometry Dash Series](https://github.com/codingminecraft/GeometryDash): Repository by GamesWithGabe
- [Mario LWJGL Series](https://github.com/codingminecraft/MarioYoutube): Repository by GamesWithGabe
- [Piskel](https://www.piskelapp.com/): Online and desktop pixel art editor, with .piskel files included in
  src/main/resources/sprites

### Acknowledgements

A massive, heartfelt thanks goes out to the following people, groups, and resources for inspiration and guidance.

- [GamesWithGabe](https://youtube.com/c/GamesWithGabe) and [The Cherno](https://youtube.com/c/TheChernoProject): For
  keeping me motivated to work on my engine, no matter how much frustration debugging causes me.
- [CodeNMore](https://www.youtube.com/playlist?list=PLah6faXAgguMnTBs3JnEJY0shAc18XYQZ): For creating first game engine
  tutorial I ever followed and starting me on this crazy and amazing journey.
- [Unity Engine](https://docs.unity3d.com/ScriptReference/): ~~Who I'm ripping off~~ For making game dev
  accessible for everybody and providing excellent documentation.
- [Box2D](https://box2d.org/), [dyn4j](https://dyn4j.org/), and [Iain Winter](https://github.com/IainWinter/IwEngine):
  For demonstrating that implementing a complete physics engine from scratch isn't as intimidating as it sounds.
- [Two-Bit Coding](https://www.youtube.com/@two-bitcoding8018),
  [javidx9/OneLoneCoder](https://www.youtube.com/c/javidx9),
  and [Michel Van Bizen](https://www.youtube.com/c/MichelvanBiezen): For working though the difficult math concepts
  behind game physics in a simple manner.
- [Azurite Community](https://azurite-engine.github.io/): For proving that LWJGL and computer graphics isn't black
  magic and is learnable.
- [Greenfoot Developers](https://www.greenfoot.org/files/javadoc/): For proving you **can** use Java for game dev and
  showing the beauty of simplicity.
- [Life of Boris](https://www.youtube.com/c/LifeofBoris/featured): For showing me the Slav way in my time of need.
  🥔🤘🥃
- Stack Overflow/Reddit: For answering questions I never knew I had.
- …And many more!

### Disclaimers

- *This statement has not been evaluated by the International Council of Slavs. This software is not intended to
  idealize, encourage, promote, or spread Communism.

- †The developer is not responsible for any injuries, trauma, addictions, deaths caused by excessive drinking or
  incorrect squatting position.

## License

This software is licensed under the GNU General Public License (GPL) v3 or any later version, which grants copyleft
protections.
In true communist/socialist fashion, the source code of this program is freely available to access, run, share, and
modify.
Any forks of this project must also be distributed under the same license and may not be made or used in any proprietary
(closed-source) applications. They may still be monetized, as long as the code is open.
See [LICENSE.txt](LICENSE.txt) for more details.
