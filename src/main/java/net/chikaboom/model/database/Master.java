package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

import static net.chikaboom.util.constant.DbNamesConstant.MASTER;

/**
 * Определяет модель таблицы Master в базе данных
 */
@Data
@Entity
@Table(name = MASTER)
public class Master implements BaseEntity {
    /**
     * Id мастера
     */
    @Id
    private String idMaster;

    /**
     * Id соответствующего аккаунта. Внешний ключ
     */
    private String idAccount;

    /**
     * Адрес мастера, где он работает
     */
    private String address;

    /**
     * Краткое описание мастера о себе
     */
    private String about;

    public Master() {
        idMaster = UUID.randomUUID().toString();
    }
}
