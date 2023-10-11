package net.chikaboom.facade.dto;

import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * DOCS {@link Facade}
 */
@Data
public class WorkingDayFacade implements Facade {

    /**
     * id сущности в таблице workingDay
     */
    private int idWorkingDay;

    /**
     * Ссылка на аккаунт, кому принадлежит рабочий день
     */
    private AccountFacade accountFacade;

    /**
     * Дата конкретного рабочего дня
     */
    private Timestamp date;

    /**
     * Начало рабочего дня мастера
     */
    private Time workingDayStart;

    /**
     * Конец рабочего дня мастера
     */
    private Time workingDayEnd;
}
