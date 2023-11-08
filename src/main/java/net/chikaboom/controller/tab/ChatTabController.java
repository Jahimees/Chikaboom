package net.chikaboom.controller.tab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChatTabController {

    @Value("${tab.chat}")
    private String CHAT_TAB;

    @GetMapping("/chikaboom/personality/{idAccount}/chats")
    public String openMessageTab(@PathVariable int idAccount) {
        return CHAT_TAB;
    }
}
