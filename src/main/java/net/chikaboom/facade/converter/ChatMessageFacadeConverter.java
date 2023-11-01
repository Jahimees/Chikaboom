package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.ChatMessageFacade;
import net.chikaboom.model.database.ChatMessage;

import java.sql.Timestamp;

public final class ChatMessageFacadeConverter implements FacadeConverter {

    private ChatMessageFacadeConverter() {
    }

    public static ChatMessageFacade convertToDto(ChatMessage model) {
        ChatMessageFacade facade = new ChatMessageFacade();

        facade.setIdChatMessage(model.getIdChatMessage());
        if (model.getSender() != null) {
            facade.setSenderFacade(AccountFacadeConverter.convertToDto(model.getSender()));
        }
        if (model.getRecipient() != null) {
            facade.setRecipientFacade(AccountFacadeConverter.convertToDto(model.getRecipient()));
        }
        if (model.getDateTime() != null) {
            facade.setDateTime((Timestamp) model.getDateTime().clone());
        }
        if (model.getMessageStatus() != null) {
            facade.setMessageStatusFacade(MessageStatusFacadeConverter.convertToDto(model.getMessageStatus()));
        }
        facade.setMessage(model.getMessage());

        return facade;
    }

    public static ChatMessage convertToModel(ChatMessageFacade facade) {
        ChatMessage model = new ChatMessage();

        model.setIdChatMessage(facade.getIdChatMessage());
        if (facade.getSenderFacade() != null) {
            model.setSender(AccountFacadeConverter.convertToModel(facade.getSenderFacade()));
        }
        if (facade.getRecipientFacade() != null) {
            model.setRecipient(AccountFacadeConverter.convertToModel(facade.getRecipientFacade()));
        }
        if (facade.getDateTime() != null) {
            model.setDateTime((Timestamp) facade.getDateTime().clone());
        }
        if (facade.getMessageStatusFacade() != null) {
            model.setMessageStatus(MessageStatusFacadeConverter.convertToModel(facade.getMessageStatusFacade()));
        }
        model.setMessage(facade.getMessage());

        return model;
    }
}
