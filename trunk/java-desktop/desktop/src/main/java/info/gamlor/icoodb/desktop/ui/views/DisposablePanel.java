package info.gamlor.icoodb.desktop.ui.views;

import info.gamlor.icoodb.desktop.utils.Disposable;
import info.gamlor.icoodb.desktop.utils.Disposer;

import javax.swing.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 02.08.2010
 */
public abstract class DisposablePanel extends JPanel implements Disposable {
    private final Disposer disposer = new Disposer();

    public abstract Disposable getModel();

    protected Disposer getDisposer() {
        return disposer;
    }

    @Override
    public void dispose() {
        final Disposable model = getModel();
        if(null!=model){
            model.dispose();            
        }
        disposer.dispose();
    }
}
