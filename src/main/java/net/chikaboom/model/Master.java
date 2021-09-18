package net.chikaboom.model;

import java.util.UUID;

/**
 * Определяет модель таблицы Master в базе данных
 */
public class Master implements Entity {
    private String idMaster;
    private String idAccount;
    private String address;
    private String description;

    public Master() {
        idMaster = UUID.randomUUID().toString();
    }

    public String getIdMaster() {
        return idMaster;
    }

    public void setIdMaster(String idMaster) {
        this.idMaster = idMaster;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
