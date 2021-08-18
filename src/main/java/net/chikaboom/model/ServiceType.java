package net.chikaboom.model;

/**
 * Определяет модель таблицы ServiceType в базе данных
 */
public class ServiceType {
    private String idServiceType;
    private String name;

    public String getIdServiceType() {
        return idServiceType;
    }

    public void setIdServiceType(String idServiceType) {
        this.idServiceType = idServiceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
