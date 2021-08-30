package net.chikaboom.model;

/**
 * Определяет модель таблицы Client в базе данных
 */
public class Client implements Entity {
    private String idClient;
    private String idAccount;

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
