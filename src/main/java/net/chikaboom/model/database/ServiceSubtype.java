package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы ServiceSubtype в базе данных
 */
@Data
@Entity
@Table(name = SERVICE_SUBTYPE)
public class ServiceSubtype implements BaseEntity {

    /**
     * id сущности в табилце serviceSubtype
     */
    @Id
    @Column(name = ID_SERVICE_SUBTYPE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idServiceSubtype;

    /**
     * Наименование подуслуги
     */
    @Column(name = NAME)
    private String name;

    /**
     * Родительский тип услуги для данной подуслуги
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ID_SERVICE_TYPE)
    private ServiceType serviceType;

}
