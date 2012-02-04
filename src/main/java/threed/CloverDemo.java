package threed;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import threed.common.IndexBuffer;
import threed.common.ShaderLoader;
import threed.common.ShaderProgram;
import threed.common.VertexBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

public class CloverDemo {

    VertexBuffer vertexBuffer;
    IndexBuffer indexBuffer;
    ShaderProgram shader;
    DisplayMode mode;
    float ratio = 1;
    
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

        updateModelView();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Display.setVSyncEnabled(true);

        glShadeModel(GL_SMOOTH);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        int w = mode.getWidth();
        int h = mode.getHeight();


        float[] vertices = {
            0, 0, 0, 0,
            w, 0, 1, 0,
            w, h, 1, 1,
            0, h, 0, 1
        };

        vertexBuffer = new VertexBuffer(vertices);
        indexBuffer = new IndexBuffer(new int[] { 0, 1, 2, 3 });

        shader = ShaderLoader.createShaderProgram("apple");
    }

    private void updateModelView() {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        float w = mode.getWidth()*ratio;
        float h = mode.getHeight()*ratio;
        float x = (mode.getWidth()/2) - w/2;
        float y = (mode.getHeight()/2) - h/2;

        glViewport((int) x, (int) y, (int) w, (int) h);
    }

    private void mainLoop() {
        long frames = 0;
        long start = System.currentTimeMillis();

        while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Display.isCloseRequested()) {
            processInput();
            frames++;
            render();
            Display.update();
        }
        
        long total = System.currentTimeMillis() - start;

        float seconds = total/1000f;
        System.out.println("fps: " + (int) (frames / seconds));
    }

    private void processInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            ratio -= 0.05;
            updateModelView();
        } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            ratio += 0.05;
            updateModelView();
        }
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glEnableClientState(GL_VERTEX_ARRAY);

        // bind buffers
        vertexBuffer.bind();
        indexBuffer.bind();

        // init shader
        shader.bind();
        shader.setUniform1f("time", System.currentTimeMillis() % 1000000 / 1000f);
        shader.setUniform3f("unResolution", mode.getWidth(), mode.getHeight(), 1);

        int vertexTextureCoordinate = glGetAttribLocation(shader.id, "vertexTextureCoordinate");
        glVertexAttribPointer(vertexTextureCoordinate, 2, GL_FLOAT, true, 4*4, 2*4);
        glEnableVertexAttribArray(vertexTextureCoordinate);

        // draw the stuff
        glDrawElements(GL_QUADS, 4, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glDisableClientState(GL_VERTEX_ARRAY);

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
