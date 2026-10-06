# Mayonez Engine Changelog

## v0.8.3-pre9

Global transform and convert demos to node

### Features

- Add Node getGlobalTransform method
- Add Node useParentTransform property to point transform to parent's
- Components set useParentTransform to true by default
- Add Node getSibling method
- Add Node constructor with name and position
- Specify space game player ship index in user config
- Tweak space game shuttle textures
- Specify space game enemy/obstacle/star count in user config

### Refactor

- Replace GameObject.getComponent calls with Node.getChild
- Replace Sprite spriteTransform property with Node transform
- Remove deprecated ECS methods in Scene/GameObject/Component
- Sprite now extends node
- Separate Animator enabled and visible methods
- BREAKING CHANGE: Make Node transform field private and use getter
- BREAKING CHANGE: Remove Script getCollider/getRigidbody methods
- Change Camera subject type to Node
- Convert ECS to Node in all demo scenes

### Fixes

- Fix thruster sprites not animating
- Fix Scene addNode concurrent modification
- Fix Projectiles given correct initial angle and velocity
- Fix memory leak due to Projectiles not despawning

### Documentation

- Update and format Node/Scene Javadoc

## v0.8.3-pre8

FilePath improvements and Shader definition files

### Features

- Split all vertex and fragment shaders into separate files
- Read shader stage filenames from a single JSON definition file
- Read ShaderStage source code from filename
- Remove #type directive from GLSL shaders
- Parse shader uniforms and cache locations after linking
- Throw ShaderException if stages missing during link
- Shader creation methods throw ShaderExceptions by default
- Create Asset init and equals methods
- Shader init method override catches ShaderExceptions
- Remove TextFile auto-close stream property
- FilePath subclasses store URL/File members
- Create FilePath createFile/createDirectory/delete methods
- Create FilePath isFile/isDirectory methods
- Create FilePath getParent/combine methods
- Create JarClasspathFilePath/LocalClasspathFilePath FilePath subclasses
- Replace mayonez.assets.scanner package with FilePath scanFiles method
- Delete org.reflections.vfs package
- Remove FilePath typeName property
- Add FilePath/Asset filename property for actual filename

### Refactor

- Logger uses raw output stream for log file instead of TextFile
- Merge Assets scanResources and scanFiles methods
- Rename FilePath/Asset filename property and related parameters to path
- Move engine name/version files from assets/info folder to info

### Fixes

- Unescape classpath URLs containing special characters
- Convert URLs to file paths with the correct separator character
- Rename --engine option to --backend in run scripts, tests, and Gradle tasks

### Build

- Include test jar file in test classpath

### Documentation

- Null-mark mayonez.renderer.shader package
- Update shader descriptions and doc comments

## v0.8.3-pre7

Backends, VSync, and PhysicsLayers

### Features

- Combine Window beginFrame and endFrame methods into pollEvents
- Window polls events before render frame instead of fixed update
- MouseInput accumulates displacement per frame instead of resetting
- MouseInput double click requires both buttons to be the same
- Replace frame skip on/off preference with max ticks per frame
- Add VSync on/off preference
- Add Scene getTopLevelNodes method
- Recreate Scene root node when restarting instead of reusing
- Application, SceneManager, and Scene requests to stop after current frame rather than instantly exiting
- Free GLFW, OpenGL, and scene after stopping window
- Create Backend enum to specify window library and graphics API
- Replace RunConfig record and EngineType enum with Backend
- Remove OpenGL 4.0 shaders

### Refactor

- Rename SceneLayer to PhysicsLayer
- PhysicsWorld creates PhysicsLayers instead of Scene
- Collider stores PhysicsLayer instead of Node
- Damageables and projectiles use tags instead of SceneLayers
- Camera inherits from Node
- Rename Renderable isVisible method to shouldRender
- CollisionEvent stores collider Node instead of parent GameObject
- Remove Collider collision EventListeners
- Deprecate GameObject, Component, and Script classes in favor of node
- Move OperatingSystem enum to mayonez.application
- Rename UsesEngine (EngineType) annotation with UsesBackend (Backend)

### Build

- Updated Gradle version to 9.7.0
- Use OpenGL 3.3 only and remove fallback option

### Documentation

- Null-mark mayonez.event package

## v0.8.3-pre6

Switch ECS to Node-based

### Features

- Created Node superclass for GameObject/Component to represent scene entities
- Force blank node names to default to class name
- Organize Nodes into trees with parent and children properties
- Add Node scene and scene depth methods
- Add Node name and transform properties, regardless of depth
- Add Node tags property from GameObject
- Add Node enabled/visible properties to toggle updating and rendering
- Add Node shouldUpdate/shouldRender methods to check if enabled/visible globally
- Add Node updateOrder and zIndex property
- Add Node update/fixedUpdate/debugRender methods
- Add Scene property for unique node names
- Scene garbage collects any lingering destroyed nodes every iteration

### Refactor

- Replace SceneLayer usage with tags when applicable
- BREAKING CHANGE: Rename ECS destroy to setDestroyed
- BREAKING CHANGE: Move enable/destroy callbacks to Node
- BREAKING CHANGE: Remove zIndex parameter from GameObject constructor
- Replace collider event listeners with public callback methods
- GameObject stores Node children instead of Components
- Scene stores all Nodes in a flat list and sorts by depth
- Scene stores top level Nodes as children of its root Node
- BREAKING CHANGE: Update/render methods can be overridden for all Nodes
- Update/render all nodes inside Scene method with one loop
- Add Scene methods for searching all Nodes
- Init nodes in forward tree (top-down) order
- Start/destroy nodes in reverse (bottom-up) tree order
- Calling removeNode destroys the node and removes it later
- Move rename unique method to Node
- Rename Nodes to unique after adding to scene

### Build

- Add Gradle kapt compiler plugin

### Test

- Write tests for Node class
- Delete unused GameObject Scene tests

## v0.8.3-pre5

Fixed update and callback queues

### Features

- Add JSpecify and NullAway to annotate nullability for core classes
- Create freeGLFW function
- Add fixed ticks per second preference separate from render FPS
- Add ECS fixedUpdate methods to for physics and time-senitive logic
- GLFW error callback now uses Logger class
- Don't get/set window position if using Wayland
- Add GameObject enabled property
- Add visible property for GameObject/Component classes
- Add GameObject removeComponent method
- Create CallbackBuffer class to queue Runnables
- GameObject names are now changeable
- Add Component name property
- Allow scenes to rename GameObject to have unique names

### Refactor

- Default null Scene/GameObject/Component names to the class name
- Convert UpdateOrder enum into record and remove unused constants
- Use framebuffer's resize callback over window's
- BufferedList uses CallbackQueue internally
- Delete SceneEvent classes and use CallbackQueue in SceneManager
- BREAKING CHANGE: Replace Renderable isEnabled method with isVisible

### Fixes

- Make sure fixed TPS isn't zero
- Don't use fullscreen vidMode size if windowed

### Build

- Update Gradle version to 9.5.0

### Test

- Add SceneManager tests

### Documentation

- Add JSpecify and NullAway for static null safety analysis
- Null-mark mayonez.application package
- Null-mark Scene/GameObject/Component class

## v0.8.3-pre4

Scene events and UI containers

### Features

- AWT window resizes content pane to preferred size
- Add SceneEvent class for scene stopping
- Execute SceneManager event callbacks after updating
- Set slot size and spacing for space game weapon hotbar
- Create UIContainer abstract class to manage child UIElement positions
- Create BoxContainer subclass of UIContainer
- Create PlayerStatus BoxContainer subclass to space player health and shield elements
- WeaponHotbar class extends BoxContainer
- Convert UIElement interface into Script subclass

### Refactor

- Don't start first scene before window
- Unify single SceneManager changeScene function with parameters
- Finish updating scene current before stopping
- Merge UIRenderableElement interface into UISprite class
- Tweak GameObject/Component toString method

### Fixes

- Fix infinite recursion in UIContainer

### Chores

- Reset script file permissions

## v0.8.3-pre3

Window current size and new parser

### Features

- Camera stores windowScale instead of WindowProperties class
- Don't use GLFW_SCALE_TO_MONITOR window hint
- Add Window getContentScale method
- Store Window singleton in Mayonez class
- Add window width/height/fullscreen delegators to Mayonez
- Initialize demo Scenes with current window size
- Create window resizable user preference
- Add Window getContentWidth/Height method for working content area
- AWT window fullscreen selects nearest resolution to user preference
- AWT and GL window stores optional resolutions
- Log Window creation events
- Add OpenGL fallback version flag to ArgumentsParser
- Initialize Window with RunConfig

### Refactor

- Delete WindowProperties class
- Rewrite ArgumentsParser using Apache Commons CLI
- Launcher constructor now sets RunConfig

### Build

- Update Gradle version to 9.2.0
- Update JUnit version to 6.0.0
- Update Kotlin version to 2.2.20
- Update LWJGL version to 3.3.6
- Update Gradle workflow task versions
- Update Dokka plugin to 2.1.0
- Add Apache Commons CLI dependency

### Documentation

- Update instructions and dependencies in readme

## v0.8.3-pre2

Parse shaders from multiple files

### Features

- Free ShaderStages after linking program
- Combine shader view and projection uniforms
- Add printGLInfo function to debug OpenGL version
- Create ShaderParser utility class
- Split shaders without removing delimiters
- Parse shader version headers using regex
- Parse shaders with extra whitespace
- Read shaders from multiple source files

### Refactor

- Rename ShaderProgram class to ShaderStage (represents OpenGL shader)
- Rename Shader shaderID field to programID (represents OpenGL program)
- Split default.glsl into default.vert and default.frag files
- Draw UI sprites using default shader

### Fixes

- Use switch statements in fragment shaders to sample texturej
- Fix incorrect uniform name in debug shader

### Build

- Require OpenGL version 4.0 with 3.3 fallback

### Test

- Test reading individual shader stages

## v0.8.3-pre1

Full screen and resizable window

### Features

- Add Window setFullscreen method
- Add Window resize callback
- Restore window position and size after exiting fullscreen
- Create GLFW getWindowPosition method
- Broadcast Window resize events in WindowEvents class
- Update camera screen size and projection matrix on window resize
- Update glViewport on window resize
- Add fullscreen user preference
- Center GLWindow in monitor usable work area
- Create WindowConfig record
- AWT window uses user preference size

### Refactor

- Delete GLApplication and JApplication subclasses
- Move getCurrentTimeSecs method to Window interface
- AWT Window always flushes resources

### Fixes

- Fix AWT SpriteSheets being created with GLTextures

### Build

- Create Gradle runGl and runAwt tasks
- Merge buildSrc Plugins.kt and Versions.kt into Dependencies.kt