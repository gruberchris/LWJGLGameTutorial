package main;

import engine.io.Window;

public class Main implements Runnable {
    private Thread gameThread;
    public Window window;
    public final int WIDTH = 1280, HEIGHT = 760;

    public void start() {
        gameThread = new Thread(this,"gameThread");
        gameThread.start();
    }

    private void init() {
        window = new Window(WIDTH, HEIGHT, "Game Tutorial");
        window.create();
    }

    public void run() {
        init();

        while(!window.shouldClose()) {
            // Game loop
            update();
            render();
        }

        // Game is shutting down
        window.destroy();
    }

    private void update() {
        window.update();
    }

    private void render() {
        window.swapBUffers();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
