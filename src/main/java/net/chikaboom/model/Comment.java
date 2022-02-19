package net.chikaboom.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Определяет модель таблицы Comment в базе данных
 */
@Data
@Entity
@Table(name = "comment")
public class Comment implements BaseEntity {
    /**
     * Id комментария
     */
    @Id
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
