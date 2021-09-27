package net.chikaboom.model;

import java.util.UUID;

/**
 * Определяет модель таблицы Comment в базе данных
 */
public class Comment implements Entity {
    //    TODO DOCUMENTATION
    private String idComment;
    private String idMaster;
    private String idClient;
    private String message;
    private boolean isClientMessage;
    private int rate;

    public Comment() {
        idComment = UUID.randomUUID().toString();
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getIdMaster() {
        return idMaster;
    }

    public void setIdMaster(String idMaster) {
        this.idMaster = idMaster;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isClientMessage() {
        return isClientMessage;
    }

    public void setClientMessage(boolean clientMessage) {
        isClientMessage = clientMessage;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
