package net.chikaboom.controller;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.ChatMessageFacade;
import net.chikaboom.facade.service.ChatMessageFacadeService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Контроллер, отвечающий за приём и передачу сообщений
 */
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageFacadeService chatMessageFacadeService;

    /**
     * Принимает сообщения по адресу /chat/{idRecipient}
     * и отправляет на адрес /accounts/idRecipient/queue/messages, на который подписывается получатель
     *
     * @param idRecipient       идентификатор получателя
     * @param chatMessageFacade переданное сообщение
     * @return созданное сообщение в базе
     */
    @MessageMapping("/chat/{idRecipient}")
    @SendTo("/accounts/{idRecipient}/queue/messages")
    public ChatMessageFacade processMessage(@DestinationVariable int idRecipient, @RequestBody ChatMessageFacade chatMessageFacade) {
        return chatMessageFacadeService.create(chatMessageFacade);
    }
}
