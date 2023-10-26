package net.chikaboom.facade.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CommentFacade implements Facade {

    private int idComment;
    private AccountFacade accountClientFacade;
    private AccountFacade accountMasterFacade;
    private boolean isGood;
    private Timestamp date;
    private String text;
}
