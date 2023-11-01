package net.chikaboom.facade.service;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.ChatMessageFacadeConverter;
import net.chikaboom.facade.dto.ChatMessageFacade;
import net.chikaboom.model.database.ChatMessage;
import net.chikaboom.service.data.ChatMessageDataService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageFacadeService {

    private final ChatMessageDataService chatMessageDataService;

    public ChatMessageFacade findById(int idChatMessage) {
        Optional<ChatMessage> chatMessage = chatMessageDataService.findById(idChatMessage);

        if (!chatMessage.isPresent()) {
            throw new NotFoundException("There not found chat message");
        }

        return ChatMessageFacadeConverter.convertToDto(chatMessage.get());
    }

    public List<ChatMessageFacade> findMessagesByIdRecipient(int idAccountRecipient) {
        return chatMessageDataService.findByIdRecipient(idAccountRecipient)
                .stream().map(ChatMessageFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    public List<ChatMessageFacade> findChatByAccountIds(int idFirstAccount, int idSecondAccount) {
        return chatMessageDataService.findChatByAccountIds(idFirstAccount, idSecondAccount)
                .stream().map(ChatMessageFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    public ChatMessageFacade create(ChatMessageFacade chatMessageFacade) {
        return ChatMessageFacadeConverter.convertToDto(
                chatMessageDataService.create(ChatMessageFacadeConverter.convertToModel(chatMessageFacade)));
    }
}
