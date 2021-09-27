package net.chikaboom.model;

import java.util.UUID;

/**
 * Определяет модель таблицы Work в базе данных
 */
public class Work implements Entity {
    //    TODO DOCUMENTATION
    private String idWork;
    private String idMaster;
    private byte[] image;
    private String comment;

    public Work() {
        idWork = UUID.randomUUID().toString();
    }

    public String getIdWork() {
        return idWork;
    }

    public void setIdWork(String idWork) {
        this.idWork = idWork;
    }

    public String getIdMaster() {
        return idMaster;
    }

    public void setIdMaster(String idMaster) {
        this.idMaster = idMaster;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
