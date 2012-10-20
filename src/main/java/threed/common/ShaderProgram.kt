package threed.common

import org.lwjgl.opengl.GL20.*

class ShaderProgram(val id: Int) {

    fun bind() {
        glUseProgram(id)
    }

    fun set(name: String, value: Float) {
        val location = glGetUniformLocation(id, name)
        if (location != -1)
            glUniform1f(location, value)
    }

    fun set(name: String, value: Triple<Float, Float, Float>) {
        val location = glGetUniformLocation(id, name)
        if (location != -1)
            glUniform3f(location, value.first, value.second, value.third)
    }

    fun set(name: String, p: AttributePointer) {
        val index = glGetAttribLocation(id, name)
        if (index != -1) {
            glVertexAttribPointer(index, p.size, p.typ, p.normalized, p.stride, p.offset)
            glEnableVertexAttribArray(index)
        }
    }
}

class AttributePointer(val size: Int, val typ: Int, val normalized: Boolean, val stride: Int, val offset: Long)
