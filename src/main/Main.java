package main;

import engine.io.Window;

public class Main implements Runnable {
    public Thread gameThread;
    public Window window;
    public final int WIDTH = 1280, HEIGHT = 760;

    public void start() {
        gameThread = new Thread(this,"gameThread");
        gameThread.start();
    }

    public void init() {
        window = new Window(WIDTH, HEIGHT, "Game Tutorial");
        window.create();
    }

    public void run() {
        init();

        while(!window.shouldClose()) {
            update();
            render();
        }
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
