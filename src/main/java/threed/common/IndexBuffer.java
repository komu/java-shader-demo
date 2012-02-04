package threed.common;

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

public final class IndexBuffer {

    private final int id;

    public IndexBuffer(int[] data) {
        id = glGenBuffers();

        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        for (int v : data)
            buffer.put(v);
        buffer.rewind();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
    }

    public void delete() {
        glDeleteBuffers(id);
    }
}
