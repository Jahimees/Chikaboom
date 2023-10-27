package net.chikaboom.facade.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * DOCS {@link Facade}
 */
@Data
public class CommentFacade implements Facade {

    /**
     * Идентификатор комментария
     */
    private int idComment;

    /**
     * Аккаунт человека, оставившего комментарий
     */
    private AccountFacade accountClientFacade;

    /**
     * Аккаунт мастера, которому был оставлен комментарий
     */
    private AccountFacade accountMasterFacade;

    /**
     * Отражает, положительный комментарий или отрицательный
     */
    private boolean isGood;

    /**
     * Дата комментария
     */
    private Timestamp date;

    /**
     * Текст комментария
     */
    private String text;
}
