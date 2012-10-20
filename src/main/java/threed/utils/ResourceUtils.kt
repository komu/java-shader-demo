package threed.utils

import java.io.*

object ResourceUtils {
        
    fun loadStringResource(path: String, encoding: String): String {
        val input = javaClass.getClassLoader()!!.getResourceAsStream(path)
        if (input != null)
            return input.use { it.reader(encoding).readText() }
        else
            throw FileNotFoundException("no such file: $path")
    }
}
