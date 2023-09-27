package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы account_settings в базе данных
 */
@Entity
@Data
@Table(name = ACCOUNT_SETTINGS)
public class AccountSettings {

    public AccountSettings() {}

    public AccountSettings(Time defaultWorkingDayStart, Time defaultWorkingDayEnd) {
        this.defaultWorkingDayStart = defaultWorkingDayStart;
        this.defaultWorkingDayEnd = defaultWorkingDayEnd;
    }

    /**
     * Идентификатор настроек аккаунта
     */
    @Id
    @Column(name = ID_ACCOUNT_SETTINGS)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAccountSettings;

    /**
     * Настройка видимости телефона в профиле
     */
    @Column(name = IS_PHONE_VISIBLE)
    private boolean isPhoneVisible;

    /**
     * Настройка начала рабочего дня по умолчанию
     */
    @Column(name = DEFAULT_WORKING_DAY_START)
    private Time defaultWorkingDayStart;

    /**
     * Настройка конца рабочего дня по умолчанию
     */
    @Column(name = DEFAULT_WORKING_DAY_END)
    private Time defaultWorkingDayEnd;
}
