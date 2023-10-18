package net.chikaboom.facade.service;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.AccountFacadeConverter;
import net.chikaboom.facade.converter.UserFileFacadeConverter;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.UserFileFacade;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.UserFile;
import net.chikaboom.service.data.UserFileDataService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserFileFacadeService {

    private final UserFileDataService userFileDataService;

    public UserFileFacade findById(int id) {
        Optional<UserFile> userFileOptional = userFileDataService.findById(id);

        if (!userFileOptional.isPresent()) {
            throw new NotFoundException("There not found file with id " + id);
        }

        return UserFileFacadeConverter.convertToDto(userFileOptional.get());
    }

    public List<UserFileFacade> findAll() {
        return userFileDataService.findAll()
                .stream().map(UserFileFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    public List<UserFileFacade> findAllByAccount(AccountFacade accountFacade) {
        Account accountModel = AccountFacadeConverter.convertToModel(accountFacade);

        return userFileDataService.findAllByAccount(accountModel)
                .stream().map(UserFileFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    public void deleteById(int id) {
        userFileDataService.deleteById(id);
    }

    public UserFileFacade update(UserFileFacade userFileFacade) {

        UserFile userFileModel = UserFileFacadeConverter.convertToModel(userFileFacade);

        return UserFileFacadeConverter.convertToDto(userFileDataService.update(userFileModel));
    }

    public UserFileFacade create(UserFileFacade userFileFacade) {

        UserFile userFileModel = UserFileFacadeConverter.convertToModel(userFileFacade);

        return UserFileFacadeConverter.convertToDto(
                userFileDataService.create(userFileModel));
    }
}
