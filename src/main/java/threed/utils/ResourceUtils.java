package threed.utils;

import threed.common.ShaderLoader;

import java.io.*;

public class ResourceUtils {
        
    public static String loadStringResource(String path, String encoding) throws IOException {
        InputStream in = ShaderLoader.class.getClassLoader().getResourceAsStream(path);
        if (in == null)
            throw new FileNotFoundException("no such file: " + path);

        try {
            Reader reader = new InputStreamReader(in, encoding);
            char[] buffer = new char[1024];
            int n;
            StringBuilder sb = new StringBuilder();

            while ((n = reader.read(buffer)) != -1)
                sb.append(buffer, 0, n);

            return sb.toString();
            
        } finally {
            in.close();
        }
    }
}
