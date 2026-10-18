# Mayonez Engine

The ultimate made-from-scratch and open-source 2D Java game engine to fit your Slavic (and non-Slavic) needs!

![GitHub actions badge](https://github.com/SlavSquatSuperstar/mayonez-engine-2d/actions/workflows/gradle.yml/badge.svg)
![Release version badge](https://img.shields.io/badge/release-0.8.3-brightgreen)
![Linux platform badge](https://img.shields.io/badge/platform-Linux-lightgrey)
![macOS platform badge](https://img.shields.io/badge/platform-macOS-lightgrey)
![Windows platform badge](https://img.shields.io/badge/platform-Windows-lightgrey)

![Java language badge](https://img.shields.io/badge/Made%20with-Java-red)
![Kotlin language badge](https://img.shields.io/badge/Made%20with-Kotlin-red)
![LWJGL framework badge](https://img.shields.io/badge/Made%20with-LWJGL-blue)
![GPLv3 license badge](https://img.shields.io/badge/license-GPLv3-blue)

## Features

This fully-operational game engine includes:

- A 2D renderer that can draw sprites and shapes with both Java AWT and LWJGL OpenGL libraries
- Rigidbody physics with force-based movement, multiple collider shapes, linear and angular impulse resolution, and
  friction
- A Godot-style entity-component-system for creating modular, reusable nodes
- An asset system that can load classpath resources and read from and write to local files
- Key and mouse input that supports key presses, mouse clicks and movement, and key axes
- A scene manager that allows you to load multiple scenes and save their states when switching
- Logging with different message priorities and automatic log file generation
- Debug rendering capabilities for drawing and filling shapes, lines, and points
- An extensible event system for key and mouse inputs and custom game events
- Font rendering for both world and UI text that supports bitmap fonts
- Customizable game preferences that you can set yourself

Planned features to come include:

- A responsive UI system with interactable components such as buttons and menus
- Playable in-game sound effects
- Physics joints letting you attach objects to one another
- An editor that allows you to construct your own scenes and objects
- And much more to come later!

## Platforms

GitHub releases are available for supported platforms. On unsupported platforms, you will need to compile from source.

| Operating System      | Architecture | Status      | Tested On      |
|:----------------------|:-------------|:------------|:---------------|
| GNU/Linux             | x64          | Supported   | Ubuntu 24.04   |
| Windows               | x64          | Supported   | Windows 10     |
| macOS (Intel)         | x64          | Supported   | macOS Tahoe 26 |
| macOS (Apple Silicon) | arm64        | Unsupported | N/A            |

## Quick Start Guide

### System Requirements

- 64-bit operating system
- Graphics card supporting OpenGL 4.0 or above
- Java 21 or above

### Installing Java

- Download the [Java release](https://adoptium.net/temurin/releases) specified above or use your system's package
  manager.
    - Select **JRE** (players) or **JDK** (developers) under "Package Type"
    - Select the correct operating system and architecture for your computer.
    - Download the .pkg/.msi file for GUI installation, or the .zip file for manual installation.
- Run the installer or extract the archive, then check if Java is installed by running `java --version` inside a command
  line/terminal window.
    - On Mac/Linux, the terminal app is usually Terminal.
    - On Windows, the terminal app is usually PowerShell or Command Prompt.

### Instructions for Players

- Download the **mayonez-engine-\<version\>-\<os\>.zip** file for your operating system under the
  "[Releases](https://github.com/SlavSquatSuperstar/mayonez-engine-2d/releases)" section on the GitHub website.
- Drag `run.sh` (Mac/Linux) or `run.bat`/`run.ps1` (Windows) from the extracted folder into a command line/terminal
  window and press Enter.
    - If you are blocked from running Terminal scripts, then run `chmod +x run.sh` in Terminal first.
    - If you are blocked from running PowerShell scripts, then run
      `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned` in PowerShell first.

### Instructions for Developers

Editing the Project

- Install a Java IDE, such as [Intellij IDEA](https://www.jetbrains.com/idea/download/) (recommended) or
  [Eclipse Java IDE](https://www.eclipse.org/downloads/).
- Download this repository by clicking the **Code** button above, then
    - Running `git clone https://github.com/SlavSquatSuperstar/mayonez-engine-2d.git` in a terminal, or
    - Cloning through the GitHub Desktop or CLI.
    - Note: Clicking **Download ZIP** will not download the `.git` folder.
- In your IDE, open the extracted folder or create a new project from existing sources.
    - If prompted, import this repository as a Gradle project.
    - You can also create a new project from version control using the repository link above if your IDE supports it.
- Execute the project through the using `./gradlew run` or `.\gradlew.bat run`.
    - On first run, the wrapper will download the [Gradle](https://gradle.org/) distribution and dependencies.
- There are several preset Gradle run configurations usable by IntelliJ IDEA:
    - **Run**: Launches the demo scenes
    - **Test**: Runs the unit tests
    - **Package**: Packages releases for all platforms
    - **Javadoc**: Builds the fat .jar and generates Javadoc
- The `clearlogs.sh`, `clearlogs.bat`, and `clearlogs.ps1` scripts clear all log files generated by the program.

Packaging the Project

- The Gradle `packageLinux`, `packageMac`, and `packageWindows` tasks build and zip the project with the correct natives
  and run scripts for the specified operating system.
- The Gradle `packageAll` task performs all three of these tasks.
- The distribution .jars can be tested using the `dist/linux/run.sh`, `dist/mac/run.sh`, and
  `dist/windows/run.bat`/`dist/windows/run.ps1` scripts.

## Demo Scenes

The current demo includes 3 scenes: a Space Shooter scene, a Physics Sandbox scene, and a Pool Balls scene.

The demos can be accessed by running the project through Gradle (developers) or the release .jar (players). There are
several hidden scenes with cut features or meant for testing. Can you find them?

### Demo Controls

- All Scenes:
    - **R** to reload the current scene
    - **P** to pause/unpause the scene (may be buggy)
    - **Shift** + **1**–**3** to switch scenes
    - **Esc** to exit the program
- Space Shooter (1):
    - **W**/**S** to fly forward/backwards
    - **A**/**D** to turn left/right
    - **Q**/**E** to strafe left/right
    - **Space** to brake
    - **Left Mouse** and _hold_ to fire weapons
    - **1**–**4** to switch weapons
    - **B** to toggle auto-brake
    - **H** to toggle hints
    - Controls can be set in user_config.json
- Physics Sandbox (2):
    - **Left Mouse** and *drag* on objects to move them around
    - **Right Mouse***, *drag*, and *release* on objects to flick them
    - **1**–**4** to spawn shapes at the moues cursor
    - **Space** to toggle gravity
    - **H** to toggle hints
- Pool Balls (3):
    - **Left Mouse** and *drag* on objects to move them around
    - **Right Mouse***, *drag*, and *release* on objects to flick them

*Note: Right mouse is known to not work on Windows with some trackpads.

## Additional Info

### Sprite Art Sources

The demo game textures were created in [GIMP 3](https://www.gimp.org/) or pulled from GamesWithGabe's GitHub repositories under the MIT license. 

- [GIMP](https://www.gimp.org/): Cross-platform, open-source raster image editor, with .xcf files included in
- [Geometry Dash Series](https://github.com/codingminecraft/GeometryDash): Repository by GamesWithGabe
- [Mario LWJGL Series](https://github.com/codingminecraft/MarioYoutube): Repository by GamesWithGabe
  the [pixelart](pixelart) folder.

### Resources

The following tutorials or projects may be useful or inspiration and guidance.

- [Godot](https://docs.godotengine.org/en/stable/) and [Unity](https://docs.unity3d.com/ScriptReference/)
  API documentations
- GamesWithGabe [2D game engine](https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE)
  and [Geometry Dash](https://www.youtube.com/playlist?list=PLtrSb4XxIVbpSD7Gv0GLtMtKxrFmmS3K2) tutorials
- The Cherno [OpenGL](https://www.youtube.com/playlist?list=PLlrATfBNZ98foTJPJ_Ev03o2oq3-GGOS2) and
  [game engine](https://www.youtube.com/playlist?list=PLlrATfBNZ98dC-V-N3m0Go4deliWHPFwT) tutorials
- CodeNMore [2D game engine](https://www.youtube.com/playlist?list=PLah6faXAgguMnTBs3JnEJY0shAc18XYQZ) tutorial
- [Azurite](https://azurite-engine.github.io/), [OneLoneCoder](https://github.com/OneLoneCoder/olcPixelGameEngine),
  and [Greenfoot](https://www.greenfoot.org/door) game engines
- [Box2D](https://box2d.org/), [dyn4j](https://dyn4j.org/), and [IainWinter](https://github.com/IainWinter/IwEngine)
  physics engines
- [Ten-Minute Physics](https://matthias-research.github.io/pages/tenMinutePhysics/), [Two-Bit Coding](https://www.youtube.com/@two-bitcoding8018),
  [javidx9](https://www.youtube.com/@javidx9), [Michel Van Bizen](https://www.youtube.com/@MichelvanBiezen),
  and [Iain Winter](https://winter.dev/) physics and math tutorials

## License

Mayonez Engine is licensed under the [GNU General Public License, version 3](https://www.gnu.org/licenses/#GPL) (GPLv3)
or any later version, which grants copyleft protections. In true ~~communist~~ ~~socialist~~ FOSS fashion, the source
code of this program is freely available to access, run, share, and modify forever.

Any forks of this project must also be distributed under the same license and may not be used in or made into any
proprietary (closed-source) applications. Forks may still be monetized, as long as the code is open to end users.
See [LICENSE.txt](LICENSE.txt) or read the [online version]((https://www.gnu.org/licenses/gpl-3.0.html)) for more
details.

This software may not be redistributed without the license file. If your download does not contain the GPLv3 license,
you should ask the distributor to include it. The [.txt version](https://www.gnu.org/licenses/gpl-3.0.txt) is available
on the GNU website.

For email inquiries, contact [slavsquatsuperstar@gmail.com](mailto:slavsquatsuperstar@gmail.com)

Copyright © 2026 SlavSquatSuperstar GPLv3