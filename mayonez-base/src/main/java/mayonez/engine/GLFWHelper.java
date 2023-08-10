package mayonez.engine;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.math.*;
import mayonez.util.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Contains methods to assist in GLFW window creation.
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
        GLFWErrorCallback.createPrint(System.err).set(); // Setup error callback
        if (!glfwInit()) {
            if (OperatingSystem.getCurrentOS() == OperatingSystem.MAC_OS) {
                Logger.error("GLFW must run with the \"-XstartOnFirstThread\" VM argument on macOS");
            }
            throw new GLFWException("Unable to initialize GLFW");
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
        configureWindowSettings();
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
    private static void configureWindowSettings() {
        glfwDefaultWindowHints(); // Reset window settings
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Don't stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // Don't allow resizing
        // Set proper version for macOS
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        // Scale screen properly for Windows
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);
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
        Mayonez.setWindowScale(contentScale.mul(ratio));
        Logger.debug("Window scale has been set to %s", Mayonez.getWindowScale());
    }

    /**
     * How much the display has been scaled by.
     *
     * @param windowID the GLFW window pointer
     * @return the content scale
     */
    private static Vec2 getWindowContentScaling(long windowID) {
        var xScaleBuff = BufferUtils.createFloatBuffer(1);
        var yScaleBuff = BufferUtils.createFloatBuffer(2);

        glfwGetWindowContentScale(windowID, xScaleBuff, yScaleBuff);
        var xContentScale = xScaleBuff.get(0);
        var yContentScale = yScaleBuff.get(0);
//        Logger.debug("Window scaling is %.2fx%.2f", xContentScale, yContentScale);
        return new Vec2(xContentScale, yContentScale);
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
//        Logger.debug("Window size is %dx%d", xWindowSize, yWindowSize);

        // Size of the rendered image in the display's native scale
        glfwGetFramebufferSize(windowID, xIntBuff, yIntBuff);
        var xFramebuffSize = xIntBuff.get(0);
        var yFramebuffSize = yIntBuff.get(0);
//        Logger.debug("Framebuffer size is %dx%d", xFramebuffSize, yFramebuffSize);

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
