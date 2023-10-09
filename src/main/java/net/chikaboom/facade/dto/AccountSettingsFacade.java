package net.chikaboom.facade.dto;

import lombok.Data;

import java.sql.Time;

@Data
public class AccountSettingsFacade implements Facade {

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
