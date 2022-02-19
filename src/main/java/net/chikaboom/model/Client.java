package net.chikaboom.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Определяет модель таблицы Client в базе данных
 */
@Data
@Entity
@Table(name = "client")
public class Client implements BaseEntity {
    /**
     * Id клиента
     */
    @Id
    private String idClient;

    /**
     * Id аккаунта. Внешний ключ
     */
    private String idAccount;

    public Client() {
        idClient = UUID.randomUUID().toString();
    }
}
