package net.chikaboom.model;

/**
 * Определяет модель таблицы Work в базе данных
 */
public class Work {
    private String idWork;
    private String idMaster;
    private byte[] image;
    private String comment;

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
