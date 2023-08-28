package net.chikaboom.controller.tab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chikaboom/personality/{idAccount}/clients")
public class ClientTabController {

    @Value("${tab.client}")
    private String clientTabPage;

    @GetMapping
    public String openClientTab(@PathVariable int idAccount) {
        return clientTabPage;
    }
}
