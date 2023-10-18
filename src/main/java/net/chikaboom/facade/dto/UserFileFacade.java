package net.chikaboom.facade.dto;

import lombok.Data;

@Data
public class UserFileFacade implements Facade {

    private int idUserFile;
    private AccountFacade accountFacade;
    private String filePath;

}
