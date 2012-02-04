package threed.common;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUseProgram;

public final class ShaderProgram {
    
    private final int id;

    public ShaderProgram(int id) {
        this.id = id;
    }

    public void bind() {
        glUseProgram(id);
    }

    public void setUniform(String name, float value) {
        glUniform1f(getUniformLocation(name), value);
    }
    
    private int getUniformLocation(String name) {
        int location = glGetUniformLocation(id, name);
        if (location != -1)
            return location;
        else
            throw new IllegalArgumentException("no such uniform parameter: '" + name + "'");
    }
}
