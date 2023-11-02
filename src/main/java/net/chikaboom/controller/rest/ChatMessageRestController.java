package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.ChatMessageFacade;
import net.chikaboom.facade.service.ChatMessageFacadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//TODO create message
@RestController
@RequiredArgsConstructor
public class ChatMessageRestController {

    private final ChatMessageFacadeService chatMessageFacadeService;

    @GetMapping("/accounts/{idAccount}/messages")
    public ResponseEntity<List<ChatMessageFacade>> findAllByIdRecipient(@PathVariable int idAccount) {
        List<ChatMessageFacade> messages = chatMessageFacadeService
                .findMessagesByIdRecipientOrIdSender(idAccount);

        return ResponseEntity.ok(messages);
    }

    @GetMapping("/accounts/{idAccount}/messages/chat/{idCompanion}")
    public ResponseEntity<List<ChatMessageFacade>> findAllByAccountIds(@PathVariable int idAccount,
                                                                       @PathVariable int idCompanion) {

        return ResponseEntity.ok(chatMessageFacadeService.findChatByAccountIds(idAccount, idCompanion));
    }
}
