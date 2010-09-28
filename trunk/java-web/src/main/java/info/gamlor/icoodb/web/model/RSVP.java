package info.gamlor.icoodb.web.model;

/**
 * @author roman.stoffel@gamlor.info
 * @since 03.08.2010
 */
public class RSVP {
    private final Dinner dinner;
    private String name = "";
    private UserIdentity identity;

    public RSVP(Dinner dinner,UserIdentity identity) {
        this.dinner = dinner;
        this.identity = identity;
    }

    public Dinner getDinner() {
        return dinner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
