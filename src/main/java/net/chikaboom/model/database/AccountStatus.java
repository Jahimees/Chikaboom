package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы account_status в базе данных
 */
@Data
@Entity
@Table(name = ACCOUNT_STATUS)
public class AccountStatus implements BaseEntity {

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
     * Ссылка на аккаунт
     */
    @OneToOne
    @JoinColumn(name = ID_ACCOUNT)
    private Account account;

    /**
     * Внешний ключ к status. Определяет конкретный тип статуса
     */
    @ManyToOne
    @JoinColumn(name = ID_STATUS)
    private Status status;
}
