package main;

import engine.io.Window;

public class Main implements Runnable {
    private Thread gameThread;
    private Window window;

    private void start() {
        gameThread = new Thread(this,"gameThread");
        gameThread.start();
    }

    private void init() {
        final int WIDTH = 1280;
        final int HEIGHT = 760;
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
