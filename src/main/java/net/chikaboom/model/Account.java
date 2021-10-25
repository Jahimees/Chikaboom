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
    /**
     * id аккаунта
     */
    private String idAccount;

    /**
     * Имя владельца аккаунта
     */
    private String name;

    /**
     * Фамилия владельца аккаунта
     */
    private String surname;

    /**
     * Электронная почта владельца. Также является логином
     */
    private String email;

    /**
     * Пароль от аккаунта
     */
    private String password;

    /**
     * Номер телефона владельца аккаунта
     */
    private String phone;

    /**
     * Дата регистрации аккаунта
     */
    private Timestamp registrationDate;

    public Account() {
        idAccount = UUID.randomUUID().toString();
        registrationDate = Timestamp.valueOf(LocalDateTime.now()); //TODO Убрать и перенести в регистрацию
    }
}
