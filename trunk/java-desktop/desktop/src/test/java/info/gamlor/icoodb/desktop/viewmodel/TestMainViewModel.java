package info.gamlor.icoodb.desktop.viewmodel;

import com.google.inject.Injector;
import info.gamlor.icoodb.desktop.testutils.WasInvoked;
import info.gamlor.icoodb.desktop.ui.viewmodel.MainViewModel;
import info.gamlor.icoodb.desktop.ui.views.CreateDinner;
import info.gamlor.icoodb.desktop.ui.views.DinnerOverview;
import info.gamlor.icoodb.desktop.utils.Disposable;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.swing.*;

import static info.gamlor.icoodb.desktop.testutils.PropertyChangeEventsTestUtils.assertCanSetAndGetWithEvents;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author roman.stoffel@gamlor.info
 * @since 20.07.2010
 */
public class TestMainViewModel {
    private MainViewModel toTest;
    private Injector mockInjector;

    @BeforeMethod
    public void setup() {
        this.mockInjector = mock(Injector.class);
        this.toTest = new MainViewModel(mockInjector);
    }

    @Test
    public void resolvedDefaultView() {
        when(mockInjector.getInstance(DinnerOverview.class)).thenReturn(createDinnerOverview());
        MainViewModel toTest = new MainViewModel(mockInjector);

        final JPanel view = toTest.getViewPanel();
        assertTrue(view instanceof DinnerOverview);
    }

    @Test
    public void listsDinners() {
        when(mockInjector.getInstance(DinnerOverview.class)).thenReturn(createDinnerOverview());
        MainViewModel toTest = new MainViewModel(mockInjector);

        toTest.executeCreateDinner();
        toTest.listDinners();

        final JPanel view = toTest.getViewPanel();
        assertTrue(view instanceof DinnerOverview);
    }

    @Test
    public void createNewDinner() {
        when(mockInjector.getInstance(CreateDinner.class)).thenReturn(new CreateDinner());

        toTest.executeCreateDinner();

        final JPanel view = toTest.getViewPanel();
        assertTrue(view instanceof CreateDinner);
    }

    @Test
    public void closesOnChildWorkflow() {
        when(mockInjector.getInstance(DinnerOverview.class)).thenReturn(createDinnerOverview());
        when(mockInjector.getInstance(CreateDinner.class)).thenReturn(new CreateDinner());

        toTest.executeCreateDinner();

        toTest.finished();
        final JPanel view = toTest.getViewPanel();
        assertTrue(view instanceof DinnerOverview);
    }

    private DinnerOverview createDinnerOverview() {
        return new DinnerOverview(ModelTestInstances.dinnerListModel());
    }

    @Test
    public void usesViewOnSwitch() {
        when(mockInjector.getInstance(DinnerOverview.class)).thenReturn(createDinnerOverview());
        final CreateDinner dinnerView = new CreateDinner();
        when(mockInjector.getInstance(CreateDinner.class)).thenReturn(dinnerView);


        WasInvoked<CreateDinner> expectInvocation = new WasInvoked<CreateDinner>(dinnerView);
        toTest.finishAndSwitchTo(CreateDinner.class, expectInvocation);
        expectInvocation.assertWasInvoked();
        final JPanel view = toTest.getViewPanel();
        assertNotNull(view);
        assertTrue(view instanceof CreateDinner);
    }

    @Test
    public void getSetViewModel() {
        assertCanSetAndGetWithEvents(toTest, "viewPanel", new JPanel());
    }

    @Test
    public void disposesOldView() {
        DisposableJPanel panelToDispose = new DisposableJPanel();
        toTest.setViewPanel(panelToDispose);
        toTest.setViewPanel(new JPanel());
        panelToDispose.assertDisposed();
    }


    private static class DisposableJPanel extends JPanel implements Disposable {
        private boolean disposed;

        @Override
        public void dispose() {
            this.disposed = true;
        }

        public void assertDisposed() {
            assertTrue(disposed);
        }
    }
}
