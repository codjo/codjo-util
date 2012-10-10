package net.codjo.util.file;
import java.io.File;
/**
 *
 */
public class PathUtil {

    public static String normalize(String path) {
        return normalize(path, File.separatorChar);
    }


    static String normalize(String path, char separatorChar) {
        String result;
        if (separatorChar == '/') {
            result = path.replaceAll("\\\\", "/");
        }
        else {
            result = path.replaceAll("/", "\\\\");
        }
        return result;
    }
}
