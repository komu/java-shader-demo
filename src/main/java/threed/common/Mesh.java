package threed.common;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;

public final class Mesh {

    public final int size;
    private final int vertexBuffer;
    public static final int ENTRIES = 5;
    public static final int SIZE_IN_BYTES = ENTRIES*4;
    public static final int VERTEX_OFFSET = 0;
    public static final int TEX_COORD_OFFSET = 3*4;
    public static final int VERTEX_COUNT = 3;
    public static final int TEX_COORD_COUNT = 2;

    private Mesh(float[] data) {
        this.size = data.length / ENTRIES;
        vertexBuffer = createVertexBuffer(data);
    }

    private static int createVertexBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        for (float v : data)
            buffer.put(v);
        buffer.rewind();

        int id = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        return id;
    }

    public void delete() {
        glDeleteBuffers(vertexBuffer);
    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
    }
    
    public static Builder builder(int capacity) {
        return new Builder(capacity);
    }

    public void draw() {
        glDrawArrays(GL_QUADS, 0, size);
    }

    public static final class Builder {
    
        private int index = 0;
        private final float[] data;

        private Builder(int capacity) {
            data = new float[capacity * ENTRIES];
        }
    
        public Builder add(float x, float y, float z, float u, float t) {
            data[index++] = x;
            data[index++] = y;
            data[index++] = z;
            data[index++] = u;
            data[index++] = t;
            return this;
        }
        
        public Mesh build() {
            return new Mesh(data);
        }
    }
}
