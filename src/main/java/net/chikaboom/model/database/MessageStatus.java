package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.*;

@Data
@Entity(name = MESSAGE_STATUS)
public class MessageStatus {

    @Id
    @Column(name = ID_MESSAGE_STATUS)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMessageStatus;

    @Column(name = NAME)
    private String name;
}
