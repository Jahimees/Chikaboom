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

/**
 * Фасадный сервис-прослойка между контроллером и сервисом.
 * Преобразовывает данные из dto в model и наоборот
 */
@Service
@RequiredArgsConstructor
public class ChatMessageFacadeService {

    private final ChatMessageDataService chatMessageDataService;

    /**
     * Производит поиск сообщения по его идентификатору
     *
     * @param idChatMessage идентификатор сообщения
     * @return найденное сообщение
     */
    public ChatMessageFacade findById(int idChatMessage) {
        Optional<ChatMessage> chatMessage = chatMessageDataService.findById(idChatMessage);

        if (!chatMessage.isPresent()) {
            throw new NotFoundException("There not found chat message");
        }

        return ChatMessageFacadeConverter.convertToDto(chatMessage.get());
    }

    /**
     * Производит поиск сообщений по идентификатору получателя
     *
     * @param idAccountRecipient идентификатор получателя сообщений
     * @return список сообщений
     */
    public List<ChatMessageFacade> findMessagesByIdRecipient(int idAccountRecipient) {
        return chatMessageDataService.findByIdRecipient(idAccountRecipient)
                .stream().map(ChatMessageFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    /**
     * Производит поиск всех сообщений пользователя (полученных и отправленных)
     *
     * @param idAccount идентификатор аккаунта
     * @return список всех сообщений пользователя
     */
    public List<ChatMessageFacade> findMessagesByIdRecipientOrIdSender(int idAccount) {
        return chatMessageDataService.findByIdRecipientOrIdSender(idAccount)
                .stream().map(ChatMessageFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    /**
     * Производит поиск всех сообщений двух собеседников
     *
     * @param idFirstAccount  идентификатор первого пользователя
     * @param idSecondAccount идентификатор второго аккаунта
     * @return список найденных сообщений
     */
    public List<ChatMessageFacade> findChatByAccountIds(int idFirstAccount, int idSecondAccount) {
        return chatMessageDataService.findChatByAccountIds(idFirstAccount, idSecondAccount)
                .stream().map(ChatMessageFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    /**
     * Производит создание сообщения-сущности в базе данных
     *
     * @param chatMessageFacade создаваемое сообщение
     * @return созданное сообщение
     */
    public ChatMessageFacade create(ChatMessageFacade chatMessageFacade) {
        return ChatMessageFacadeConverter.convertToDto(
                chatMessageDataService.create(ChatMessageFacadeConverter.convertToModel(chatMessageFacade)));
    }

    /**
     * Помечает сообщения как прочитанные получателем
     *
     * @param idSender    идентификатор отправителя
     * @param idRecipient идентификатор получателя
     * @return измененные сообщения, которые были прочитаны
     */
    public List<ChatMessageFacade> markMessagesAsViewed(int idSender, int idRecipient) {
        List<ChatMessage> chatMessageList = chatMessageDataService.markMessagesAsViewed(idSender, idRecipient);

        return chatMessageList.stream()
                .map(ChatMessageFacadeConverter::convertToDto).collect(Collectors.toList());
    }
}
