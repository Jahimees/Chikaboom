package net.chikaboom.facade.service;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.MessageStatusFacadeConverter;
import net.chikaboom.facade.dto.MessageStatusFacade;
import net.chikaboom.model.database.MessageStatus;
import net.chikaboom.service.data.MessageStatusDataService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageStatusFacadeService {

    private final MessageStatusDataService messageStatusDataService;

    public MessageStatusFacade findByName(String name) {
        Optional<MessageStatus> messageStatus = messageStatusDataService.findByName(name);

        if (!messageStatus.isPresent()) {
            throw new NotFoundException("There not found message status with name " + name);
        }

        return MessageStatusFacadeConverter.convertToDto(messageStatus.get());
    }
}
