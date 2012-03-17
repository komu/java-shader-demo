package threed.common

import org.lwjgl.BufferUtils

import java.nio.FloatBuffer

import org.lwjgl.opengl.GL11.GL_QUADS
import org.lwjgl.opengl.GL11.glDrawArrays
import org.lwjgl.opengl.GL15.*

class Mesh(data: FloatArray) {

    val size = data.size / ENTRIES
    private val vertexBuffer = createVertexBuffer(data)

    class object {
        public val ENTRIES: Int = 5
        public val SIZE_IN_BYTES: Int = 20
        public val VERTEX_OFFSET: Long = 0
        public val TEX_COORD_OFFSET: Long = (3*4).toLong()
        public val VERTEX_COUNT: Int = 3
        public val TEX_COORD_COUNT: Int = 2

        fun builder(capacity: Int) = Builder(capacity)

        fun square(w: Int, h: Int): Mesh =
            Mesh.builder(4)
                .add(0, 0, 0, 0, 0)
                .add(w, 0, 0, 1, 0)
                .add(w, h, 0, 1, 1)
                .add(0, h, 0, 0, 1)
                .build()

        private fun createVertexBuffer(data: FloatArray): Int {
            val buffer = BufferUtils.createFloatBuffer(data.size).sure()
            for (val v in data)
                buffer.put(v)
            buffer.rewind()

            val id = glGenBuffers()

            glBindBuffer(GL_ARRAY_BUFFER, id)
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
            glBindBuffer(GL_ARRAY_BUFFER, 0)

            return id
        }

        class Builder private (capacity: Int) {

            private var index = 0
            private val data = FloatArray(capacity * ENTRIES)

            fun add(x: Float, y: Float, z: Float, u: Float, t: Float): Builder {
                data[index++] = x
                data[index++] = y
                data[index++] = z
                data[index++] = u
                data[index++] = t
                return this
            }

            fun add(x: Int, y: Int, z: Int, u: Int, t: Int): Builder =
                add(x.toFloat(), y.toFloat(), z.toFloat(), u.toFloat(), t.toFloat())

            fun build() = Mesh(data)
        }
    }

    fun delete() {
        glDeleteBuffers(vertexBuffer)
    }

    fun bind() {
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer)
    }
    
    fun draw() {
        glDrawArrays(GL_QUADS, 0, size)
    }
}
