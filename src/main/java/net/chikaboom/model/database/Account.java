package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы Account в базе данных
 */
@Data
@Entity
@Table(name = ACCOUNT)
public class Account implements BaseEntity {
    /**
     * id аккаунта
     */
    @Id
    @Column(name = ID_ACCOUNT)
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
    @Column(name = REGISTRATION_DATE)
    private Timestamp registrationDate;

    private String salt;

    public Account() {
        idAccount = UUID.randomUUID().toString();
    }
}
