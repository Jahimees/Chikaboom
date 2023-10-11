package net.chikaboom.facade.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

/**
 * DOCS {@link Facade}
 */
@Data
public class AccountFacade implements Facade {

    /**
     * id аккаунта
     */
    private int idAccount;

    /**
     * Пароль от аккаунта
     */
    private String password;

    private String oldPassword;

    /**
     * Имя пользователя
     */
    private String username;

    /**
     * Дата регистрации аккаунта
     */
    private Timestamp registrationDate;

    /**
     * Электронная почта пользователя
     */
    private String email;

    /**
     * Содержит все роли пользователя
     */
    private Set<RoleFacade> rolesFacade;

    /**
     * Адрес пользователя
     */
    private String address;

    /**
     * Содержит подробную информацию об аккаунте
     */
    private UserDetailsFacade userDetailsFacade;

    /**
     * Содержит настройки аккаунта
     */
    private AccountSettingsFacade accountSettingsFacade;
}
