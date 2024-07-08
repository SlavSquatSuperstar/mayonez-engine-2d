Thank you for downloading Mayonez Engine v0.8.0-pre10!
Find the latest release at https://github.com/SlavSquatSuperstar/mayonez-engine-2d/releases.

Running the Program
1. Install the Java Runtime Environment (JRE) on your system.
   a) Download the latest LTS release from https://adoptium.net/temurin/releases
   b) Select "JRE" under "Package Type" and the correct operating and architecture for your computer.
   c) Follow the instructions for the package installer
2. Open your computer's command line app.
   a) On Mac and Linux, the command line is usually Terminal.
   b) On Windows, the command line is usually Powershell or Command Prompt.
3. Drag the run script to your terminal window and press enter.
   a) If you are on Mac or Linux, use "run".
   b) If you are on Windows, use "run.bat/run.ps1".
   c) If you are blocked from running PowerShell scripts, then run
      "Set-ExecutionPolicy -Scope CurrentUsed RemoteSigned" in PowerShell first.

Demo Scene Controls
1. All Scenes
   - R to reload the current scene
   - P to pause/unpause the scene (may be buggy)
   - Shift + 1-5 to switch scenes
   - Esc to exit the program
2. Space Shooter
   - W/S to fly forward/backwards
   - A/D to turn left/right
   - Q/E to strafe left/right
   - Space to brake
   - Left Mouse and hold to fire weapons
   - 1/2 to switch ammo types
   - Controls can be set in user_config.json
3. Mario
   - W to jump
   - A/D to move side to side
   - S to ground pound
4. Physics Sandbox
   - Left Mouse and drag on objects to move them around
   - Right Mouse*, drag, and release on objects to flick them
   - 1-4 to spawn shapes at the mouse cursor
   - Space to toggle gravity
5. Pool Balls
   - Left Mouse and drag on objects to move them around
   - Right Mouse*, drag, and release on objects to flick them
6. Geometry Dash Editor
   - WASD to move
   - Left Mouse on buttons to select a brush
   - Left Mouse on an empty space, with a brush selected, to place a block
*Note: Right mouse is known to not work with some trackpads.

Changing User Preferences
1. Program settings are stored in "preferences.json" next to the .jar file.
2. Any changes to "preferences.json" will be applied upon restart.
3. Removing a line will cause the program to load the defaults for that setting.

Log Files
1. Log files are automatically generated if the preference "save_logs" is set to true.
2. Logs record application events used to debug the game if something goes wrong.
3. Players may safely delete log files if no errors occur while running the program.

This software is licensed under the GNU General Public License, version 3 (GPLv3).
See LICENSE.txt for more information.

For email inquiries, contact slavsquatsuperstar@gmail.com.
Copyright SlavSquatSuperstar 2023 GPLv3 License