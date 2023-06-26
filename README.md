# Mayonez Engine

The ultimate made-from-scratch and open-source 2D Java game engine to fit your Slavic (and non-Slavic) needs!

<img src="https://img.shields.io/badge/platform-macOS-lightgrey"></img>
<img src="https://img.shields.io/badge/platform-Windows-lightgrey"></img>
<img src="https://img.shields.io/badge/stable-v0.7.6-green"></img>
<img src=https://img.shields.io/badge/build-passing-brightgreen></img>

<img src="https://img.shields.io/badge/Made%20with-Java-red"></img>
<img src="https://img.shields.io/badge/Made%20with-Kotlin-red"></img>
<img src="https://img.shields.io/badge/Made%20with-LWJGL-blue"></img>
<img src=https://img.shields.io/badge/license-GPL3-blue></img>

## Features

This fully-operational game engine includes:

- A physics system with force-based movement, multiple collider shapes, linear and angular impulse resolution, and
  friction
- A 2D renderer that can draw sprites and shapes with both Java AWT and LWJGL OpenGL libraries
- An entity-component system that allows you to create modular, reusable game objects and components
- An asset system that can read from classpath resources and read and write to local files
- Keyboard and mouse input that supports key presses, mouse clicks and movement, and key axes
- Scene management that allows you to load multiple scenes and save their states when switching
- Logging with different message priorities and automatic log file generation
- Debug rendering capabilities for drawing and filling shapes, lines, and points
- Customizable game preferences that you can set yourself

### Planned

- A UI system with reusable components such as buttons and labels
- An event system for keyboard and mouse inputs
- Font rendering for displaying text on UI
- Nestable game objects that will be fixed to the parent object's position
- And much more to come later!

## Platforms

| Operating System      |   Status   |
|:----------------------|:----------:|
| macOS (Intel)         | Supported  |
| Windows               | Supported  | 
| macOS (Apple silicon) | Not Tested |
| GNU/Linux             | Not Tested |

## Quick Start Guide

### System Requirements

- 64-bit operating system
- Graphics card supporting OpenGL 3.3
- Java 17 (see further instructions)

### Instructions for Players

- Download the latest Java version: [Eclipse Temurin 17+](https://adoptium.net/temurin/releases) (select "JRE" under "
  Package Type") or [Oracle JRE 17+](https://www.oracle.com/java/technologies/downloads/)
- Extract and run the installer, then check if Java is installed by entering `java --version` inside a command
  line/terminal window.
- Download the repository, drag `run` (Mac/Linux) or `run.bat` (Windows) from the extracted folder into your command
  line/terminal, and press enter.

### Instructions for Developers

To download this repository, run `git clone https://github.com/SlavSquatSuperstar/mayonez-engine-2d.git`.
Then, in an IDE, create a new project from existing sources and import a Gradle project.
You can also create a new project from version control using the link above if your editor supports it.
There are several Gradle tasks (compile, test, run, export) included to help in your development.

Below are the tools and libraries used in the development of this project.

- Java Version: [Eclipse Temurin 17+](https://adoptium.net/)
  or [Oracle OpenJDK 17+](https://www.oracle.com/java/technologies/downloads/#java17)
- Kotlin Version: [1.8+](https://kotlinlang.org/docs/whatsnew1820.html)
- Recommended IDE: [Intellij IDEA CE 2023.1+](https://www.jetbrains.com/idea/download/)
- Build System: [Gradle 8.1+](https://gradle.org/install/)
    - Utilizes [Gradle Shadow Plugin](https://github.com/johnrengelman/shadow/releases) for creating fat jars

- Dependencies (bundled in JAR)
    - [LWJGL 3.3.1](https://www.lwjgl.org/customize) (user OS & architecture specific)
        - Libraries: [JOML](https://joml-ci.github.io/JOML/), GLFW, OpenGL, and STB
    - [Kotlin Standard Library](https://kotlinlang.org/docs/getting-started.html)
    - [JSON in Java](https://github.com/stleary/JSON-java) (org.json)
    - [Java Reflections](https://github.com/ronmamo/reflections) (org.reflections)
    - [SLF4J Simple Binding](https://www.slf4j.org/) (dependency for Reflections)
    - [JUnit 5](https://junit.org/junit5/docs/current/user-guide/) (not bundled, for testing only)

## Demo Scenes

The current demo includes 4 scenes: a Space Shooter scene, a Mario scene, a Physics Sandbox scene,
and a Pool Balls scene.

### Demo Key Binds

- All Scenes:
    - **R** to reload the current scene
    - **P** to pause/unpause the scene
    - **Shift** + **1**/**2**/**3**/**4** to switch scenes
    - **Esc** to exit the program
- Space Shooter:
    - **W**/**S** to fly forward/backwards
    - **A**/**D** to turn left/right
    - **Q**/**E** to strafe left/right
    - **Space** to brake
    - **Left mouse** to fire weapons
    - **1**/**2** to switch ammo types
    - **Arrow keys** to zoom camera in/out and rotate camera left/right
- Mario:
    - **W** to jump
    - **AD** to move side to side
    - **S** to ground pound
- Physics Sandbox:
    - **Left mouse** and *drag* on objects to move them around
    - **Right mouse**, *drag*, and *release* on objects to flick them
    - **1**/**2**/**3**/**4** to spawn shapes
    - **Space** to toggle gravity
- Pool Balls:
    - **Left mouse** and *drag* on objects to move them around
    - **Right mouse**, *drag*, and *release* on objects to flick them

## Additional Info

### Sprite Art

The demo game textures were pulled from GamesWithGabe's GitHub repositories or created in the program Piskel.

- [Geometry Dash Series](https://github.com/codingminecraft/GeometryDash): Repository by GamesWithGabe
- [Mario LWJGL Series](https://github.com/codingminecraft/MarioYoutube): Repository by GamesWithGabe
- [Piskel](https://www.piskelapp.com/): Online and desktop pixel art editor, with .piskel files included in
  the [pixelart](pixelart) folder.

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

## License

This software is licensed under the [GNU General Public License, version 3](https://www.gnu.org/licenses/#GPL) (GPLv3)
or any
later version, which grants copyleft
protections.

In true communist/socialist fashion, the source code of this program is freely available to access, run, share, and
modify.

Any forks of this project must also be distributed under the same license and may not be made or used in any proprietary
(closed-source) applications. They may still be monetized, as long as the code is open to the public.
See [LICENSE.txt](LICENSE.txt) or read the [online version]((https://www.gnu.org/licenses/gpl-3.0.html)) for more
details.

This software should not be redistributed without the license file. If your download does not contain the GPL license,
you should ask the distributor to include it. The [.txt version](https://www.gnu.org/licenses/gpl-3.0.txt) is available
on the GNU website.

Copyright © 2023 GPLv3 License