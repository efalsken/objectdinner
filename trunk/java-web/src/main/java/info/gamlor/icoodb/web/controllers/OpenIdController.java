package info.gamlor.icoodb.web.controllers;

import info.gamlor.icoodb.web.business.LoggedInUser;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.ParameterList;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static info.gamlor.icoodb.web.utils.ExceptionUtils.reThrow;

/**
 * @author roman.stoffel@gamlor.info
 * @since 18.08.2010
 */
@Controller
@RequestMapping(value = OpenIdController.PATH)
@Scope(value = "session")
@SessionAttributes("info")
public class OpenIdController {
    static final String PATH = "/openid";
    static final String RESPONSE_PART = "login.html";
    private transient ConsumerManager manager;
    private DiscoveryInformation info;

    public OpenIdController() {
        try {
            manager = new ConsumerManager();
        } catch (ConsumerException e) {
            reThrow(e);
        }
    }

    public OpenIdController(ConsumerManager manager) {
        this.manager = manager;
    }

    @RequestMapping(value = "startAuth.html", method = RequestMethod.POST)
    public View tryAuthenticate(@RequestParam("openIdUrl") String openIdUrl, HttpServletRequest request) {
        final String url = toUrl(openIdUrl);
        if (invalidUrl(url)) {
            return redirectToRoot();
        }
        try {
            // discover the OpenID authentication server's endpoint URL
            List discoveries = manager.discover(url);

            // attempt to associate with the OpenID provider
            // and retrieve one service endpoint for authentication
            info = manager.associate(discoveries);

            String returnURL = findOutRedirectUrl(request.getRequestURL().toString());

            // generate an AuthRequest message to be sent to the OpenID provider
            AuthRequest authReq = manager.authenticate(discoveries, returnURL);
            return new RedirectView(authReq.getDestinationUrl(true));
        } catch (DiscoveryException e) {
            return redirectToRoot();
        } catch (Exception e) {
            return reThrow(e);
        }
    }

    private RedirectView redirectToRoot() {
        return new RedirectView("/", true);
    }

    private boolean invalidUrl(String url) {
        try {
            return new URL(url).getHost().length() <= 0;
        } catch (MalformedURLException e) {
            return true;
        }
    }


    @RequestMapping(value = RESPONSE_PART, method = RequestMethod.GET)
    public View continueAuthenticate(@RequestParam MultiValueMap<String, String> params, HttpServletRequest request) {
        if (null != info && !params.isEmpty()) {
            processAuthResponse(request);
        }
        return redirectHome();
    }


    @RequestMapping(value = "logout.html")
    public View logout(HttpSession session) {
        session.removeAttribute(LoggedInUser.USER_IS_AUTHENTICATED);
        return redirectHome();
    }

    private RedirectView redirectHome() {
        return new RedirectView("/");
    }

    private void processAuthResponse(HttpServletRequest request) {
        String queryString = request.getQueryString();

        String receivingURL = receivingUrl(request, queryString);

        ParameterList openidResp = new ParameterList(request.getParameterMap());
        VerificationResult verification = null;
        try {
            verification = manager.verify(receivingURL.toString(), openidResp, info);

            // examine the verification result and extract the verified identifier
            Identifier verified = verification.getVerifiedId();
            if (null != verified) {
                request.getSession().setAttribute(LoggedInUser.USER_IS_AUTHENTICATED, verified.getIdentifier());
            }
        } catch (Exception e) {
            reThrow(e);
        }
    }

    private String receivingUrl(HttpServletRequest request, String queryString) {
        StringBuffer receivingURL = new StringBuffer(request.getRequestURL().toString());
        if (queryString != null && queryString.length() > 0) {
            receivingURL.append("?").append(request.getQueryString());
        }
        return receivingURL.toString();
    }

    private String findOutRedirectUrl(String requestURL) {
        int urlIndex = requestURL.indexOf(PATH);
        return requestURL.substring(0, urlIndex) + "/" + PATH + "/" + RESPONSE_PART;
    }

    private String toUrl(String url) {
        if (url.startsWith("http://")) {
            return url;
        } else {
            return "http://" + url;
        }
    }

}
