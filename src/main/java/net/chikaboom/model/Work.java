package net.chikaboom.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Определяет модель таблицы Work в базе данных
 */
@Getter
@Setter
public class Work implements Entity {
    /**
     * Id примера работы
     */
    private String idWork;

    /**
     * Id мастера, который выполнил работу. Внешний ключ
     */
    private String idMaster;

    /**
     * Картинка работы
     */
    private byte[] image;

    /**
     * Комментарий мастера о работе
     */
    private String comment;

    public Work() {
        idWork = UUID.randomUUID().toString();
    }
}
