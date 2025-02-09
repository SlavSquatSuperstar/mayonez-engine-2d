Thank you for downloading Mayonez Engine 0.8.2-pre3!
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
- All Scenes
   - R to reload the current scene
   - P to pause/unpause the scene (may be buggy)
   - Shift + 1-3 to switch scenes
   - Esc to exit the program
- Space Shooter (1)
   - W/S to fly forward/backwards
   - A/D to turn left/right
   - Q/E to strafe left/right
   - Space to brake
   - Left Mouse and hold to fire weapons
   - 1-4 to switch weapons
   - B to toggle auto-brake
   - H to toggle hints
   - Controls can be set in user_config.json
- Physics Sandbox (2)
   - Left Mouse and drag on objects to move them around
   - Right Mouse, drag, and release on objects to flick them
   - 1-4 to spawn shapes at the mouse cursor
   - Space to toggle gravity
   - H to toggle hints
- Pool Balls (3)
   - Left Mouse and drag on objects to move them around
   - Right Mouse*, drag, and release on objects to flick them

*Note: Right mouse is known to not work on Windows with some trackpads.

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
Copyright © 2020-2024 SlavSquatSuperstar GPLv3