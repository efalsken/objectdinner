package info.gamlor.icoodb.web.business;

import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;
import info.gamlor.icoodb.web.model.UserIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author roman.stoffel@gamlor.info
 * @since 21.08.2010
 */
@Component()
@Scope(value = "request")

public class LoggedInUser {

    @Autowired
    private HttpSession session;
    @Autowired
    private ObjectContainer container;
    public static final String USER_IS_AUTHENTICATED = "USER_AUTH_TOKEN";


    public LoggedInUser() {
    }

    public LoggedInUser(HttpSession session,ObjectContainer container) {
        this.session = session;
        this.container = container;
    }

    public boolean knowsUser(){
        return null!=userIdString();    
    }

    /**
     * User name or empty string if not logged in
     */
    public String tryGetUserName(){
        String userId = userIdString();
        return null==userId?"":userId;
    }

    @Transactional
    public UserIdentity loggedInUser() {
        ensureHasUser();
        final String idString =  userIdString();

        List<UserIdentity> ids = queryForUser(idString);
        if(ids.size()==1){
            return ids.get(0);
        } else if(ids.size()==0){
            return createNewUser(idString);
        } else{
            throw new IllegalStateException("Cannot have mulitple users for this id = "+idString);
        }
    }

    private UserIdentity createNewUser(String idString) {
        UserIdentity id = new UserIdentity(idString);
        container.store(id);
        return id;
    }

    private List<UserIdentity> queryForUser(final String idString) {
        return container.query(new Predicate<UserIdentity>() {
            @Override
            public boolean match(UserIdentity o) {
                return o.getIdentity().equals(idString);
            }
        });
    }

    private void ensureHasUser() {
        if(!knowsUser()){
            throw new IllegalStateException("Requires a user. Check with knowsUser first");
        }
    }

    private String userIdString() {
        return (String) session.getAttribute(USER_IS_AUTHENTICATED);
    }
}
