package threed;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import threed.common.IndexBuffer;
import threed.common.ShaderLoader;
import threed.common.ShaderProgram;
import threed.common.VertexBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

public class CloverDemo {

    VertexBuffer vertexBuffer;
    IndexBuffer indexBuffer;
    ShaderProgram shader;
    DisplayMode mode;
    
    private void execute() throws Exception {
        initialize();
        mainLoop();
        cleanup();
    }

    private void cleanup() {
        vertexBuffer.delete();
        indexBuffer.delete();
        Display.destroy();
    }
    
    private void initialize() throws Exception {
        mode = Display.getDisplayMode();
        Display.setDisplayModeAndFullscreen(mode);
        Display.create();
        Keyboard.create();

        initGl();
    }

    private void initGl() throws Exception {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluOrtho2D(0, mode.getWidth(), 0, mode.getHeight());

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glViewport(0, 0, mode.getWidth(), mode.getHeight());

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Display.setVSyncEnabled(true);

        glShadeModel(GL_SMOOTH);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        vertexBuffer = new VertexBuffer(new float[] { -1, -1, 1, -1, 1, 1, -1, 1 });
        indexBuffer = new IndexBuffer(new int[] { 0, 1, 2, 3 });

        shader = ShaderLoader.createShaderProgram("clover");
    }

    private void mainLoop() {
        long frames = 0;
        long start = System.currentTimeMillis();

        while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Display.isCloseRequested()) {
            frames++;
            render();
            Display.update();
        }
        
        long total = System.currentTimeMillis() - start;

        float seconds = total/1000f;
        System.out.println("fps: " + (int) (frames/seconds));
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shader.bind();
        shader.setUniform("time", System.currentTimeMillis() % 1000000 / 1000f);
		glPushMatrix();
        glScalef(2000f, 2000f, 1f);

        glEnableClientState(GL_VERTEX_ARRAY);

        vertexBuffer.bind();

        indexBuffer.bind();
        glDrawElements(GL_QUADS, 4, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glDisableClientState(GL_VERTEX_ARRAY);
        glPopMatrix();
        glUseProgram(0);
    }

    public static void main(String[] args) {
        try {
            new CloverDemo().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
