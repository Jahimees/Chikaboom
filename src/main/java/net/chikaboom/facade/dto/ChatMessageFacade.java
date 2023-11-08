package net.chikaboom.facade.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChatMessageFacade {

    private int idChatMessage;
    private AccountFacade senderFacade;
    private AccountFacade recipientFacade;
    private String message;
    private Timestamp dateTime;
    private MessageStatusFacade messageStatusFacade;
}
