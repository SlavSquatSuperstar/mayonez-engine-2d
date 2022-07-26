# Mayonez Engine

The 2D Java game engine to fit your Slavic needs!*†

This project was developed using macOS 12.x Monterey (Intel). This app has not been tested on any other systems.

## Quick Start Guide

### Instructions for End-Users

- Download the latest Java version: [JRE 17+](https://www.oracle.com/java/technologies/downloads/)
- To check if Java is installed, enter 'java --version' inside a command line window.
- Download the repository, drag 'run.sh' (Mac/Linux) or 'run.bat' (Windows) from the extracted folder to a command line
  window, and press enter.

### Instructions for Developers

- Java Version: [Oracle OpenJDK 17+](https://jdk.java.net/) or [Eclipse Temurin 17+](https://adoptium.net/)
- Kotlin Version: [1.7.0](https://github.com/JetBrains/kotlin/releases/tag/v1.7.0)
- Recommended IDE: [Intellij IDEA 2022.1.x+](https://www.jetbrains.com/idea/download/)
- Build System: [Gradle 7.4.x+](https://gradle.org/install/)
    - Utilizes [Gradle Shadow Plugin](https://github.com/johnrengelman/shadow)
- Dependencies (bundled in JAR)
    - [LWJGL](https://www.lwjgl.org/customize) (user OS & architecture specific)
      with [JOML](https://joml-ci.github.io/JOML/)
        - Libraries: GLFW, OpenGL, STB
    - [Kotlin Standard Library](https://kotlinlang.org/docs/getting-started.html)
    - [JSON in Java](https://github.com/stleary/JSON-java) (org.json)
    - [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/)
    - [Apache Commons IO](https://commons.apache.org/proper/commons-io/)
    - [Java Reflections](https://github.com/ronmamo/reflections)
    - [SLF4J Simple Binding](https://www.slf4j.org/)

## Additional Info

### Acknowledgements

A massive, heartfelt thanks goes out to the following people and resources for inspiration and guidance:

- [GamesWithGabe](https://youtube.com/c/GamesWithGabe): For continuously keeping me motivated to work on my engine, no
  matter how much frustration debugging causes me.
- [The Cherno](https://youtube.com/c/TheChernoProject): For teaching me the theoretical side of designing a proper game
  engine.
- [CodeNMore](https://www.youtube.com/playlist?list=PLah6faXAgguMnTBs3JnEJY0shAc18XYQZ): For creating first game engine
  tutorial I ever followed and starting me on this crazy journey.
- [javidx9 (OneLoneCoder)](https://www.youtube.com/c/javidx9): For explaining the hard math concepts behind video
  games in a simple manner.
- [Michel Van Bizen](https://www.youtube.com/c/MichelvanBiezen): For working through the difficult mathematical proofs
  in comprehensive detail.
- [Unity Engine](https://docs.unity3d.com/ScriptReference/): ~~Who I'm ripping off~~ For making game dev
  accessible for everybody and providing excellent documentation.
- [Azurite Community](https://azurite-engine.github.io/): For showing that implementing LWJGL and physics from scratch
  isn't as intimidating as it sounds.
- [Greenfoot Developers](https://www.greenfoot.org/files/javadoc/): For proving you **can** use Java for game dev and
  showing the beauty of simplicity.
- [Life of Boris](https://www.youtube.com/c/LifeofBoris/featured): For showing me the Slav way in my time of need.
  🥔🤘🥃
- Stack Overflow/Reddit: For answering questions I never knew I had.
- …And many more!

### Disclaimers

- *This statement has not been evaluated by the International Council of Slavs. This software is not intended to
  idealize, encourage, promote, or spread Communism.

- †The developer is not responsible for any addictions, trauma, injuries, or deaths caused by excessive drinking or
  incorrect squatting position.

## License

This software is licensed under the GNU General Public License (GPL) v3 or any later version, which grants copyleft
protections. In true communist fashion, the source code of this program is freely available to access, run, share,
and modify. Any forks of this project must also be distributed under the same license and may not be made or used in any
proprietary (monetized or closed-source). See [LICENSE.txt](LICENSE.txt) for more details.