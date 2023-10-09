package net.chikaboom.facade.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AccountStatusFacade implements Facade {

    /**
     * id сущности account_status
     */
    private int idAccountStatus;

    /**
     * Дата, с какого времени действует настоящий статус
     */
    private Timestamp sinceDate;

    /**
     * Дата, до какого времени действует настоящий статус
     */
    private Timestamp toDate;

    /**
     * Ссылка на аккаунт
     */
    private AccountFacade accountFacade;

    /**
     * Внешний ключ к status. Определяет конкретный тип статуса
     */
    private StatusFacade statusFacade;
}
