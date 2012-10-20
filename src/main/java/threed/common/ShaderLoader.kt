package threed.common

import threed.utils.ResourceUtils

import java.io.*

import org.lwjgl.opengl.GL20.*

object ShaderLoader {

    fun createShaderProgram(name: String): ShaderProgram {
        val program = glCreateProgram()
        
        glAttachShader(program, createShader("shaders/$name.v.glsl", GL_VERTEX_SHADER))
        glAttachShader(program, createShader("shaders/$name.f.glsl", GL_FRAGMENT_SHADER))
        glLinkProgram(program)
        glValidateProgram(program)

        val log = glGetProgramInfoLog(program, 1024)!!
        if (!log.isEmpty())
            throw RuntimeException("Invalid program: $log")

        return ShaderProgram(program)
    }

    private fun createShader(filename: String, shaderType: Int): Int {
        val source = ResourceUtils.loadStringResource(filename, "UTF-8")
        val  shader = glCreateShader(shaderType)

        glShaderSource(shader, source)
        glCompileShader(shader)
        
        val log = glGetShaderInfoLog(shader, 1024)!!
        if (!log.isEmpty())
            throw RuntimeException("Invalid shader: $log")

        return shader
    }
}
