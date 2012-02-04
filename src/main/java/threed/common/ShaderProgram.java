package threed.common;

import static org.lwjgl.opengl.GL20.*;

public final class ShaderProgram {
    
    public final int id;

    public ShaderProgram(int id) {
        this.id = id;
    }

    public void bind() {
        glUseProgram(id);
    }

    public void setUniform1f(String name, float value) {
        int location = glGetUniformLocation(id, name);
        if (location != -1)
            glUniform1f(location, value);
    }

    public void setUniform3f(String name, float x, float y, float z) {
        int location = glGetUniformLocation(id, name);
        if (location != -1)
            glUniform3f(location, x, y, z);
    }
}
