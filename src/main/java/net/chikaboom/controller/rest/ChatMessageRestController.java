package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.ChatMessageFacade;
import net.chikaboom.facade.service.ChatMessageFacadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер для управления сущностями сообщений
 */
@RestController
@RequiredArgsConstructor
public class ChatMessageRestController {

    private final ChatMessageFacadeService chatMessageFacadeService;

    /**
     * Производит поиск всех входящих сообщений пользователя
     *
     * @param idAccount идентификатор пользователя
     * @return список входящих сообщений пользователя
     */
    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @GetMapping("/accounts/{idAccount}/messages")
    public ResponseEntity<List<ChatMessageFacade>> findAllByIdRecipient(@PathVariable int idAccount) {
        List<ChatMessageFacade> messages = chatMessageFacadeService
                .findMessagesByIdRecipientOrIdSender(idAccount);

        return ResponseEntity.ok(messages);
    }

    /**
     * Производит поиск всех сообщений между двумя собеседниками
     *
     * @param idAccount идентификатор первого собеседника
     * @param idCompanion идентификатор второго собеседника
     * @return список сообщений чата
     */
    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @GetMapping("/accounts/{idAccount}/messages/chat/{idCompanion}")
    public ResponseEntity<List<ChatMessageFacade>> findAllByAccountIds(@PathVariable int idAccount,
                                                                       @PathVariable int idCompanion) {

        return ResponseEntity.ok(chatMessageFacadeService.findChatByAccountIds(idAccount, idCompanion));
    }

    /**
     * Помечает сообщения в чате как "прочитанные"
     * @param idAccount идентификатор аккаунта-получателя, который прочитал сообщения
     * @param idCompanion идентификатор аккаунта-отправителя, чьи сообщения были прочитаны
     * @return список прочитанных сообщений
     */
    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @PatchMapping("/accounts/{idAccount}/messages/chat/{idCompanion}")
    public ResponseEntity<List<ChatMessageFacade>> markAsViewedAllMessages(@PathVariable int idAccount,
                                                                           @PathVariable int idCompanion) {
        return ResponseEntity.ok(
                chatMessageFacadeService.markMessagesAsViewed(idCompanion, idAccount));
    }
}
