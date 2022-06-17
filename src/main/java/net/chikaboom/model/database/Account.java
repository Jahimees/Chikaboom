package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

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
    private int idAccount;

    /**
     * Номер телефона владельца аккаунта
     */
    @Column(name = PHONE)
    private String phone;

    /**
     * Пароль от аккаунта
     */
    @Column(name = PASSWORD)
    private String password;

    /**
     * Соль для пароля
     */
    @Column(name = SALT)
    private String salt;

    /**
     * Имя пользователя
     */
    @Column(name = NICKNAME)
    private String nickname;

    /**
     * Дата регистрации аккаунта
     */
    @Column(name = REGISTRATION_DATE)
    private Timestamp registrationDate;

    @Column(name = ID_ROLE)
    private int idRole;

}
