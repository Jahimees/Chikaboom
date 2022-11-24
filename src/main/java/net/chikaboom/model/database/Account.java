package net.chikaboom.model.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы Account в базе данных
 */
@Data
@Entity
@Table(name = ACCOUNT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements BaseEntity {
    /**
     * id аккаунта
     */
    @Id
    @Column(name = ID_ACCOUNT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    /**
     * Электронная почта пользователя
     */
    @Column(name = EMAIL)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_ROLE)
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_PHONE_CODE)
    private PhoneCode phoneCode;

    @Column(name = ADDRESS)
    private String address;

    @OneToOne
    @JoinColumn(name = ID_ABOUT)
    private About about;

    @OneToOne
    @JoinColumn(name = ID_SOCIAL_NETWORK)
    private SocialNetwork socialNetwork;

}
