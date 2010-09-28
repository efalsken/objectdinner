package info.gamlor.icoodb.web.controllers;

import info.gamlor.icoodb.web.business.LoggedInUser;
import org.mockito.Matchers;
import org.openid4java.association.AssociationException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.UrlIdentifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static info.gamlor.icoodb.web.controllers.OpenIdController.PATH;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 21.08.2010
 */
public class  TestOpenIdController {

    private MockConsumerManager mockManager;
    private OpenIdController toTest;
    private HttpServletRequest mockHTTP;

    @BeforeMethod
    public void setup() throws Exception {
        this.mockHTTP = mock(HttpServletRequest.class);
        mockManager = new MockConsumerManager();
        this.toTest = new OpenIdController(mockManager);
    }

    @Test
    public void createsARequest(){
        when(mockHTTP.getRequestURL()).thenReturn(new StringBuffer("http://localhost/"+ PATH));
        View view = toTest.tryAuthenticate("http://localhost/openIdProvider", mockHTTP);
        assertEquals(MockConsumerManager.DESTINATION_URL,((RedirectView)view).getUrl().toString());

        mockManager.assertDiscovered();
        mockManager.assertAssiosiated();
        mockManager.assertStartedAuthenticate();
    }
    @Test
    public void canDealWithInvalidUrl(){
        when(mockHTTP.getRequestURL()).thenReturn(new StringBuffer("http://localhost/"+ PATH));
        View view = toTest.tryAuthenticate("http://  ", mockHTTP);
        RedirectView redir = (RedirectView) view;
        assertTrue(!redir.getUrl().contains(MockConsumerManager.DESTINATION_URL));

    }
    @Test
    public void throwOnDiscoverRedirects(){
        mockManager.throwOnDiscover();
        when(mockHTTP.getRequestURL()).thenReturn(new StringBuffer("http://localhost/"+ PATH));
        View view = toTest.tryAuthenticate("http://  ", mockHTTP);
        RedirectView redir = (RedirectView) view;
        assertTrue(!redir.getUrl().contains(MockConsumerManager.DESTINATION_URL));

    }
    @Test
    public void confirmsLogin(){
        HttpSession mockSession = mock(HttpSession.class);
        when(mockHTTP.getSession()).thenReturn(mockSession);
        prerequisiteForLogin();
        View view = toTest.continueAuthenticate(notEmptyParameters(),mockHTTP );


        mockManager.assertVerified();
        verify(mockSession).setAttribute(Matchers.eq(LoggedInUser.USER_IS_AUTHENTICATED), Matchers.<Object>any());
    }
    @Test
    public void noConfirmationIfNoToken(){
        HttpSession mockSession = mock(HttpSession.class);
        when(mockHTTP.getSession()).thenReturn(mockSession);
        prerequisiteForLogin();
        mockManager.dontReturnId();
        View view = toTest.continueAuthenticate(notEmptyParameters(),mockHTTP );


        mockManager.assertVerified();
        verify(mockSession,never()).setAttribute(Matchers.eq(LoggedInUser.USER_IS_AUTHENTICATED), Matchers.<Object>any());
    }
    @Test
    public void logout(){
        HttpSession mockSession = mock(HttpSession.class);
        toTest.logout(mockSession);
        verify(mockSession).removeAttribute(LoggedInUser.USER_IS_AUTHENTICATED);

    }

    private LinkedMultiValueMap<String, String> notEmptyParameters() {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.put("something",new ArrayList<String>());
        return map;
    }

    private void prerequisiteForLogin() {
        when(mockHTTP.getRequestURL()).thenReturn(new StringBuffer("http://localhost/"+ PATH));
        View view = toTest.tryAuthenticate("http://localhost/openIdProvider", mockHTTP);
    }


}

class MockConsumerManager extends ConsumerManager{
     public static final String DESTINATION_URL = "http://localhost/openID";

    private final DiscoveryInformation info;
    private boolean discoveredWasCalled;
    private boolean assiosiateWasCalled;
    private boolean authenticateWasCalled;
    private boolean verifyWasCalled;
    private UrlIdentifier identifier =  new UrlIdentifier("http://localhost/id");
    private boolean throwOnDiscover;


    MockConsumerManager() throws Exception {
        info =  new DiscoveryInformation(new URL("http://localhost/openIdProvider"));
    }

    @Override
    public List discover(String identifier) throws DiscoveryException {
        assertNotNull(identifier);
        discoveredWasCalled = true;
        if(throwOnDiscover){
            throw new DiscoveryException("");
        } else{
            return new ArrayList();
        }
    }

    @Override
    public DiscoveryInformation associate(List discoveries) {
        assertNotNull(discoveries);
        assiosiateWasCalled = true;
        return info;
    }

    @Override
    public AuthRequest authenticate(List discoveries, String returnToUrl) throws ConsumerException, MessageException {

        assertNotNull(discoveries);
        assertNotNull(returnToUrl);
        authenticateWasCalled = true;
        return new MockAuthRequest();
    }

    @Override
    public VerificationResult verify(String receivingUrl, ParameterList response, DiscoveryInformation discovered) throws MessageException, DiscoveryException, AssociationException {
        assertNotNull(receivingUrl);
        assertNotNull(response);
        assertNotNull(discovered);
        verifyWasCalled = true;
        VerificationResult result = new VerificationResult();
        result.setVerifiedId(identifier);
        return result;
    }

    public void assertDiscovered() {
        assertTrue(discoveredWasCalled);
    }

    public void assertAssiosiated() {
        assertTrue(assiosiateWasCalled);
    }

    public void assertStartedAuthenticate() {
        assertTrue(authenticateWasCalled);
    }
    public void assertVerified() {
        assertTrue(verifyWasCalled);
    }

    public void dontReturnId() {
        this.identifier = null;
    }

    public void throwOnDiscover() {
        this.throwOnDiscover = true;
    }

    private static class MockAuthRequest extends AuthRequest{

        private MockAuthRequest() {
            super(new ParameterList());
        }

        @Override
        public String getDestinationUrl(boolean httpGet) {
            return DESTINATION_URL;
        }
    }
}

