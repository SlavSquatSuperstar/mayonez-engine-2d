package mayonez.engine;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.io.*;
import mayonez.math.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWErrorCallback.createPrint;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Assists in GLFW window creation for the OpenGL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
final class GLFWHelper {

    private GLFWHelper() {
    }

    /**
     * Initializes the GLFW library.
     */
    static void initGLFW() throws GLFWException {
        createPrint(System.err).set(); // Setup error callback
        try {
            if (!glfwInit()) {
                throw new GLFWException("Unable to initialize GLFW");
            }
        } catch (IllegalStateException e) {
            Logger.error("GLFW was not initialized on the main thread");
            if (OperatingSystem.getCurrentOS() == OperatingSystem.MAC_OS) {
                Logger.error("Java must run with the \"-XstartOnFirstThread\" VM argument on macOS");
            }
            throw new GLFWException("Aborting GLFW initialization because \"XstartOnFirstThread\" was not enabled");
        }
    }

    /**
     * Create a new window and return its GLFW pointer.
     *
     * @param width  the window's width
     * @param height the window's height
     * @param title  the window's title
     * @return the window id
     */
    static long createGLFWWindow(int width, int height, String title) throws GLFWException {
        configureWindowHints();
        var windowID = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowID == NULL) {
            throw new GLFWException("Could not create the GLFW window");
        }
        setWindowScale(windowID);
        setWindowPosition(windowID);
        return windowID;
    }

    /**
     * Set the GLFW window hints for the application window.
     */
    private static void configureWindowHints() {
        glfwDefaultWindowHints(); // Reset window settings
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Don't stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // Don't allow resizing
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE); // Scale screen properly for Windows

        // Set GLFW context version to 4.0 core
        // macOS only supports OpenGL versions 3.2-4.1, inclusive
        // Source: https://www.glfw.org/docs/latest/window.html
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
    }

    /**
     * Set the application's window scale parameter.
     *
     * @param windowID the GLFW window pointer
     */
    private static void setWindowScale(long windowID) {
        // Source: https://github.com/glfw/glfw/issues/845
        var contentScale = getWindowContentScaling(windowID);
        var ratio = getWindowFramebufferRatio(windowID);
        WindowProperties.setWindowScaling(contentScale.mul(ratio));
        Logger.debug("Window scale has been set to %s", WindowProperties.getWindowScaling());
    }

    /**
     * How much the display has been scaled by.
     *
     * @param windowID the GLFW window pointer
     * @return the content scale
     */
    private static Vec2 getWindowContentScaling(long windowID) {
        var xScale = BufferUtils.createFloatBuffer(1);
        var yScale = BufferUtils.createFloatBuffer(2);
        glfwGetWindowContentScale(windowID, xScale, yScale);
        return new Vec2(xScale.get(0), yScale.get(0));
    }

    /**
     * The ratio between the window size and the framebuffer (rendered image) size.
     * On Windows, the ratio should equal 1, and on Unix, the ratio should be
     * the reciprocal of the content scale.
     *
     * @param windowID the GLFW window pointer
     * @return the ratio
     */
    private static Vec2 getWindowFramebufferRatio(long windowID) {
        var xIntBuff = BufferUtils.createIntBuffer(1);
        var yIntBuff = BufferUtils.createIntBuffer(1);

        // Size of the actual image in the display's apparent scale
        glfwGetWindowSize(windowID, xIntBuff, yIntBuff);
        var xWindowSize = xIntBuff.get(0);
        var yWindowSize = yIntBuff.get(0);

        // Size of the rendered image in the display's native scale
        glfwGetFramebufferSize(windowID, xIntBuff, yIntBuff);
        var xFramebuffSize = xIntBuff.get(0);
        var yFramebuffSize = yIntBuff.get(0);

        return new Vec2((float) xWindowSize / xFramebuffSize,
                (float) yWindowSize / yFramebuffSize);
    }

    /**
     * Center the window inside the computer display.
     *
     * @param windowID the GLFW window pointer
     */
    private static void setWindowPosition(long windowID) throws GLFWException {
        // Push a new frame to the thread stack
        try (MemoryStack stack = stackPush()) {
            IntBuffer windowWidth = stack.mallocInt(1);
            IntBuffer windowHeight = stack.mallocInt(1);
            glfwGetWindowSize(windowID, windowWidth, windowHeight);

            GLFWVidMode screenResolution = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (screenResolution == null) {
                throw new GLFWException("Could not get the video mode");
            }
            glfwSetWindowPos(
                    windowID,
                    (screenResolution.width() - windowWidth.get(0)) / 2,
                    (screenResolution.height() - windowHeight.get(0)) / 2
            );
        } // Pop the stack frame automatically
    }

}
