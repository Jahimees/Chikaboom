package net.chikaboom.model.response;

import lombok.Data;
import net.chikaboom.model.database.BaseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Объект-ответ, используется для ответов в json-формате
 */
@Data
public class CustomResponseObject implements BaseEntity {
    private Timestamp timestamp;
    private int status;
    private String message;
    private String path;

    public CustomResponseObject(int status, String message, String path) {
        this.status = status;
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
        this.message = message;
        this.path = path;
    }
}
