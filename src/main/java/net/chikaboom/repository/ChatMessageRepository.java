package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

    List<ChatMessage> findByRecipient(Account recipientAccount);

    List<ChatMessage> findByRecipientAndSender(Account recipient, Account sender);
}
