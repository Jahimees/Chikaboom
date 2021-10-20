package net.chikaboom.model;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Определяет модель таблицы Account в базе данных
 */
@Data
public class Account implements Entity {
    //    TODO DOCUMENTATION
    private String idAccount;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    private Timestamp registrationDate;

    public Account() {
        idAccount = UUID.randomUUID().toString();
        registrationDate = Timestamp.valueOf(LocalDateTime.now()); //TODO Убрать и перенести в регистрацию
    }
}
