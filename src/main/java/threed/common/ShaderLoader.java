package threed.common;

import threed.utils.ResourceUtils;

import java.io.*;

import static org.lwjgl.opengl.GL20.*;

public class ShaderLoader {
    
    public static ShaderProgram createShaderProgram(String name) throws IOException {
        int program = glCreateProgram();
        
        glAttachShader(program, createShader("shaders/" + name + ".v.glsl", GL_VERTEX_SHADER));
        glAttachShader(program, createShader("shaders/" + name + ".f.glsl", GL_FRAGMENT_SHADER));
        glLinkProgram(program);
        glValidateProgram(program);

        String log = glGetProgramInfoLog(program, 1024);
        if (!log.isEmpty())
            throw new RuntimeException("Invalid program: " + log);

        return new ShaderProgram(program);
    }

    private static int createShader(String filename, int type) throws IOException {
        String source = ResourceUtils.loadStringResource(filename, "UTF-8");
        int shader = glCreateShader(type);

        glShaderSource(shader, source);
        glCompileShader(shader);
        
        String log = glGetShaderInfoLog(shader, 1024);
        if (!log.isEmpty())
            throw new RuntimeException("Invalid shader: " + log);

        return shader;
    }
}
