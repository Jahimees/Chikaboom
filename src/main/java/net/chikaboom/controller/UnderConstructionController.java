package net.chikaboom.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chikaboom/under_construction")
public class UnderConstructionController {

    @Value("${page.under_construction}")
    private String UNDER_CONSTRUCTION_PAGE;

    @GetMapping
    public String processRequest() {
        return UNDER_CONSTRUCTION_PAGE;
    }
}
