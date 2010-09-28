package info.gamlor.icoodb.web.business;

import com.db4o.ObjectContainer;
import info.gamlor.icoodb.web.model.UserIdentity;
import info.gamlor.icoodb.web.testutils.InMemoryDB;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 21.08.2010
 */
public class TestLoggedInUser {
    private LoggedInUser toTest;
    private HttpSession mockSession;
    private ObjectContainer db;
    private static final String USER_ID = "http://localhost/userId";

    @BeforeMethod
    public void setup(){
        this.mockSession = mock(HttpSession.class);
        when(mockSession.getAttribute(LoggedInUser.USER_IS_AUTHENTICATED)).thenReturn(null);
        this.db = InMemoryDB.newDB();
        this.toTest = new LoggedInUser(mockSession,db);
    }

    @Test
    public void isLoggedIn(){
        prepareLoggedIn();
        assertTrue(toTest.knowsUser());

    }

    @Test
    public void isNotLoggedIn(){
        assertFalse(toTest.knowsUser());
    }
    @Test
    public void emptyStringForNotLoggedIn(){
        assertEquals("",toTest.tryGetUserName());
    }
    @Test
    public void userName(){
        prepareLoggedIn();
        assertEquals(USER_ID,toTest.tryGetUserName());
    }


    @Test
    public void createsUserIfUnknown(){
        prepareLoggedIn();
        assertNotNull(toTest.loggedInUser());
        assertEquals(1,db.query(UserIdentity.class).size());
    }
    @Test
    public void returnsExistingUser(){
        prepareLoggedIn();
        db.store(new UserIdentity(USER_ID));
        assertNotNull(toTest.loggedInUser());
        assertEquals(1,db.query(UserIdentity.class).size());
    }
    @Test
    public void hasIdString(){
        prepareLoggedIn();
        assertEquals(USER_ID,toTest.loggedInUser().getIdentity());
    }
    @Test(expectedExceptions = {IllegalStateException.class})
    public void throwsIfNoUser(){
        toTest.loggedInUser();
    }

    private void prepareLoggedIn() {
        when(mockSession.getAttribute(LoggedInUser.USER_IS_AUTHENTICATED)).thenReturn(USER_ID);
    }
}
