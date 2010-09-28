package info.gamlor.icoodb.desktop.ui.viewmodel;

import info.gamlor.icoodb.desktop.utils.OneArgAction;

import javax.swing.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 25.07.2010
 */
public interface WorkFlowController {
    void finished();

    <T extends JPanel> void finishAndSwitchTo(Class<T> view,
                                              OneArgAction<T> viewPreparation);
}
