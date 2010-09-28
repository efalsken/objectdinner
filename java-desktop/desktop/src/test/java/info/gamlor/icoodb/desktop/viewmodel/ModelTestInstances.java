package info.gamlor.icoodb.desktop.viewmodel;

import info.gamlor.icoodb.desktop.logic.DinnerLogic;
import info.gamlor.icoodb.desktop.testutils.InMemoryDB;
import info.gamlor.icoodb.desktop.ui.viewmodel.DinnerListModel;
import info.gamlor.icoodb.desktop.ui.viewmodel.WorkFlowController;
import info.gamlor.icoodb.desktop.utils.OneArgAction;

import javax.swing.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 30.07.2010
 */
public final class ModelTestInstances {

    public static DinnerListModel dinnerListModel() {
        return new DinnerListModel(new DinnerLogic(InMemoryDB.newDB()), new WorkFlowController() {
            @Override
            public void finished() {

            }

            @Override
            public <T extends JPanel> void finishAndSwitchTo(Class<T> view, OneArgAction<T> viewPreparation) {
            }
        });
    }
}
