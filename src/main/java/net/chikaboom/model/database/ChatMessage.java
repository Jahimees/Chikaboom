package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

import static net.chikaboom.util.constant.DbNamesConstant.*;

@Data
@Entity(name = CHAT_MESSAGE)
public class ChatMessage {

    @Id
    @Column(name = ID_CHAT_MESSAGE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idChatMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ID_ACCOUNT_SENDER)
    private Account sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ID_ACCOUNT_RECIPIENT)
    private Account recipient;

    @Column(name = MESSAGE)
    private String message;

    @Column(name = DATE_TIME)
    private Timestamp dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ID_MESSAGE_STATUS)
    private MessageStatus messageStatus;
}
