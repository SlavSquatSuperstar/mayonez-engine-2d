# Mayonez Engine

The ultimate made-from-scratch and open-source 2D Java game engine to fit your Slavic (and non-Slavic) needs!

<img src="https://img.shields.io/badge/platform-macOS-lightgrey"></img>
<img src="https://img.shields.io/badge/platform-Windows-lightgrey"></img>
<img src="https://img.shields.io/badge/platform-Linux-lightgrey"></img>
<img src="https://img.shields.io/badge/release-v0.8.1-brightgreen"></img>
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
| macOS (Apple Silicon) | ARM64        | Not Tested | N/A              |
| BSD                   | x86_x64      | Not Tested | N/A              |

## Quick Start Guide

### System Requirements

- 64-bit operating system (newer Java versions may not be available for 32-bit systems)
- Graphics card supporting OpenGL 4.0 or above
- Java 21 or above (see further instructions)

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
    - Package: Packages releases for all platforms
    - Javadoc: Builds the fat .jar and generates JavaDoc
- The `clearlogs.sh`, `clearlogs.bat`, and `clearlogs.ps1` scripts clear all log files generated by the program

Packaging the Project

- The Gradle `packageMac`, `packageLinux`, and `packageWindows` tasks (WIP) build and zip the project with the correct
  natives and run scripts for the specified operating system.
- The Gradle `packageAll` task performs all three of these tasks.
- The distribution .jars can be tested using the `dist/mac/run.sh`, `dist/linux/run.sh`, and
  `dist/windows/run.bat`/`dist/windows/run.ps1` scripts.

Development Tools and Libraries

- Java Version: [Eclipse Temurin 21+](https://adoptium.net)
  or [Oracle OpenJDK 21+](https://www.oracle.com/java/technologies/downloads/#java21)
- Kotlin Version: [2.0+](https://kotlinlang.org/docs/whatsnew20.html)
- Recommended IDE: [Intellij IDEA CE 2024+](https://www.jetbrains.com/idea/download/)
    - [Eclipse Java IDE](https://www.eclipse.org/downloads/) may also be used, but IntelliJ has better Kotlin and Gradle
      integration
- Build System: [Gradle 8.10+](https://gradle.org/install/)

Dependencies (automatically downloaded by Gradle and bundled in .jar)

- [Kotlin Standard Library](https://kotlinlang.org/docs/getting-started.html)
- [LWJGL 3.3.4](https://www.lwjgl.org/customize): User OS & architecture specific
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
- Space Shooter (1):
    - **W**/**S** to fly forward/backwards
    - **A**/**D** to turn left/right
    - **Q**/**E** to strafe left/right
    - **Space** to brake
    - **Left Mouse** and _hold_ to fire weapons
    - **1**-**4** to switch weapons
    - **B** to toggle auto-brake
    - **H** to toggle hints
    - Controls can be set in user_config.json
- Renderer Test (2):
- Physics Sandbox (3):
    - **Left Mouse** and *drag* on objects to move them around
    - **Right Mouse***, *drag*, and *release* on objects to flick them
    - **1**-**4** to spawn shapes at the moues cursor
    - **Space** to toggle gravity
    - **H** to toggle hints
- Pool Balls (4):
    - **Left Mouse** and *drag* on objects to move them around
    - **Right Mouse***, *drag*, and *release* on objects to flick them
- Mario (5):
    - **W** to jump
    - **A**/**D** to move side to side
    - **S** to ground pound
- Geometry Dash Editor (6):
    - **WASD** to move
    - **Left Mouse** on _buttons_ to select a brush
    - **Left Mouse** on an _empty space_, with a brush selected, to place a block

*Note: Right mouse is known to not work on Windows with some trackpads.

## Additional Info

### Sprite Art Sources

The demo game textures were pulled from GamesWithGabe's GitHub repositories under the MIT license or created in
GIMP 2.10.

- [Geometry Dash Series](https://github.com/codingminecraft/GeometryDash): Repository by GamesWithGabe
- [Mario LWJGL Series](https://github.com/codingminecraft/MarioYoutube): Repository by GamesWithGabe
- [GIMP](https://www.gimp.org/): Cross-platform, open-source raster image editor, with .xcf files included in
  the [pixelart](pixelart) folder.

### Acknowledgements

The following individuals, communities, or projects were instrumental for inspiration and guidance.

- GamesWithGabe [2D game engine](https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE)
  and [Geometry Dash](https://www.youtube.com/playlist?list=PLtrSb4XxIVbpSD7Gv0GLtMtKxrFmmS3K2) tutorials
- The Cherno [OpenGL](https://www.youtube.com/playlist?list=PLlrATfBNZ98foTJPJ_Ev03o2oq3-GGOS2) and
  [game engine](https://www.youtube.com/playlist?list=PLlrATfBNZ98dC-V-N3m0Go4deliWHPFwT) tutorials
- CodeNMore [2D game engine](https://www.youtube.com/playlist?list=PLah6faXAgguMnTBs3JnEJY0shAc18XYQZ) tutorial
- [Unity](https://docs.unity3d.com/ScriptReference/) and [Godot](https://docs.godotengine.org/en/stable/)
  game engine documentations
- [Azurite](https://azurite-engine.github.io/), [OneLoneCoder](https://github.com/OneLoneCoder/olcPixelGameEngine),
  and [Greenfoot](https://www.greenfoot.org/door) game engines
- [Box2D](https://box2d.org/), [dyn4j](https://dyn4j.org/), and [IainWinter](https://github.com/IainWinter/IwEngine)
  physics engines
- [javidx9](https://www.youtube.com/@javidx9), [Michel Van Bizen](https://www.youtube.com/@MichelvanBiezen),
  [Iain Winter](https://winter.dev/), and [Two-Bit Coding](https://www.youtube.com/@two-bitcoding8018)
  physics and math tutorials
- [Life of Boris](https://www.youtube.com/@LifeofBoris) life advice
- Random [Stack Overflow](https://stackoverflow.com/) and [Reddit](https://www.reddit.com/) threads

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