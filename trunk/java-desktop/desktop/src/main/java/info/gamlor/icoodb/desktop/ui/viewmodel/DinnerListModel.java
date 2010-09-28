package info.gamlor.icoodb.desktop.ui.viewmodel;

import ch.lambdaj.function.convert.Converter;
import com.google.inject.Inject;
import info.gamlor.icoodb.desktop.logic.DinnerLogic;
import info.gamlor.icoodb.desktop.model.Dinner;
import info.gamlor.icoodb.desktop.ui.views.CreateDinner;
import info.gamlor.icoodb.desktop.ui.views.RSVPPanel;
import info.gamlor.icoodb.desktop.utils.Disposable;
import info.gamlor.icoodb.desktop.utils.OneArgAction;

import java.util.List;

import static ch.lambdaj.Lambda.convert;

/**
 * @author roman.stoffel@gamlor.info
 * @since 21.07.2010
 */
public class DinnerListModel extends AbstractViewModel {
    private DinnerLogic logic;
    private final WorkFlowController workflowController;

    @Inject
    public DinnerListModel(DinnerLogic logic, WorkFlowController workflowController) {
        this.logic = logic;
        this.workflowController = workflowController;
        this.getDisposer().add(new Disposable() {
            @Override
            public void dispose() {
                storeAll();
            }
        });
    }

    private void storeAll() {
        for (Dinner dinner : logic.listDinners()) {
            logic.store(dinner);
        }
    }

    public List<DinnerListItem> getDinners() {
        return convert(logic.listDinners(), new Converter<Dinner, DinnerListItem>() {
            @Override
            public DinnerListItem convert(Dinner dinner) {
                return new DinnerListItem(dinner);
            }
        });
    }

    public void edit(final Dinner dinner) {
        workflowController.finishAndSwitchTo(CreateDinner.class, new OneArgAction<CreateDinner>() {
            @Override
            public void invoke(CreateDinner arg) {
                arg.getModel().setDinner(dinner);
            }
        });
    }


    public void addRSVP(final Dinner dinner) {
        workflowController.finishAndSwitchTo(RSVPPanel.class, new OneArgAction<RSVPPanel>() {
            @Override
            public void invoke(RSVPPanel arg) {
                arg.getModel().createForDinner(dinner);
            }
        });
    }

    public void delete(final Dinner dinner) {
        logic.delete(dinner);
        firePropertyChange("dinners", logic.listDinners());
    }
}
