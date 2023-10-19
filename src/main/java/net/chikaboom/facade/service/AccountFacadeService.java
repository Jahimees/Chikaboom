package net.chikaboom.facade.service;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.AccountFacadeConverter;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.CustomPrincipal;
import net.chikaboom.service.data.AccountDataService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Пример того, как это ДОЛЖНО быть. Controller(Facade) Facade(Service, Converter) Service(Repository)
 */
@Service
@RequiredArgsConstructor
public class AccountFacadeService {

    private final AccountDataService accountDataService;

    @Transactional
    public AccountFacade findById(int idAccount) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("There not found account with id " + idAccount);
        }

        return convertToDto(idAccount, accountOptional.get());
    }

    @Transactional
    public List<AccountFacade> findAll() {
        return accountDataService.findAll()
                .stream().map(AccountFacadeConverter::toDtoForNotAccountUser).collect(Collectors.toList());
    }

    @Transactional
    public AccountFacade create(AccountFacade accountFacade) {

        Account account = accountDataService.create(AccountFacadeConverter.convertToModel(accountFacade));

        return AccountFacadeConverter.toDtoForAccountUser(account);
    }

    private AccountFacade convertToDto(int idAccount, Account account) {
        CustomPrincipal customPrincipal;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String) {
            customPrincipal = new CustomPrincipal(0, 0);
        } else {
            customPrincipal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        return customPrincipal.getIdAccount() == idAccount ? AccountFacadeConverter
                .toDtoForAccountUser(account) : AccountFacadeConverter.toDtoForNotAccountUser(account);
    }
}
