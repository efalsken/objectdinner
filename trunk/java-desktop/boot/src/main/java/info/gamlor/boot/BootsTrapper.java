package info.gamlor.boot;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Starts-Up the application.
 *
 * @author <a href="mailto:roman.stoffel@gamlor.info">Roman Stoffel</a>
 * @since 13.07.2009
 */
public class BootsTrapper {
    private final static String APPLICATION_PATH = "./application";
    private final static String APPLICATION_MAIN = "info.gamlor.icoodb.desktop.Boot";

    public static void main(String[] args) {
        final File file = new File(APPLICATION_PATH);
        try {
            tryStartUp(file,args);
        } catch (Exception e) {
            System.err.println("Unexpected exception'" + file + "'.");
            e.printStackTrace(System.err);
        }
    }

    private static void tryStartUp(File applicationRoot,String[] arguments) throws Exception {
        if (applicationRoot.exists()) {
            final URL[] applicationJars = enumerateLibs(applicationRoot);

            // Here we setup our native library class loader
            final URLClassLoader classLoader = new URLClassLoader(applicationJars);
            Thread.currentThread().setContextClassLoader(classLoader);
            // and load the real-applicaiton with it
            final Class<?> bootClass = classLoader.loadClass(APPLICATION_MAIN);
            final Method mainMethod = bootClass.getMethod("main", arguments.getClass());
            mainMethod.invoke(null,new Object[]{arguments});
            
        } else {
            System.err.println("Couldn't find application-jars in '" + applicationRoot + "'.");
        }
    }

    private static URL[] enumerateLibs(File file) throws MalformedURLException {
        final File[] files = file.listFiles();
        final List<URL> urls = new ArrayList<URL>();
        for (File jarFile : files) {
            urls.add(jarFile.toURI().toURL());
        }
        return urls.toArray(new URL[urls.size()]);
    }
}
