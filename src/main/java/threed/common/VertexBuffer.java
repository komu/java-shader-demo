package threed.common;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.*;

public final class VertexBuffer {
    
    private final int id;

    public VertexBuffer(float[] data) {
        id = glGenBuffers();

        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        for (float v : data)
            buffer.put(v);
        buffer.rewind();

        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, id);
        glVertexPointer(2, GL_FLOAT, 4*4, 0);
    }

    public void delete() {
        glDeleteBuffers(id);
    }
}
