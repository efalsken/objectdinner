package info.gamlor.icoodb.web.dto;

/**
 * Only the attributes shown in the lists
 *
 * @author roman.stoffel@gamlor.info
 * @since 25.08.2010
 */
public class InListDinnerViewModel extends DinnerViewModelBase {
    private int attendeeCount;

    public int getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(int attendeeCount) {
        this.attendeeCount = attendeeCount;
    }
}
