package main;

import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Shader;
import engine.graphics.Vertex;
import engine.io.Window;
import engine.maths.Vector3f;

public class Main implements Runnable {
    private Thread gameThread;
    private Window window;

    public Mesh mesh = new Mesh(new Vertex[] {
            new Vertex(new Vector3f(-0.5f, 0.5f, 0.0f)),
            new Vertex(new Vector3f(-0.5f, -0.5f, 0.0f)),
            new Vertex(new Vector3f(0.5f, -0.5f, 0.0f)),
            new Vertex(new Vector3f(0.5f, 0.5f, 0.0f))
    }, new int[] {
            0, 1, 2,
            0, 3, 2
    });

    public Renderer renderer;
    public Shader shader;

    private void start() {
        gameThread = new Thread(this,"gameThread");
        gameThread.start();
    }

    private void init() {
        final int WIDTH = 1280;
        final int HEIGHT = 760;
        window = new Window(WIDTH, HEIGHT, "Game Tutorial");
        shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");
        renderer = new Renderer(shader);
        window.setBackgroundColor(1.0f, 0, 0);
        window.create();
        mesh.create();
        shader.create();
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
        renderer.renderMesh(mesh);
        window.swapBuffers();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
