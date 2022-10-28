# Mayonez Engine

The 2D Java game engine to fit your Slavic needs!*†

This project was developed using macOS 13.x Ventura (Intel). This app has not been tested on any other systems.

## Quick Start Guide

### Instructions for End-Users

- Download the latest Java version: [Eclipse Temurin 17+](https://adoptium.net/temurin/releases) (select "JRE" under "
  Package Type") or [Oracle JRE 17+](https://www.oracle.com/java/technologies/downloads/)
- Extract and run the package, then check if Java is installed by entering "java --version" inside a command line
  window.
- Download the repository, drag 'run.sh' (Mac/Linux) or 'run.bat' (Windows) from the extracted folder to a command line
  window, and press enter.

### Instructions for Developers

- Java Version: [Eclipse Temurin 17+](https://adoptium.net/) or [Oracle OpenJDK 17+](https://jdk.java.net/)
- Kotlin Version: [1.7.20](https://github.com/JetBrains/kotlin/releases/tag/v1.7.20)
- Recommended IDE: [Intellij IDEA CE 2022.2+](https://www.jetbrains.com/idea/download/)
- Build System: [Gradle 7.5.1+](https://gradle.org/install/)
    - Utilizes [Gradle Shadow Plugin](https://github.com/johnrengelman/shadow) for creating fat jars
- Dependencies (bundled in JAR)
    - [LWJGL](https://www.lwjgl.org/customize) (user OS & architecture specific)
      with [JOML](https://joml-ci.github.io/JOML/)
        - Libraries: GLFW, OpenGL, STB
    - [Kotlin Standard Library](https://kotlinlang.org/docs/getting-started.html)
    - [JSON in Java](https://github.com/stleary/JSON-java) (org.json)
    - [Java Reflections](https://github.com/ronmamo/reflections)
    - [SLF4J Simple Binding](https://www.slf4j.org/) (dependency for Reflections)

## Additional Info

### Acknowledgements

A massive, heartfelt thanks goes out to the following people, groups, and resources for inspiration and guidance.

- [GamesWithGabe](https://youtube.com/c/GamesWithGabe): For continuously keeping me motivated to work on my engine, no
  matter how much frustration debugging causes me.
- [The Cherno](https://youtube.com/c/TheChernoProject): For teaching me the theoretical side of designing a proper game
  engine.
- [CodeNMore](https://www.youtube.com/playlist?list=PLah6faXAgguMnTBs3JnEJY0shAc18XYQZ): For creating first game engine
  tutorial I ever followed and starting me on this crazy journey.
- [Unity Engine](https://docs.unity3d.com/ScriptReference/): ~~Who I'm ripping off~~ For making game dev
  accessible for everybody and providing excellent documentation.
- [Box2D](https://box2d.org/), [dyn4j](https://dyn4j.org/), and [Winter's Blog](https://blog.winter.dev/): For unpacking the complexities of game physics into a
  followable example.
- [Azurite Community](https://azurite-engine.github.io/): For showing that implementing LWJGL and physics from scratch
  isn't as intimidating as it sounds.
- [javidx9 (OneLoneCoder)](https://www.youtube.com/c/javidx9): For explaining the hard math concepts behind video
  games in a simple manner.
- [Michel Van Bizen](https://www.youtube.com/c/MichelvanBiezen): For working through the difficult mathematical proofs
  in comprehensive detail.
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
protections. In true Communist fashion, the source code of this program is freely available to access, run, share,
and modify. Any forks of this project must also be distributed under the same license and may not be made or used in any
proprietary (closed-source) applications. See [LICENSE.txt](LICENSE.txt) for more details.
