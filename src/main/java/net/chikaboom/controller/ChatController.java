package net.chikaboom.controller;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.ChatMessageFacade;
import net.chikaboom.facade.service.ChatMessageFacadeService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

//    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageFacadeService chatMessageFacadeService;

    @MessageMapping("/chat") ///{idRecipient}
    @SendTo("/accounts/queue/messages") //{idRecipient}/
    public ChatMessageFacade processMessage(ChatMessageFacade chatMessageFacade) {
        ChatMessageFacade chatMessageFacade1 = chatMessageFacadeService.create(chatMessageFacade);

        return chatMessageFacade1;
//        messagingTemplate.convertAndSendToUser(
//                String.valueOf(chatMessageFacade.getRecipientFacade().getIdAccount()), "/queue/messages",
//                chatMessageFacade);
    }
}
