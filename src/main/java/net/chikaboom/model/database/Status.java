package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static net.chikaboom.util.constant.DbNamesConstant.ID_STATUS;
import static net.chikaboom.util.constant.DbNamesConstant.STATUS;

/**
 * Определяет модель таблицы Status в базе данных
 */
@Data
@Entity
@Table(name = STATUS)
public class Status {
    /**
     * id сущности в таблице Status
     */
    @Id
    @Column(name = ID_STATUS)
    private int idStatus;

    /**
     * Имя статуса
     */
    @Column(name = STATUS)
    private String status;
}
