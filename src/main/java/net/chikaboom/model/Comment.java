package net.chikaboom.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Определяет модель таблицы Comment в базе данных
 */
@Getter
@Setter
public class Comment implements Entity {
    //    TODO DOCUMENTATION
    private String idComment;
    private String idMaster;
    private String idClient;
    private String message;
    private boolean isClientMessage;
    private int rate;

    public Comment() {
        idComment = UUID.randomUUID().toString();
    }
}
