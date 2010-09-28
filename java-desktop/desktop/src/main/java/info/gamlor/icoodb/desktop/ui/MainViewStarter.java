package info.gamlor.icoodb.desktop.ui;

import com.google.inject.Inject;
import com.google.inject.Provider;
import info.gamlor.icoodb.desktop.ui.views.MainPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 20.07.2010
 */
class MainViewStarter {
    @Inject
    public MainViewStarter(final Provider<MainPanel> paneProvider) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = paneProvider.get();
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setTitle("db4o Nerddinner");
                frame.setMinimumSize(new Dimension(800, 600));
                frame.setPreferredSize(new Dimension(1024, 769));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
