package info.gamlor.icoodb.web.testutils;

import java.io.File;
import java.io.IOException;

import static info.gamlor.icoodb.web.utils.ExceptionUtils.reThrow;

/**
 * @author roman.stoffel@gamlor.info
 * @since 10.08.2010
 */
public class TemporaryFiles {
    public static String tempDirName() {
        final File file;
        try {
            file = File.createTempFile("tempFile", "tmp");
            file.deleteOnExit();
            return file.getAbsolutePath();
        } catch (IOException e) {
            return reThrow(e);
        }
    }
}
