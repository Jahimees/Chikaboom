package net.chikaboom.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Определяет модель таблицы Work в базе данных
 */
@Data
@Entity
@Table(name = "work")
public class Work implements BaseEntity {
    /**
     * Id примера работы
     */
    @Id
    private String idWork;

    /**
     * Id мастера, который выполнил работу. Внешний ключ
     */
    private String idMaster;

    /**
     * Картинка работы
     */
    private String image;

    /**
     * Комментарий мастера о работе
     */
    private String comment;

    public Work() {
        idWork = UUID.randomUUID().toString();
    }
}
