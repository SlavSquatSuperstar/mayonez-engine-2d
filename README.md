# Mayonez Engine

The ultimate made-from-scratch and open-source 2D Java game engine to fit your Slavic (and non-Slavic) needs!

<img src="https://img.shields.io/badge/platform-macOS-lightgrey"></img>
<img src="https://img.shields.io/badge/platform-Windows-lightgrey"></img>
<img src="https://img.shields.io/badge/platform-Linux-lightgrey"></img>
<img src="https://img.shields.io/badge/release-v0.7.10-brightgreen"></img>
<img src=https://img.shields.io/badge/build-passing-brightgreen></img>

<img src="https://img.shields.io/badge/Made%20with-Java-red"></img>
<img src="https://img.shields.io/badge/Made%20with-Kotlin-red"></img>
<img src="https://img.shields.io/badge/Made%20with-LWJGL-blue"></img>
<img src=https://img.shields.io/badge/license-GPLv3-blue></img>

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
- Playable in-game sound effects
- Nestable game objects that will be fixed to the parent object's position
- And much more to come later!

## Platforms

| Operating System      | Architecture | Status     | Tested On        |
|:----------------------|:-------------|:-----------|:-----------------|
| macOS (Intel)         | x86_64       | Supported  | macos Sequoia 15 |
| Windows               | x86_64       | Supported  | Windows 10       | 
| GNU/Linux             | x86_64       | Supported  | Pop!\_OS 22.04   |
| macOS (Apple Silicon) | ARM64        | Not Tested | None             |
| BSD                   | N/A          | Not Tested | None             |

## Quick Start Guide

### System Requirements

- 64-bit operating system (newer Java versions may not be available for 32-bit systems)
- Graphics card supporting OpenGL 4.0 or above
- Java 17 or above (see further instructions)

### Instructions for Players

Installing Java

- Download the latest [Java LTS release](https://adoptium.net/temurin/releases)
    - Select "JRE" under "Package Type" and the correct operating system and architecture for your
      computer.
    - Download the .pkg/.msi GUI installer, or the .zip file if you know how to manually install Java.
- Run the installer or extract the archive, then check if Java is installed by running `java --version` inside a
  command line/terminal window.
    - On Mac/Linux, the terminal app is usually Terminal.
    - On Windows, the terminal app is usually PowerShell or Command Prompt.

Running the Program

- Download the `mayonez-engine-<version>.zip` file for your operating system under the latest
  [release](https://github.com/SlavSquatSuperstar/mayonez-engine-2d/releases) on the GitHub website.
- Drag `run` (Mac/Linux) or `run.bat`/`run.ps1` (Windows) from the extracted folder into a command line/terminal window
  and press Enter.
- Before running PowerShell scripts, run `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned` in PowerShell
  to allow running scripts.

### Instructions for Developers

Editing the Project

- Download this repository by clicking the **Code** button above, or by either
    - Running `git clone https://github.com/SlavSquatSuperstar/mayonez-engine-2d.git` in a
      terminal, or
    - Cloning https://github.com/SlavSquatSuperstar/mayonez-engine-2d in GitHub desktop.
- In an IDE, open the extracted folder or create a new project from existing sources, then tell the IDE to import a
  Gradle project if prompted.
    - You can also create a new project from version control using the repository link above if your editor supports it.
- The Gradle wrapper (`gradlew`/`gradlew.bat`) is configured to automatically download the newest version of Gradle.
    - To change the Gradle version, simply edit the `wrapper` task in the root `build.gradle.kts` and run
      `./gradlew wrapper`/`.\gradlew.bat wrapper`.
- There are several preset Gradle run configurations to facilitate development in Intellij IDEA:
    - Run: Launches the demo scenes
    - Test: Runs the unit tests
    - Javadoc: Builds the fat .jar and generates JavaDoc
- There are also several scripts to help streamline development:
    - `clearlogs`: Clears all log files generated by the program
    - `package`: Builds and zips the project into the `dist/` folder
    - `dist/run`: Runs the .jar file inside `dist/`

Development Tools and Libraries

- Java Version: [Eclipse Temurin 21+](https://adoptium.net)
  or [Oracle OpenJDK 21+](https://www.oracle.com/java/technologies/downloads/#java21)
- Kotlin Version: [1.9+](https://kotlinlang.org/docs/whatsnew20.html)
- Recommended IDE: [Intellij IDEA CE 2024+](https://www.jetbrains.com/idea/download/)
    - [Eclipse Java IDE](https://www.eclipse.org/downloads/) may also be used, but IntelliJ has better Kotlin and Gradle
      integration
- Build System: [Gradle 8.10+](https://gradle.org/install/)

Dependencies (automatically downloaded by Gradle and bundled in .jar)

- [Kotlin Standard Library](https://kotlinlang.org/docs/getting-started.html)
- [LWJGL 3.3.3](https://www.lwjgl.org/customize): User OS & architecture specific
    - Libraries: [JOML](https://joml-ci.github.io/JOML/), GLFW, OpenGL, and STB
- [JSON in Java (org.json)](https://github.com/stleary/JSON-java)
- [Java Reflections (org.reflections)](https://github.com/ronmamo/reflections): Some code taken under Apache 2.0
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/): Not bundled, for test code only

## Demo Scenes

The current demo includes 4 scenes: a Space Shooter scene, a Mario scene, a Physics Sandbox scene,
and a Pool Balls scene.

The demos can be accessed by running the project through Gradle (developers) or the release .jar (players).

### Demo Controls

- All Scenes:
    - **R** to reload the current scene
    - **P** to pause/unpause the scene (may be buggy)
    - **Shift** + **1**-**5** to switch scenes
    - **Esc** to exit the program
- Space Shooter:
    - **W**/**S** to fly forward/backwards
    - **A**/**D** to turn left/right
    - **Q**/**E** to strafe left/right
    - **Space** to brake
    - **Left Mouse** and _hold_ to fire weapons
    - **1**/**2** to switch ammo types
    - **H** to toggle hints (WIP)
    - Controls can be set in user_config.json
- Mario:
    - **W** to jump
    - **A**/**D** to move side to side
    - **S** to ground pound
- Physics Sandbox:
    - **Left Mouse** and *drag* on objects to move them around
    - **Right Mouse***, *drag*, and *release* on objects to flick them
    - **1**-**4** to spawn shapes at the moues cursor
    - **Space** to toggle gravity
- Pool Balls:
    - **Left Mouse** and *drag* on objects to move them around
    - **Right Mouse***, *drag*, and *release* on objects to flick them
- Geometry Dash Editor:
    - **WASD** to move
    - **Left Mouse** on _buttons_ to select a brush
    - **Left Mouse** on an _empty space_, with a brush selected, to place a block
- *Note: Right mouse is known to not work on Windows with some trackpads.

## Additional Info

### Sprite Art Sources

The demo game textures were pulled from GamesWithGabe's GitHub repositories under the MIT license or created in
GIMP 2.10.

- [Geometry Dash Series](https://github.com/codingminecraft/GeometryDash): Repository by GamesWithGabe
- [Mario LWJGL Series](https://github.com/codingminecraft/MarioYoutube): Repository by GamesWithGabe
- [GIMP](https://www.gimp.org/): Cross-platform, open-source raster image editor, with .xcf files included in
  the [pixelart](pixelart) folder.

### Acknowledgements

A massive, heartfelt thanks goes out to the following people, groups, and resources for inspiration and guidance.

- [GamesWithGabe](https://youtube.com/c/GamesWithGabe) and [The Cherno](https://youtube.com/c/TheChernoProject): For
  keeping me motivated to work on my engine, no matter how much frustration debugging causes me.
- [CodeNMore](https://www.youtube.com/playlist?list=PLah6faXAgguMnTBs3JnEJY0shAc18XYQZ): For creating the first game
  engine
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

Mayonez Engine is licensed under the [GNU General Public License, version 3](https://www.gnu.org/licenses/#GPL) (GPLv3)
or any later version, which grants copyleft protections. In true ~~communist~~ ~~socialist~~ FOSS fashion, the source
code of this program is freely available to access, run, share, and modify forever.

Any forks of this project must also be distributed under the same license and may not be used in or made into any
proprietary (closed-source) applications. Forks may still be monetized, as long as the code is open to end users.
See [LICENSE.txt](LICENSE.txt) or read the [online version]((https://www.gnu.org/licenses/gpl-3.0.html)) for
more details.

This software may not be redistributed without the license file. If your download does not contain the GPLv3 license,
you should ask the distributor to include it. The [.txt version](https://www.gnu.org/licenses/gpl-3.0.txt) is available
on the GNU website.

For email inquiries, contact [slavsquatsuperstar@gmail.com](mailto:slavsquatsuperstar@gmail.com)

Copyright © 2020-2024 SlavSquatSuperstar GPLv3