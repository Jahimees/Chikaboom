package net.chikaboom.controller.tab;

import net.chikaboom.annotation.LoggableViewController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CommentTabController {

    @Value("${tab.comment}")
    private String COMMENT_PAGE;

    @LoggableViewController

    @GetMapping("/chikaboom/personality/{idAccount}/comment")
    public String openCommentTab(@PathVariable int idAccount) {
        return COMMENT_PAGE;
    }
}
