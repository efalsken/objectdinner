package info.gamlor.icoodb.web.controllers;

import info.gamlor.icoodb.web.business.DinnerLogic;
import info.gamlor.icoodb.web.business.LoggedInUser;
import info.gamlor.icoodb.web.model.Dinner;
import info.gamlor.icoodb.web.model.RSVP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author roman.stoffel@gamlor.info
 * @since 23.08.2010
 */
@Controller
@RequestMapping(value = "/rsvp")
@Scope(value = "request")
public class RSVPController {
    @Autowired
    private DinnerLogic dinnerLogic;
    @Autowired
    private LoggedInUser user;

    public RSVPController() {
    }

    public RSVPController(DinnerLogic dinnerLogic,LoggedInUser user ) {

        this.dinnerLogic = dinnerLogic;
        this.user = user;
    }

    @RequestMapping(value = "attend.html", method = RequestMethod.POST)
    public ModelAndView storeRSVP(@RequestParam("id") int dinnerId, @RequestParam("name") String name) {
         final Dinner dinner = dinnerLogic.byIdOrNull(dinnerId);
         if(null==dinner){
             return new ModelAndView(new RedirectView("/"));
         }
         if(!user.knowsUser()){
             return new ModelAndView(new RedirectView("/"));
         }
         final RSVP rsvp = dinner.createRSVP(user.loggedInUser());
         rsvp.setName(name);
         dinnerLogic.store(dinner);
         return new ModelAndView(new RedirectView("/"));
    }
}
