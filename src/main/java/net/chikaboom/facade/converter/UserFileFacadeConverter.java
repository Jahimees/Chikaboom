package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.UserFileFacade;
import net.chikaboom.model.database.UserFile;

public final class UserFileFacadeConverter {

    private UserFileFacadeConverter() {}

    public static UserFileFacade convertToDto(UserFile model) {
        UserFileFacade userFileFacade = new UserFileFacade();

        userFileFacade.setIdUserFile(model.getIdUserFile());
        userFileFacade.setAccountFacade(AccountFacadeConverter.toDtoOnlyId(model.getAccount()));
        userFileFacade.setFilePath(model.getFilePath());

        return userFileFacade;
    }

    public static UserFile convertToModel(UserFileFacade dto) {
        UserFile userFile = new UserFile();

        userFile.setIdUserFile(dto.getIdUserFile());
        userFile.setAccount(AccountFacadeConverter.convertToModel(dto.getAccountFacade()));
        userFile.setFilePath(dto.getFilePath());

        return userFile;
    }
}
