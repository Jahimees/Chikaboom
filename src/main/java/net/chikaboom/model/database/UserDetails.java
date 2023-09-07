package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы user_details в базе данных
 */
@Data
@Table(name = USER_DETAILS)
@Entity
public class UserDetails implements BaseEntity {

    private static final long serialVersionUID = 150570965476655532l;

    /**
     * Идентификатор пользовательских данных
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID_USER_DETAILS)
    private int idUserDetails;

    /**
     * Владелец пользовательских данных.
     * Может быть пустым, если данные являются расширением {@link Account} (например просто данные о мастере).
     * При создании мастером клиентов (сущность userDetails без привязки к аккаунту), заполняется это поле тем мастером,
     * кто его создал
     */
    @OneToOne
    @JoinColumn(name = ID_ACCOUNT_OWNER)
    private Account masterOwner;

    /**
     * Общедоступная информация о пользователе
     */
    @OneToOne
    @JoinColumn(name = ID_ABOUT)
    private About about;

    /**
     * Ссылки на социальные сети пользователя
     */
    @OneToOne
    @JoinColumn(name = ID_SOCIAL_NETWORK)
    private SocialNetwork socialNetwork;

    /**
     * Код телефона пользователя
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_PHONE_CODE)
    private PhoneCode phoneCode;

    /**
     * Номер телефона владельца аккаунта
     */
    @Column(name = PHONE)
    private String phone;

    /**
     * Отображаемый номер телефона
     */
    @Column(name = DISPLAYED_PHONE)
    private String displayedPhone;

    /**
     * Имя человека
     */
    @Column(name = FIRST_NAME)
    private String firstName;

    /**
     * Фамилия человека
     */
    @Column(name = LAST_NAME)
    private String lastName;

    /**
     * Дата последнего визита к мастеру (не хранится в базе)
     */
    @Transient
    private Timestamp lastVisitDate;

    /**
     * Количество визитов к определенному мастеру (не хранится в базе)
     */
    @Transient
    private int visitCount;

}
