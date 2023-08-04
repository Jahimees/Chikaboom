package net.chikaboom.model.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы Service в базе данных
 */
@Data
@Entity
@Table(name = SERVICE)
public class Service implements BaseEntity {

    /**
     * id сущности в таблице service
     */
    @Id
    @Column(name = ID_SERVICE)
    private int idService;

    /**
     * Наименование услуги
     */
    @Column(name = SERVICE_NAME)
    private String serviceName;

}
