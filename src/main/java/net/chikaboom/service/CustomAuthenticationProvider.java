package net.chikaboom.service;

import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public CustomAuthenticationProvider(AccountRepository accountRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String nickname = authentication.getName();
        String password = authentication.getCredentials().toString();

        Account account = accountRepository.findAccountByNickname(nickname);

        if (account == null) {
            throw new BadCredentialsException("Unknown user " + nickname);
        }

        if (!bCryptPasswordEncoder.matches(password, account.getPassword())) {
            throw new BadCredentialsException("Bad password");
        }

        return new UsernamePasswordAuthenticationToken(
                account, password, account.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
