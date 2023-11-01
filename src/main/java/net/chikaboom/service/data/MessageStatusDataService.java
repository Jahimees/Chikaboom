package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.MessageStatus;
import net.chikaboom.repository.MessageStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageStatusDataService {

    private final MessageStatusRepository messageStatusRepository;

    public Optional<MessageStatus> findById(int idMessageStatus) {
        return messageStatusRepository.findById(idMessageStatus);
    }

    public List<MessageStatus> findAll() {
        return messageStatusRepository.findAll();
    }
}
