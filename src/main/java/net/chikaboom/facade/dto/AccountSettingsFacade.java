package net.chikaboom.facade.dto;

import lombok.Data;

import java.sql.Time;

/**
 * DOCS {@link Facade}
 */
@Data
public class AccountSettingsFacade implements Facade {

    public AccountSettingsFacade() {}

    public AccountSettingsFacade(Time defaultWorkingDayStart, Time defaultWorkingDayEnd) {
        this.defaultWorkingDayStart = defaultWorkingDayStart;
        this.defaultWorkingDayEnd = defaultWorkingDayEnd;
    }

    /**
     * Идентификатор настроек аккаунта
     */
    private int idAccountSettings;

    /**
     * Настройка видимости телефона в профиле
     */
    private boolean isPhoneVisible;

    /**
     * Настройка начала рабочего дня по умолчанию
     */
    private Time defaultWorkingDayStart;

    /**
     * Настройка конца рабочего дня по умолчанию
     */
    private Time defaultWorkingDayEnd;
}
