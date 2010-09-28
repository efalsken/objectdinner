package info.gamlor.icoodb.desktop.ui.viewmodel;

import com.google.inject.Inject;
import info.gamlor.icoodb.desktop.logic.BoundValidator;
import info.gamlor.icoodb.desktop.logic.RSVPLogic;
import info.gamlor.icoodb.desktop.logic.RSVPValidator;
import info.gamlor.icoodb.desktop.model.Dinner;
import info.gamlor.icoodb.desktop.model.RSVP;
import info.gamlor.icoodb.desktop.utils.Disposable;
import info.gamlor.icoodb.desktop.utils.OneArgAction;

import java.util.List;

/**
 * @author roman.stoffel@gamlor.info
 * @since 02.08.2010
 */
public class RSVPModel extends AbstractViewModel {
    private final RSVPLogic logic;
    private final WorkFlowController workflow;

    private Dinner dinner = new Dinner();
    private RSVP currentRSVP = dinner.createRSVP();

    private BoundValidator<RSVP> validation;
    private boolean rollbackRSVP = true;


    @Inject
    public RSVPModel(RSVPLogic logic, WorkFlowController workflow) {
        this.logic = logic;
        this.workflow = workflow;
        this.getDisposer().add(new Disposable() {
            @Override
            public void dispose() {
                if (rollbackRSVP) {
                    RSVPModel.this.dinner.removeRSVP(currentRSVP);
                }
            }
        });
        bindValidator();
    }


    public void executeDone() {
        logic.store(currentRSVP);
        rollbackRSVP = false;
        workflow.finished();
    }

    public String getTitle() {
        return dinner.getTitle();
    }

    public String getDescription() {
        return dinner.getDescription();
    }

    public String getHostedBy() {
        return dinner.getHostedBy();
    }

    public String getAddress() {
        return dinner.getAddress();
    }

    public List<RSVP> getAttendees() {
        return dinner.getAttendees();
    }

    private void createRSVP() {
        final RSVP rsvp = dinner.createRSVP();
        currentRSVP = rsvp;
        fireRSVPEvents(rsvp);
    }

    private void fireAllEvents() {
        firePropertyChange("title", dinner.getTitle());
        firePropertyChange("description", dinner.getDescription());
        firePropertyChange("hostedBy", dinner.getHostedBy());
        firePropertyChange("address", dinner.getAddress());
    }

    public RSVP getCurrentRSVP() {
        return currentRSVP;
    }

    public void createForDinner(Dinner dinner) {
        this.dinner = dinner;
        fireAllEvents();
        createRSVP();
        bindValidator();
    }

    private void bindValidator() {
        validation = BoundValidator.create(this.currentRSVP, new RSVPValidator());
        getDisposer().add(validation.onChange(new OneArgAction<Boolean>() {
            @Override
            public void invoke(Boolean arg) {
                boolean value = arg;
                firePropertyChange("readyToFinish", value);
            }
        }));
    }

    public boolean isReadyToFinish() {
        return validation.isValid();
    }

    private void fireRSVPEvents(RSVP rsvp) {
        firePropertyChange("currentRSVP", rsvp);
        firePropertyChange("attendees", dinner.getAttendees());
    }
}
