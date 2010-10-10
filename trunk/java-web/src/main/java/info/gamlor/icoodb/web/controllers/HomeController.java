package info.gamlor.icoodb.web.controllers;


import info.gamlor.icoodb.web.business.DinnerLogic;
import info.gamlor.icoodb.web.business.LoggedInUser;
import info.gamlor.icoodb.web.dto.DataContainer;
import info.gamlor.icoodb.web.dto.DinnerMapper;
import info.gamlor.icoodb.web.dto.DinnerViewModel;
import info.gamlor.icoodb.web.dto.InListDinnerViewModel;
import info.gamlor.icoodb.web.model.Dinner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping(value = "/")
@Scope(value = "request")
public class HomeController {
    @Autowired()
    private DinnerLogic logic;
    @Autowired
    private LoggedInUser userLogic;
    
    static final String LOGGED_IN_FLAG = "loggedIn";
    static final String IDENTITY_STRING = "identity";

    public HomeController() {
    }

    public HomeController(DinnerLogic logic, LoggedInUser user) {
        this.logic = logic;
        this.userLogic = user;
    }


    @RequestMapping(value = "home.html", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("home");
        view.addObject(LOGGED_IN_FLAG, userLogic.knowsUser());
        view.addObject(IDENTITY_STRING, userLogic.tryGetUserName());
        return view;
    }


    @RequestMapping(value = "listDinners.json", method = RequestMethod.GET)
    public
    @ResponseBody
    DataContainer<InListDinnerViewModel> listDinners() {
        List<Dinner> dinners = logic.listDinners();
        return DataContainer.create(DinnerMapper.convertAll(dinners));
    }

    @RequestMapping(value = "newDinner.json", method = RequestMethod.POST)
    public
    @ResponseBody
    DataContainer<DinnerViewModel> hostNewDinner() {
        DinnerViewModel dinnerViewModel = DinnerMapper.toViewModel(logic.createNewDinner());
        return DataContainer.createUnEncoded(dinnerViewModel);
    }

    @RequestMapping(value = "dinner-{id}.json", method = RequestMethod.POST)
    public
    @ResponseBody
    DataContainer<DinnerViewModel> editDinner(@PathVariable int id) {
        final Dinner dinner = logic.byIdOrNew(id);
        return DataContainer.createUnEncoded(DinnerMapper.toViewModel(dinner));
    }

    @RequestMapping(value = "saveDinner.json", method = RequestMethod.POST)
    public ModelAndView saveDinner(@Valid DinnerViewModel data, BindingResult result) {
        if (!result.hasErrors()) {
            final Dinner dinner = logic.byIdOrNew(data.getId());
            dinner.setHostedByIdentity(userLogic.loggedInUser());
            DinnerMapper.fromViewModel(data, dinner);
            logic.store(dinner);
        }
        return list();
    }
}
