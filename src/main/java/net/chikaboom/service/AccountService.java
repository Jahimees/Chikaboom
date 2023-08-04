package net.chikaboom.service;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Role;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountRepository accountRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByNickname(username);

        if (account == null) {
            throw new UsernameNotFoundException("Account not found");
        }

        return account;
    }

    public boolean saveAccount(Account account) {
        Account accountFromDb = accountRepository.findAccountByNickname(account.getNickname());

        if (accountFromDb != null) {
            return false;
        }

        account.setRoles(Collections.singleton(new Role(1))); //TODO FIXME NEW hardcode
//        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);

        return true;
    }
}
