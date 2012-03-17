package threed

import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.glBindBuffer
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL20.glUseProgram
import org.lwjgl.util.glu.GLU.gluOrtho2D
import threed.common.AttributePointer
import threed.common.Mesh
import threed.common.ShaderLoader
import org.lwjgl.opengl.Display
import org.lwjgl.input.Keyboard

class ShaderDemo(val mode: DisplayMode) {

    val mesh = Mesh.square(mode.getWidth(), mode.getHeight())
    val shader = ShaderLoader.createShaderProgram("apple")
    var ratio = 1.toFloat()

    fun execute() {
        initGl()
        mainLoop()
        mesh.delete()
    }

    private fun initGl() {
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        gluOrtho2D(0.toFloat(), mode.getWidth().toFloat(), 0.toFloat(), mode.getHeight().toFloat())

        updateModelView()

        glClearColor(0.0.toFloat(), 0.0.toFloat(), 0.0.toFloat(), 0.0.toFloat())
        Display.setVSyncEnabled(true)

        glShadeModel(GL_SMOOTH)
        glEnable(GL_DEPTH_TEST)
        glDepthFunc(GL_LEQUAL)
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST)
    }

    private fun updateModelView() {
        glMatrixMode(GL_MODELVIEW)
        glLoadIdentity()

        val w = mode.getWidth()*ratio
        val h = mode.getHeight()*ratio
        val x = (mode.getWidth()/2) - w/2
        val y = (mode.getHeight()/2) - h/2

        glViewport(x.toInt(), y.toInt(), w.toInt(), h.toInt())
    }

    private fun mainLoop() {
        var frames = 0.toLong()
        val start = System.currentTimeMillis()

        while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Display.isCloseRequested()) {
            processInput()
            frames++
            render()
            Display.update()
        }

        val total = System.currentTimeMillis() - start

        val seconds = total/1000.toFloat()
        println("fps: " + (frames / seconds).toInt())
    }

    private fun processInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            ratio -= 0.05
            updateModelView()
        } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            ratio += 0.05
            updateModelView()
        }
    }

    private fun render() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        glEnableClientState(GL_VERTEX_ARRAY)

        // bind buffers
        mesh.bind()

        // init shader
        shader.bind()

        shader["time"] = System.currentTimeMillis() % 1000000 / 1000.toFloat()
        shader["unResolution"] = #(mode.getWidth().toFloat(), mode.getHeight().toFloat(), 1.toFloat())

        glVertexPointer(Mesh.VERTEX_COUNT, GL_FLOAT, Mesh.SIZE_IN_BYTES, Mesh.VERTEX_OFFSET)
        shader["vertexTextureCoordinate"] = AttributePointer(Mesh.TEX_COORD_COUNT, GL_FLOAT, true, Mesh.SIZE_IN_BYTES, Mesh.TEX_COORD_OFFSET)

        // draw the stuff
        mesh.draw()

        glBindBuffer(GL_ARRAY_BUFFER, 0)

        glDisableClientState(GL_VERTEX_ARRAY)

        glUseProgram(0)
    }
}

fun main(args: Array<String>) {
    try {
        val mode = Display.getDisplayMode().sure()
        Display.setDisplayModeAndFullscreen(mode)
        Display.create()
        Keyboard.create()

        try {
            ShaderDemo(mode).execute()

        } finally {
            Display.destroy()
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
}
