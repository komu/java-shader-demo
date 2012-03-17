package threed.utils

import java.io.*

object ResourceUtils {
        
    fun loadStringResource(path: String, encoding: String): String {
        val input = ResourceUtils.javaClass.getClassLoader().sure().getResourceAsStream(path)
        if (input == null)
            throw FileNotFoundException("no such file: $path")

        try {
            val reader = InputStreamReader(input, encoding)
            val buffer = CharArray(1024)
            val sb = StringBuilder()

            while (true) {
                val n = reader.read(buffer)
                if (n == -1)
                    break
                sb.append(buffer, 0, n);
            }

            return sb.toString().sure()
            
        } finally {
            input.close()
        }
    }
}
