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
    /**
     * Id комментария
     */
    private String idComment;

    /**
     * Id мастера. Внешний ключ
     */
    private String idMaster;

    /**
     * Id клиента. Внешний ключ
     */
    private String idClient;

    /**
     * Текст комментария
     */
    private String message;

    /**
     * Булево значение. True - сообщение от клиента мастеру. False - от мастера клиенту
     */
    private boolean isClientMessage;

    /**
     * Оценка работы мастера
     */
    private int rate;

    public Comment() {
        idComment = UUID.randomUUID().toString();
    }
}
