package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы WorkingDay в базе данных
 */
@Data
@Entity
@Table(name = WORKING_DAY)
public class WorkingDay implements BaseEntity {

    public WorkingDay() {

    }

    public WorkingDay(Timestamp workingDayStart, Timestamp workingDayEnd) {
        this.workingDayStart = workingDayStart;
        this.workingDayEnd = workingDayEnd;
    }

    /**
     * id сущности в таблице workingDay
     */
    @Id
    @Column(name = ID_WORKING_DAY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idWorkingDay;

    /**
     * Ссылка на аккаунт, кому принадлежит рабочий день
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_ACCOUNT)
    private Account account;

    /**
     * Дата конкретного рабочего дня
     */
    @Column(name = DATE)
    private Timestamp date;

    /**
     * Начало рабочего дня мастера
     */
    @Column(name = WORKING_DAY_START)
    private Timestamp workingDayStart;

    /**
     * Конец рабочего дня мастера
     */
    @Column(name = WORKING_DAY_END)
    private Timestamp workingDayEnd;
}
