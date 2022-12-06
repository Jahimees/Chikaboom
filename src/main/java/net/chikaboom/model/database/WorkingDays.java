package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.*;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы WorkingDays в базе данных
 */
@Data
@Entity
@Table(name = WORKING_DAYS)
public class WorkingDays implements BaseEntity {

    public WorkingDays() {

    }

    public WorkingDays(int workingDayStart, int workingDayEnd) {
        this.workingDayStart = workingDayStart;
        this.workingDayEnd = workingDayEnd;
    }

    /**
     * id сущности в таблице workingDays
     */
    @Id
    @Column(name = ID_WORKING_DAYS)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idWorkingDays;

    /**
     * Набор дат в виде строки, отображающих рабочие дни для пользователя
     */
    @Column(name = WORKING_DAYS)
    private String workingDays;

    /**
     * Начало рабочего дня мастера
     */
    @Column(name = WORKING_DAY_START)
    private int workingDayStart;

    /**
     * Конец рабочего дня мастера
     */
    @Column(name = WORKING_DAY_END)
    private int workingDayEnd;
}
