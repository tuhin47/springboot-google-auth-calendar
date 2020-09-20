package com.tuhin.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class GoogleCalendarController {

    @Autowired
    GoogleCalendarService googleCalendarService;

    @RequestMapping(value = "/agenda", method = RequestMethod.GET)
    public String getAgendaView(Model model) {
        if(model.containsAttribute("tasks")) return "agenda";
        return "redirect:/agendaData";
    }

    @RequestMapping(value = "/agendaData", method = RequestMethod.GET)
    public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
        return new RedirectView(googleCalendarService.authorizeURL());
    }

    @RequestMapping(value = "/login/google", method = RequestMethod.GET, params = "code")
    public RedirectView oauth2Callback(@RequestParam(value = "code") String code, RedirectAttributes redirectAttributes) {
        RedirectView redirectView = new RedirectView("/agenda", true);
        googleCalendarService.getCalendarEvents(code, redirectAttributes);
        return redirectView;
    }

}
