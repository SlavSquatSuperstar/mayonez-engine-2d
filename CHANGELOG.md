# Mayonez Engine Changelog

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

- Updated Gradle verison to 9.7.0
- Use OpenGL 3.3 only and remove fallback option

### Documentation

- Null-mark mayonez.event package
