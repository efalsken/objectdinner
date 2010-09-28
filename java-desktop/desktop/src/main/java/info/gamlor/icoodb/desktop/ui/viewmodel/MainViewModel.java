package info.gamlor.icoodb.desktop.ui.viewmodel;

import com.google.inject.Inject;
import com.google.inject.Injector;
import info.gamlor.icoodb.desktop.ui.views.CreateDinner;
import info.gamlor.icoodb.desktop.ui.views.DinnerOverview;
import info.gamlor.icoodb.desktop.utils.OneArgAction;

import javax.swing.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 20.07.2010
 */
public class MainViewModel extends AbstractViewModel implements WorkFlowController {

    private final Injector injector;
    private JPanel viewPanel;

    @Inject
    public MainViewModel(Injector injector) {
        this.injector = injector;

        setViewPanel(newOverviewInstance(injector));
    }


    public void listDinners() {
        setViewPanel(newOverviewInstance(injector));
    }

    public void executeCreateDinner() {
        final CreateDinner dinnerView = injector.getInstance(CreateDinner.class);
        setViewPanel(dinnerView);
    }


    public JPanel getViewPanel() {
        return viewPanel;
    }

    public void setViewPanel(JPanel viewPanel) {
        this.getDisposer().tryDispose(this.viewPanel);
        this.viewPanel = viewPanel;
        this.getDisposer().tryAdd(viewPanel);
        firePropertyChange("viewPanel", viewPanel);

    }

    @Override
    public void finished() {
        setViewPanel(newOverviewInstance(injector));
    }

    @Override
    public <T extends JPanel> void finishAndSwitchTo(Class<T> view, OneArgAction<T> viewPreparation) {
        T instance = injector.getInstance(view);
        viewPreparation.invoke(instance);
        setViewPanel(instance);
    }

    private DinnerOverview newOverviewInstance(Injector injector) {
        return injector.getInstance(DinnerOverview.class);
    }
}
