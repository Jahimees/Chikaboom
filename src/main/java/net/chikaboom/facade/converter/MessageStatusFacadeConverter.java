package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.MessageStatusFacade;
import net.chikaboom.model.database.MessageStatus;

public final class MessageStatusFacadeConverter {

    private MessageStatusFacadeConverter() {
    }

    public static MessageStatusFacade convertToDto(MessageStatus model) {
        MessageStatusFacade facade = new MessageStatusFacade();

        facade.setIdMessageStatus(model.getIdMessageStatus());
        facade.setName(model.getName());

        return facade;
    }

    public static MessageStatus convertToModel(MessageStatusFacade facade) {
        MessageStatus model = new MessageStatus();

        model.setIdMessageStatus(facade.getIdMessageStatus());
        model.setName(facade.getName());

        return model;
    }
}
