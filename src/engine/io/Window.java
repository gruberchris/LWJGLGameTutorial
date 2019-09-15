package engine.io;

import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long window;
    private int frames;
    private long time;
    private Input input;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void create() {
        if (!glfwInit()) {
            System.err.println("ERROR: GLFW wasn't initialized");
            return;
        }

        input = new Input();

        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        window = glfwCreateWindow(width, height, title, NULL, NULL);

        if (window == NULL) {
            System.err.println("ERROR: window wasn't created");
            return;
        }

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        assert videoMode != null;

        glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        glfwMakeContextCurrent(window);

        glfwSetKeyCallback(window, input.getKeyboardCallback());
        glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
        glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());

        glfwShowWindow(window);

        // Setting the value to 1 should limit to 60 FPS
        glfwSwapInterval(1);

        time = System.currentTimeMillis();
    }

    public void update() {
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
    }

    public void swapBUffers() {
        glfwSwapBuffers(window);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window) || input.isKeyDown(GLFW_KEY_ESCAPE);
    }

    public void destroy() {
        input.destroy();
        glfwWindowShouldClose(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}
