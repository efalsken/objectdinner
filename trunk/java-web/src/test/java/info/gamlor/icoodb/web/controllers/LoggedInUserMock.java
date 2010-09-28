package info.gamlor.icoodb.web.controllers;

import info.gamlor.icoodb.web.business.LoggedInUser;
import info.gamlor.icoodb.web.model.UserIdentity;

/**
* @author roman.stoffel@gamlor.info
* @since 25.08.2010
*/
class LoggedInUserMock extends LoggedInUser {
    private boolean loggedIn;
    public LoggedInUserMock() {
        super(null, null);
    }

    @Override
    public boolean knowsUser() {
        return loggedIn;
    }

    @Override
    public String tryGetUserName() {
        return "loginName";
    }

    @Override
    public UserIdentity loggedInUser() {
        return new UserIdentity("testhost");
    }

    public void isLoggedIn() {
        loggedIn = true;
    }

    public void notLoggedIn() {
        loggedIn = false;
    }
}
