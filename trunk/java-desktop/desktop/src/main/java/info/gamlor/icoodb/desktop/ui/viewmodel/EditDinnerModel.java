package info.gamlor.icoodb.desktop.ui.viewmodel;

import com.google.inject.Inject;
import info.gamlor.icoodb.desktop.logic.BoundValidator;
import info.gamlor.icoodb.desktop.logic.DinnerLogic;
import info.gamlor.icoodb.desktop.logic.DinnerValidator;
import info.gamlor.icoodb.desktop.model.Dinner;
import info.gamlor.icoodb.desktop.utils.OneArgAction;

/**
 * @author roman.stoffel@gamlor.info
 * @since 20.07.2010
 */
public class EditDinnerModel extends AbstractViewModel {
    private final DinnerLogic logic;
    private final WorkFlowController workFlow;

    private Dinner dinner;

    private BoundValidator<Dinner> validator;

    @Inject
    public EditDinnerModel(DinnerLogic logic, WorkFlowController workFlow) {
        this.logic = logic;
        this.setDinner(logic.createNewDinner());
        this.workFlow = workFlow;
    }

    public void executeDone() {
        logic.store(dinner);
        workFlow.finished();
    }

    public Dinner getDinner() {
        return dinner;
    }


    public void setDinner(Dinner dinner) {
        this.dinner = dinner;
        firePropertyChange("dinner", dinner);
        this.validator = BoundValidator.create(this.dinner, new DinnerValidator());
        getDisposer().add(validator.onChange(new OneArgAction<Boolean>() {
            @Override
            public void invoke(Boolean arg) {
                boolean value = arg;
                firePropertyChange("readyToFinish", value);
            }
        }));
    }

    public boolean isReadyToFinish() {
        return validator.isValid();
    }

}
