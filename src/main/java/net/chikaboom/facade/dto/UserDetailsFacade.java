package net.chikaboom.facade.dto;

import lombok.Data;
import net.chikaboom.model.database.Account;

import java.sql.Timestamp;

/**
 * DOCS {@link Facade}
 */
@Data
public class UserDetailsFacade implements Facade {

    /**
     * Идентификатор пользовательских данных
     */
    private int idUserDetails;

    /**
     * Владелец пользовательских данных.
     * Может быть пустым, если данные являются расширением {@link Account} (например просто данные о мастере).
     * При создании мастером клиентов (сущность userDetails без привязки к аккаунту), заполняется это поле тем мастером,
     * кто его создал
     */
    private AccountFacade masterOwnerFacade;

    /**
     * Общедоступная информация о пользователе
     */
    private AboutFacade aboutFacade;

    /**
     * Ссылки на социальные сети пользователя
     */
    private SocialNetworkFacade socialNetworkFacade;

    /**
     * Код телефона пользователя
     */
    private PhoneCodeFacade phoneCodeFacade;

    /**
     * Номер телефона владельца аккаунта
     */
    private String phone;

    /**
     * Отображаемый номер телефона
     */
    private String displayedPhone;

    /**
     * Имя человека
     */
    private String firstName;

    /**
     * Фамилия человека
     */
    private String lastName;

    /**
     * Дата первого визита к мастеру (не хранится в базе)
     */
    private Timestamp firstVisitDate;

    /**
     * Дата последнего визита к мастеру (не хранится в базе)
     */
    private Timestamp lastVisitDate;

    /**
     * Количество визитов к определенному мастеру (не хранится в базе)
     */
    private int visitCount;
}
