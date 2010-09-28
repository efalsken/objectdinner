package info.gamlor.icoodb.desktop;

import com.google.inject.Guice;
import com.google.inject.Injector;
import info.gamlor.icoodb.desktop.database.DatabaseModule;
import info.gamlor.icoodb.desktop.ui.UIModule;

import javax.swing.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 14.07.2010
 */
public final class Boot {
    public static void main(String[] args) throws Exception {
        setupLookAndFeel();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                startUpApplication();
            }
        });
    }

    private static void startUpApplication() {
        final Injector injector = Guice.createInjector(
                new DatabaseModule(),
                new UIModule()
        );
    }


    private static void setupLookAndFeel() throws Exception {
        if (null == System.getProperty("swing.defaultlaf")) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
    }
}
