package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.ChatMessage;
import net.chikaboom.model.database.MessageStatus;
import net.chikaboom.repository.ChatMessageRepository;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.chikaboom.util.constant.DbNamesConstant.NOT_VIEWED;
import static net.chikaboom.util.constant.DbNamesConstant.VIEWED;

/**
 * Сервис для работы с сущностями типа {@link ChatMessage}
 */
@Service
@RequiredArgsConstructor
public class ChatMessageDataService {

    private final ChatMessageRepository chatMessageRepository;
    private final AccountDataService accountDataService;
    public final MessageStatusDataService messageStatusDataService;

    /**
     * Производит поиск сообщений по получателю
     *
     * @param idAccount идентификатор получателя
     * @return список сообщений
     */
    public List<ChatMessage> findByIdRecipient(int idAccount) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("Account not found");
        }

        return chatMessageRepository.findByRecipient(accountOptional.get());
    }

    /**
     * Производит поиск всех сообщений конкретного аккаунта (полученных и отправленных)
     *
     * @param idAccount идентификатор аккаунта
     * @return список всех сообщений конкретного аккаунта
     */
    public List<ChatMessage> findByIdRecipientOrIdSender(int idAccount) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("Account not found");
        }

        return chatMessageRepository.findByRecipientOrSender(accountOptional.get(), accountOptional.get());
    }

    /**
     * Производит поиск сообщения по его идентификатору
     *
     * @param idChatMessage идентификатор сообщения
     * @return найденное сообщение
     */
    public Optional<ChatMessage> findById(int idChatMessage) {
        return chatMessageRepository.findById(idChatMessage);
    }

    /**
     * Производит поиск сообщений между двумя пользователями
     *
     * @param idFirstAccount  идентификатор аккаунта первого пользователя
     * @param idSecondAccount идентификатор аккаунта второго пользователя
     * @return список найденных сообщений
     */
    public List<ChatMessage> findChatByAccountIds(int idFirstAccount, int idSecondAccount) {
        Optional<Account> firstAccountOptional = accountDataService.findById(idFirstAccount);
        Optional<Account> secondAccountOptional = accountDataService.findById(idSecondAccount);

        if (!firstAccountOptional.isPresent() || !secondAccountOptional.isPresent()) {
            throw new NotFoundException("There not found account/s");
        }

        List<ChatMessage> chatMessageList1 = chatMessageRepository.findByRecipientAndSender(
                firstAccountOptional.get(), secondAccountOptional.get());
        List<ChatMessage> chatMessageList2 = chatMessageRepository.findByRecipientAndSender(
                secondAccountOptional.get(), firstAccountOptional.get());

        List<ChatMessage> fullMessageList = new ArrayList<>();
        fullMessageList.addAll(chatMessageList1);
        fullMessageList.addAll(chatMessageList2);

        return fullMessageList;
    }

    /**
     * Создает сущность сообщения в базе данных
     *
     * @param chatMessage создаваемое сообщение
     * @return созданное сообщение
     */
    public ChatMessage create(ChatMessage chatMessage) {
        Optional<MessageStatus> messageStatus = messageStatusDataService.findByName(NOT_VIEWED);

        if (!messageStatus.isPresent()) {
            throw new NotFoundException("There not found message status");
        }

        chatMessage.setMessageStatus(messageStatus.get());
        ChatMessage createdMessage = chatMessageRepository.saveAndFlush(chatMessage);

        Optional<Account> accountSenderOptional = accountDataService.findById(createdMessage.getSender().getIdAccount());
        Optional<Account> accountRecipientOptional = accountDataService.findById(createdMessage.getRecipient().getIdAccount());

        if (!accountRecipientOptional.isPresent()
                || !accountSenderOptional.isPresent()) {
            throw new NotFoundException("There not found account");
        }

        createdMessage.setSender(accountSenderOptional.get());
        createdMessage.setRecipient(accountRecipientOptional.get());

        return createdMessage;
    }

    /**
     * Помечает все сообщения получателя в беседе, как "прочитанные"
     *
     * @param idSender идентификатор отправителя сообщений
     * @param idRecipient идентификатор получателя сообщений
     * @return список сообщений, которые были помечены как "прочитанные"
     */
    public List<ChatMessage> markMessagesAsViewed(int idSender, int idRecipient) {
        Optional<Account> senderOptional = accountDataService.findById(idSender);
        Optional<Account> recipientOptional = accountDataService.findById(idRecipient);

        if (!senderOptional.isPresent()) {
            throw new NotFoundException("Account with id " + idSender + " not found");
        }

        if (!recipientOptional.isPresent()) {
            throw new NotFoundException("Account with id " + idRecipient + " not found");
        }

        Optional<MessageStatus> messageStatus = messageStatusDataService.findByName(VIEWED);

        if (!messageStatus.isPresent()) {
            throw new NotFoundException("Message status not found");
        }

        List<ChatMessage> messages = chatMessageRepository
                .findByRecipientAndSender(recipientOptional.get(), senderOptional.get());

        messages.forEach((message) -> {
            message.setMessageStatus(messageStatus.get());
        });

        return chatMessageRepository.saveAll(messages);
    }
}
