package info.gamlor.icoodb.web.model;

/**
 * @author roman.stoffel@gamlor.info
 * @since 21.08.2010
 */
public class UserIdentity {
    private String identity;

    public UserIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }
}
