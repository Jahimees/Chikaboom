package net.chikaboom.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/personality/{id}")
public class PersonalityController {

    @Value("${page.personality}")
    private String PERSONALITY_PAGE;

    @GetMapping
    public String openPersonalityPage(@PathVariable String id) {
        return PERSONALITY_PAGE;
    }
}
