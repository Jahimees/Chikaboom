package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.ChatMessage;
import net.chikaboom.repository.ChatMessageRepository;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageDataService {

    private final ChatMessageRepository chatMessageRepository;
    private final AccountDataService accountDataService;

    public List<ChatMessage> findByIdRecipient(int idAccount) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("Account not found");
        }

        return chatMessageRepository.findByRecipient(accountOptional.get());
    }

    public Optional<ChatMessage> findById(int idChatMessage) {
        return chatMessageRepository.findById(idChatMessage);
    }

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

    public ChatMessage create(ChatMessage chatMessage) {
        return chatMessageRepository.saveAndFlush(chatMessage);
    }
}
