package info.gamlor.icoodb.desktop.ui;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import info.gamlor.icoodb.desktop.ui.viewmodel.MainViewModel;
import info.gamlor.icoodb.desktop.ui.viewmodel.WorkFlowController;

/**
 * @author roman.stoffel@gamlor.info
 * @since 20.07.2010
 */
public class UIModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MainViewModel.class).in(Scopes.SINGLETON);
        bind(WorkFlowController.class).to(MainViewModel.class);
        bind(MainViewStarter.class).asEagerSingleton();
    }
}
