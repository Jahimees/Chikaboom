package net.chikaboom.model.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.ID_STATUS;
import static net.chikaboom.util.constant.DbNamesConstant.STATUS;

/**
 * Определяет модель таблицы Status в базе данных
 */
@Data
@Entity
@Table(name = STATUS)
public class Status implements BaseEntity {

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
