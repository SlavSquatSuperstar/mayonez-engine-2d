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

## v0.8.2

Collision fixes, better shaders and sheets

See pre-release tags for release notes

## v0.8.2-pre11

Easier text label styling

### Features

- Merge TextLabel subclasses into one class
- Store default font asset in Fonts singleton
- Create TextLabel constructor with default style attributes
- Text label stores dirty flags
- Add builder-style setter functions to TextLabel
- Create TextStyle record

### Test

- Write more UIBounds tests

## v0.8.2-pre10

Better Lines and Font Sizes

### Features

- Cut polygon line corners when drawing
- Create VertexPair struct to avoid duplicating edge points
- Find polygon faces through edge intersections
- Line up glyph baselines based on assent
- Increase font ascender/descender height
- Replace glyph ascent with baseline height
- Specify font line spacing in units of line height
- Increase pixel font glyph height
- Regenerate text glyphs when setting TextLabel position

### Refactor

- Remove OpenGL Line DrawPrimitive
- Draw debug lines as quads

### Test

- JUnit tests use Kotlin contracts

### Build

- Update JUnit version to 5.12.x
- Update Kotlin version to 2.1.x

### Documentation

- Update readme development tool versions

## v0.8.2-pre9

Font defines multiple character blocks

### Features

- Create SpriteSplitters factory class
- Font can split glyphs for AWT Textures
- FontMetadata stores max glyph width
- Create record FontBlockMetadata
- Add font texture for Latin-1 Supplement code block
- Specify glyph height and sprite height metadata separately
- Read font blocks from file
- Merge FontBlock and FontBlockMetadata classes

### Refactor

- Font class uses SpriteSplitter to create glyph textures
- Combine GL/AWT SpriteSheet test classes
- Combine GL/AWT SpriteSheet subclasses
- Store Texture and SpriteSplitter in SpriteSheet classclass
- Move font width methods to new FontWidthHelper utility class
- Store separate whitespace glyph in font
- Store font glyphs in map
- Create and store glyphs per FontBlock
- Store texture in font block
- Merge SpriteSheetTexture subclasses into Texture subclasses

### Fixes

- Draw glyphs with different height correctly
- ImageData getPixel method checks found and freed status

### Test

- Create more utility methods for SpriteSheet tests
- Test reading Records from JSON Arrays

## v0.8.2-pre8

Sprite sheet splitting and regions

### Features

- Group RenderBatches by shader before draw
- Don't fire spaceship thrusters when zeroing velocity
- Zero spaceship velocity using local reference frame
- Draw space game BackgroundStars using only circles
- Specify font texture file in metadata
- Create ImageRegion class to define a rectangular sub-region
- Create SpriteSheet sub sprites using ImageRegion
- Use generic texture for Font
- Create SpriteSheet getSheetTexture and getRegions methods
- Create SpriteSplitter strategy class for GL and AWT sheets

### Refactor

- Get SpriteSheet regions in one loop

### Fixes

- Crop GL SpriteSheet sub-image buffers correctly
- Avoid STB ImageData VM segfault after free

### Test

- Reorganize image test classes and assets
- Create AWT SpriteSheet test cases
- Write tests for SpriteSheet getRegions and spacing

### Documentation

- Add dynamic build status badge to readme

## v0.8.2-pre7

Fast circles and shader per batch

### Features

- Create PrefabUtils class for demo scenes
- Store system line separator in text I/O class
- Store Shader and upload uniforms in batch
- Update Camera matrices only when accessed
- Renderer allows using multiple shaders
- Draw circles/ellipses as quads using fragment shader
- Draw circle/ellipse outlines using shader
- Create GLShapeBatchHelpers utility class
- ShapeSprite allows changing shape and stroke

### Refactor

- Move RandomVariable class to mayonez.math package
- Infer fill from stroke in circle shader
- Rename VertexAttribute constants
- Refactor GLDebugShapeHelpers methods

### Fixes

- Fix line separators in CSV file tests
- Fix incorrect asteroids sprite sheet size

### Test

- Create scene for measuring circle draw FPS
- Write tests for RandomVariable class
- Write tests for drawn circle conversion

## v0.8.2-pre6

New CSV backend and set record types

### Features

- Allow setting z-index for sprite
- Implement Record equals function
- Add more Record number conversions
- Create RandomVariable helper class
- Create spaceship shield damaged effect
- Set star brightness and radius from luminosity
- Define star spectral types per luminosity class
- Add constants to pool balls scene

### Refactor

- Create spacegame.objects.stars package for classes and assets
- Record stores arrays as Lists and maps as Records
- Read and write CSV files with Apache Commons-CSV

### Fixes

- Fix space scene object spawn probabilities

### Build

- Add Apache Commons-CSV dependency

### Test

- Move tests gitignore to output files folder
- Rewrite Record tests

### Documentation

- Update Record Javadoc

## v0.8.2-pre5

Projectile impacts and small asteroids

### Features

- Spawn projectile impact particles
- Pass contact points in CollisionEvent
- Create ParticleFollowTarget script class
- Add hitbox sweep factor field to ProjectType
- Update fighter texture
- Store satellite properties in data files
- Create PrefabUtils helper class
- Create Animator setSpriteColor function
- Create ShipPrefabs, AsteroidPrefabs factory classes
- Create small asteroid fragment textures
- Add medium asteroid fragment textures

### Refactor

- Store ship data files in separate folder
- Reorganize asteroid classes

## v0.8.2-pre4

SceneManager scene index

### Features

- BREAKING CHANGE: Rename SceneManager change scene functions
- BREAKING CHANGE: Pass scene in changeScene function instead of scene name
- Add SceneManager get scene by index function
- Read demo scenes from CSV file
- Add more SceneManager log messages
- Launcher preloads scenes as list
- Create DemosConfig class to read user config constants
- Add value not null preference validator

### Documentation

- Remove legacy/debug scenes from readme files

## v0.8.2-pre3

Bullet collider to mitigate tunneling

### Features

- Create BulletBoxCollider class with sweep factor to mitigate tunneling

### Refactor

- Move PolygonCollider test-only methods to test module

### Test

- Create projectile test scene with controllable target and launcher

## v0.8.2-pre2

Read app version from file

### Features

- Pass window parameters to ApplicationFactory methods
- Logger only writes to log file if level high enough
- Add trace and fatal LogLevels
- Create FloatValidator for floating-point preferences
- Add double-click time user preference
- Read application name and version from resources

### Refactor

- Combine version user preference into window title
- Tweak Application and Window log message levels
- Store LogLevel user preference as a string

### Build

- Create Gradle copyDefaultPreferences task to force copy preference files

## v0.8.2-pre1

Fix incorrect impulse resolution

### Features

- Input detects mouse displacement even if not dragging mouse
- Fix MaterialData struct having wrong parameters

### Refactor

- Reorganize sandbox object factory methods
- Clean up collision solver code

### Test

- Create InputTestScene for key and mouse input
- Create CollisionTestScene for collision detection and resolution

## v0.8.1

Better batching, packaging, less modules

See pre-release tags for release notes

### Documentation

- Reformat readme acknowledgements
- Update readme OS table headers
- Update readme Update engine feature list

## v0.8.1-pre9

Data-driven spaceships

### Features

- Read weapon hardpoints from file
- Randomly select enemy hardpoint locations and weapon loadout
- Create ThrusterProperties record
- Create Animator setSpriteTransform method
- Store thrusters/hardpoints in SpaceshipProperties
- Store spaceship properties in SpaceshipPrefabs singleton
- Add fighter spaceship type
- Add railgun projectile type
- Pass player loadout to weapons script
- Pass player loadout to UI hotbar
- Read SpaceshipProperties from CSV file
- Store speed, shield regen, and textures in SpaceshipProperties
- Save auto-brake option after death

### Refactor

- Rename spaceship assets to shuttle
- Tweak shuttle hardpoint positions

## v0.8.1-pre8

No more scene background

### Features

- Draw scene backgrounds with normal sprite
- BREAKING CHANGE: Remove size from scene constructor
- Save player spaceship weapon choice after death
- Spaceship now brakes with force instead of drag
- Clamp player spaceship velocity when braking if speed very small
- Lower player spaceship brake strength when thrust keys pressed
- Add control to toggle player spaceship auto-brake
- Separate enemy spaceship move and turn braking
- Separate player spaceship horizontal and vertical braking
- Enemy spaceships can now speed up and slow down
- Add auto-brake indicator to space game UI

### Refactor

- Change renderer background sprite to background color
- Move Scene backgroundColor field to viewport
- Create MouseFlick flickGameObject abstract method define movement
- Delete old movement and rotation scripts
- BREAKING CHANGE: Delete MovementScript.MoveMode enum and replace with overrides
- Move spaceship brake logic to player movement script

### Build

- Run Gradle compileKotln and copy Kotlin sources before running compileJava

### Fixes

- Fix spaceship rotation direction signs

### Documentation

- Update readme controls

## v0.8.1-pre7

Handle key/mouse input with events

### Features

- Create SandboxObjectPrefabs factory class
- Add more control/UI hints for space game scene
- Send events for space game weapon selection
- Create Key/MouseInput isAnyDown method
- Input stores mapping states and mappings held separately
- Added NONE constant for InputState to differentiate from released
- Implement GLFW double-click
- Create Key/MouseInputManager interfaces for GLFW and AWT to implement
- Create Key/MouseInputEvent classes for managers to send to singleton
- MouseInputEvent subclasses track click times

### Refactor

- Create separate classes for physics sandbox scripts
- Deleted MappingStatus class
- Renamed MouseInput getClicks method to isDoubleClick
- Store key/mouse states in Key/MouseInput singletons
- Delete Key/MouseManager classes
- Move AWT/GLFW InputManagers to mayonez.application package
- Merge mayonez.input.keyboard/mouse packages into mayonez.input

## v0.8.1-pre6

Remove scene size and scale

### Features

- Create Camera addCameraScript to attach custom scripts
- Add PRE_RENDER UpdateOrder constant
- Collider broadcasts CollisionEvents to subscribed listeners
- Add walls to physics demos scenes
- BREAKING CHANGE: Delete Scene width and height getters
- Create BackgroundStar class
- Delete Scene getRandomPosition method
- BREAKING CHANGE: Delete Scene size and scale fields

### Refactor

- Make explosion texture more realistic
- BREAKING CHANGE: Delete CameraMode enum and replace with overrides
- BREAKING CHANGE: Move GameObject onCollisionEvent callback to CollisionBody
- Create SpaceGameEvent superclass
- Move Script init and onDestroy methods to Component

### Build

- Update Gradle version to 8.12

## v0.8.1-pre5

Move pixel scale to camera

### Features

- Created Scene getHalfSize method
- Space scene stars use color temperatures
- Create polygons LOD for space scene background stars
- Create ElementLayout order to store element count
- PointTransformer converts mouse displacement from screen to world units
- Debug line segment width is automatically adjusted with zoom
- Debug circle resolution is automatically adjusted with zoom
- Ellipse getPolygon function now takes numEdges parameter
- Decrease asteroid debris size quadratically
- Cache shader uniform locations on first upload

### Refactor

- Pass texture slots to RenderBatch constructor
- Combine font and render test scenes
- Delete unsupported alternate line styles
- Replace Camera sceneScale field with cameraScale to specify pixels per unit
- Remove sceneScale field from DebugDraw

### Fixes

- Debug shapes scale correctly to camera scale
- Mouse position converts correctly to camera scale
- Background sprite now follows camera scale
- AWT shapes are drawn with the correct stroke size
- Use Path2D to draw AWT polygons with greater precision

### Build

- Update Gradle version to 8.11

## v0.8.1-pre4

Multi z-index render batches

### Features

- External file path checks if parent folder exists before writing
- Created GLQuad interface to store common sprite pushToBatch method
- GLQuad has default override for GLRenderable methods
- Create Texture getParentTexture method for query parent dimensions
- Shared draw list for sprites and text
- Create RenderBatch canFitObject and getSortOrder methods
- Create SingleZRenderBatch class to store one z-index
- RenderBatch superclass no longer stores z-index
- Create MultiZRenderBatch class to draw objects in a range of z-indices
- GLRenderer now divides sprites into MultiZRenderBatches to save on draw calls
- Plain color no longer takes up the first slot in TextureArray
- GLRenderer constructor now takes a Shader object instead of filename

### Refactor

- GLSprite, UISprite, and GlyphSprite classes now implement GLQuad
- Texture getWidth/Height methods now return sub-sprite dimensions
- Merged BatchPushHelper file into GLQuad
- GLRenderer now adds sprites and text objects to one list
- GLRenderer now splits objects during batching instead of after adding
- Make space game projectile colliders boxes instead of ovals

### Fixes

- Fix incorrect texID uploaded when TextureArray full due to off-by-one-error

### Build

- Don't export renderer.batch package as API
- Created "Package" run configuration to package all releases

### Test

- Created RenderTestScene scene to test render batch changes

## v0.8.1-pre3

Merge tools module, io package

### Features

- Created PathUtil class to store file separators convert path strings
- Create FilePath openInput/OutputStream methods
- Added autoClose property to TextFile class to close streams after writes
- Logger TextFile now keeps stream open to improve write performance

### Refactor

- Replaced OperatingSystem getFilename methods with PathUtil methods
- Move OperatingSystem enum to mayonez.util package
- Move mayonez.math/io/util packages back to mayonez-base module
- Delete mayonez-tools module
- Replace LocationType enum with FilePath subclasses
- Asset subclasses now close streams instead of IOManagers
- Replace TextIOManager classes with unified TextIOUtils
- Renamed ImageIOManager class to BinaryIOUtils
- Deleted AssetReader/Writer interfaces
- Deleted mayonez.io.image package
- Merge mayonez.io packages into mayonez.assets

### Test

- Deleted OperatingSystem tests
- Write tests for LocationType class
- Clean up I/O test classes

## v0.8.1-pre2

Package releases for each OS

### Refactor

- Move user run scripts from dist folder to release-assets/scripts
- Move user readme and configs from dist folder to release-assets/resources
- Move release scripts into separate folders for each OS
- Split Mac and Linux run scripts into separate files
- Move LWJGL natives method into Natives object
- Delete package.X scripts since Gradle package tasks are working

### Build

- Create Gradle copyDefaultPreferences task that runs if demos configs are missing
- Create Gradle jarX tasks for compiling with correct OS natives
- Create Gradle packageX tasks for copying release assets
- Create Gradle zipX tasks to create release archive
- clearlogs.X scripts now delete log files within dist and mayonez-demos subdirectories

### Chores

- Demos config files are now ignored
- The whole dist/ folder is now ignored

## v0.8.1-pre1

Detect wrong thread used on macOS

### Features

- Create EngineInitException class to detect wrong JVM configuration
- Create JVMHelper to check if -XstartOnFirstThread is enabled
- EngineFactory now throws a fatal exception due to wrong -XstartOnFirstThread on macOS

### Refactor

- Fixed some code inspection issues for new language versions
- GameEngine subclasses constructors now take a Window instance
- Pass input instances to EngineFactory createEngine method
- Make Key/MouseInput getInstance methods internal
- Pass window properties to Engine Factory createWindow method
- Rename mayonez.engine package to mayonez.application
- Rename GameEngine-related classes to Application
- Merge EngineInitException and GLFWException classes into WindowInitException

### Build

- Update Java version to 21
- Update Kotlin version to 2.0.x
- Update LWJGL version to 3.3.4 (no longer supports macOS versions <10.11)

### Test

- Added extra tests to increase code coverage