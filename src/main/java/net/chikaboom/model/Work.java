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
    //    TODO DOCUMENTATION
    private String idWork;
    private String idMaster;
    private byte[] image;
    private String comment;

    public Work() {
        idWork = UUID.randomUUID().toString();
    }
}
