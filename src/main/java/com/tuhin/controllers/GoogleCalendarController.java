package com.tuhin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GoogleCalendarController {

    @RequestMapping("/agenda")
    public String getAgenda() {
        return "agenda";
    }


}
