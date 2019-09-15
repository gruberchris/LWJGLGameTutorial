package engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class Window {
    private int width, height;
    private String title;
    private long window;
    public int frames;
    public long time;
    public Input input;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void create() {
        if (!GLFW.glfwInit()) {
            System.err.println("ERROR: GLFW wasn't initialized");
            return;
        }

        input = new Input();

        window = GLFW.glfwCreateWindow(width, height, title, 0, 0);

        if (window == 0) {
            System.err.println("ERROR: window wasn't created");
            return;
        }

        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

        assert videoMode != null;

        GLFW.glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        GLFW.glfwMakeContextCurrent(window);

        GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
        GLFW.glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
        GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());

        GLFW.glfwShowWindow(window);

        // Setting the value to 1 should limit to 60 FPS
        GLFW.glfwSwapInterval(1);

        time = System.currentTimeMillis();
    }

    public void update() {
        GLFW.glfwPollEvents();

        frames++;

        if (System.currentTimeMillis() > time + 1000) {
            GLFW.glfwSetWindowTitle(window, title + " | FPS: " + frames);
            time = System.currentTimeMillis();
            frames = 0;
        }

        if (input.isButtonDown((GLFW.GLFW_MOUSE_BUTTON_LEFT))) {
            // NOTE: prints 1 message for each N frames rendered while button is pressed down
            System.out.println("X: " + input.getMouseX() + ", Y: " + input.getMouseY());
        }
    }

    public void swapBUffers() {
        GLFW.glfwSwapBuffers(window);
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(window) || input.isKeyDown(GLFW.GLFW_KEY_ESCAPE);
    }

    public void destroy() {
        input.destroy();
        GLFW.glfwWindowShouldClose(window);
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
}
