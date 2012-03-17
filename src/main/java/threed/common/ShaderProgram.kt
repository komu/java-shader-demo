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

    fun set(name: String, value: #(Float, Float, Float)) {
        val location = glGetUniformLocation(id, name)
        if (location != -1)
            glUniform3f(location, value._1, value._2, value._3)
    }

    fun setUniform3f(name: String, x: Float, y: Float, z: Float) {
        val location = glGetUniformLocation(id, name)
        if (location != -1)
            glUniform3f(location, x, y, z)
    }

    fun setVertexAttribPointer(name: String, size: Int, typ: Int, normalized: Boolean, stride: Int, offset: Long) {
        val index = glGetAttribLocation(id, name)
        if (index != -1) {
            glVertexAttribPointer(index, size, typ, normalized, stride, offset)
            glEnableVertexAttribArray(index)
        }
    }
}
