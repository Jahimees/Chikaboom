package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.sql.Timestamp;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы account_status в базе данных
 */
@Data
@Entity
@Table(name = ACCOUNT_STATUS)
public class AccountStatus {
    /**
     * id сущности account_status
     */
    @Id
    @Column(name = ID_ACCOUNT_STATUS)
    private int idAccountStatus;

    /**
     * Дата, с какого времени действует настоящий статус
     */
    @Column(name = SINCE_DATE)
    private Timestamp sinceDate;

    /**
     * Дата, до какого времени действует настоящий статус
     */
    @Column(name = TO_DATE)
    private Timestamp toDate;

    /**
     * Внешний ключ к account. Определяет к какому аккаунту относится статус
     */
    @Column(name = ID_ACCOUNT)
    private int idAccount;

    /**
     * Внешний ключ к status. Определяет конкретный тип статуса
     */
    @Column(name = ID_STATUS)
    private int idStatus;
}