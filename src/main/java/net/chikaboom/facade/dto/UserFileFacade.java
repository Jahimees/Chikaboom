package net.chikaboom.facade.dto;

import lombok.Data;

/**
 * DOCS {@link Facade}
 */
@Data
public class UserFileFacade implements Facade {

    /**
     * Идентификатор пользовательского файла
     */
    private int idUserFile;

    /**
     * Владелец файла
     */
    private AccountFacade accountFacade;

    /**
     * Путь и имя файла
     */
    private String filePath;
}
