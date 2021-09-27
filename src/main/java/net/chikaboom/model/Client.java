package net.chikaboom.model;

import java.util.UUID;

/**
 * Определяет модель таблицы Client в базе данных
 */
public class Client implements Entity {
    //    TODO DOCUMENTATION
    private String idClient;
    private String idAccount;

    public Client() {
        idClient = UUID.randomUUID().toString();
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }
}
