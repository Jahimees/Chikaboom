package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы Subservice в базе данных
 */
@Data
@Entity
@Table(name = SUBSERVICE)
public class Subservice implements BaseEntity {

    /**
     * id сущности в табилце subservice
     */
    @Id
    @Column(name = ID_SUBSERVICE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSubservice;

    /**
     * Наименование подуслуги
     */
    @Column(name = SUBSERVICE_NAME)
    private String subserviceName;

    /**
     * Родительский тип услуги для данной подуслуги
     */
    @ManyToOne
    @JoinColumn(name = ID_SERVICE)
    private Service service;

}
