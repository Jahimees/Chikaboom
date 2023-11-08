package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.MessageStatus;
import net.chikaboom.repository.MessageStatusRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageStatusDataService {

    private final MessageStatusRepository messageStatusRepository;

    public Optional<MessageStatus> findByName(String name) {
        return messageStatusRepository.findByName(name);
    }
}
