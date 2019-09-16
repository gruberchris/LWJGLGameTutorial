package engine.io;

import engine.maths.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long window;
    private int frames;
    private long time;
    private Input input;
    private Vector3f background;
    private GLFWWindowSizeCallback sizeCallback;
    private boolean isResized;
    private boolean isFullscreen;
    private int[] windowPosX, windowPosY;


    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;

        windowPosX = new int[1];
        windowPosY = new int[1];
        background = new Vector3f(0, 0, 0);
    }

    public void create() {
        if (!glfwInit()) {
            System.err.println("ERROR: GLFW wasn't initialized");
            return;
        }

        input = new Input();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        window = glfwCreateWindow(width, height, title, isFullscreen() ? glfwGetPrimaryMonitor() : NULL, NULL);

        if (window == NULL) {
            System.err.println("ERROR: window wasn't created");
            return;
        }

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        assert videoMode != null;

        windowPosX[0] = (videoMode.width() - width) / 2;
        windowPosY[0] = (videoMode.width() - width) / 2;
        glfwSetWindowPos(window, windowPosX[0], windowPosY[0]);
        glfwMakeContextCurrent(window);

        GL.createCapabilities();

        GL11.glEnable(GL11.GL_DEPTH_TEST);

        createCallbacks();

        glfwShowWindow(window);

        // Setting the value to 1 should limit to 60 FPS
        glfwSwapInterval(1);

        time = System.currentTimeMillis();
    }

    private void createCallbacks() {
        sizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;
                isResized = true;
            }
        };

        glfwSetKeyCallback(window, input.getKeyboardCallback());
        glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
        glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
        glfwSetScrollCallback(window, input.getMouseScrollCallback());
        glfwSetWindowSizeCallback(window, sizeCallback);
    }

    public void update() {
        if (isResized) {
            GL11.glViewport(0, 0, width, height);
            isResized = false;

            if (isFullscreen()) {
                glfwGetWindowPos(window, windowPosX, windowPosY);
                glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
            } else {
                glfwSetWindowMonitor(window, 0, windowPosX[0], windowPosY[0], width, height, 0);
            }
        }

        GL11.glClearColor(background.getX(), background.getY(), background.getZ(), 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        glfwPollEvents();

        frames++;

        if (System.currentTimeMillis() > time + 1000) {
            glfwSetWindowTitle(window, title + " | FPS: " + frames);
            time = System.currentTimeMillis();
            frames = 0;
        }

        if (input.isButtonDown((GLFW_MOUSE_BUTTON_LEFT))) {
            // NOTE: prints 1 message for each N frames rendered while button is pressed down
            System.out.println("X: " + input.getMouseX() + ", Y: " + input.getMouseY());
        }

        if (input.isKeyDown(GLFW_KEY_F11)) {
            setFullscreen(!isFullscreen());
        }
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window) || input.isKeyDown(GLFW_KEY_ESCAPE);
    }

    public void destroy() {
        input.destroy();
        sizeCallback.free();
        glfwWindowShouldClose(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public void setBackgroundColor(float r, float g, float b) {
        background.set(r, g, b);
    }

    public int getFrames() {
        return frames;
    }

    public long getTime() {
        return time;
    }

    public Input getInput() {
        return input;
    }

    public float getBackgroundR() {
        return background.getX();
    }

    public float getBackgroundG() {
        return background.getY();
    }

    public float getBackgroundB() {
        return background.getZ();
    }

    public GLFWWindowSizeCallback getSizeCallback() {
        return sizeCallback;
    }

    public boolean isResized() {
        return isResized;
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        isFullscreen = fullscreen;
        isResized = true;

        if (isFullscreen()) {
            glfwGetWindowPos(window, windowPosX, windowPosY);
            glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
        } else {
            glfwSetWindowMonitor(window, 0, windowPosX[0], windowPosY[0], width, height, 0);
        }
    }
}
