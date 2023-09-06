package net.chikaboom.service;

import com.google.i18n.phonenumbers.NumberParseException;
import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.CustomPrincipal;
import net.chikaboom.model.database.UserDetails;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.service.data.UserDetailsDataService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Собственная реализация аутентификации пользователя
 */
@Service
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AccountRepository accountRepository;
    private final UserDetailsDataService userDetailsDataService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Производит аутентификацию пользователя. Сверяет имя пользователя, а также хэшированные пароли
     *
     * @param authentication аутентификация из {@link org.springframework.security.core.context.SecurityContextHolder}
     * @return объект аутентификации с данными аутентифицированного пользователя
     * @throws AuthenticationException возникает при неверно введенных данных
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phone = authentication.getName();
        String[] phoneDetails = phone.split("_");
        String password = authentication.getCredentials().toString();

        Optional<UserDetails> userDetailsOptional;
        try {
            userDetailsOptional = userDetailsDataService.findUserDetailsByPhone(phoneDetails[0], phoneDetails[1]);
            if (!userDetailsOptional.isPresent()) {
                throw new BadCredentialsException("Unknown user with phone " + phone);
            }
        } catch (NumberParseException e) {
            throw new IllegalArgumentException("Cannot find user details. Phone is incorrect");
        }

        Optional<Account> accountOptional = accountRepository.findAccountByUserDetails(userDetailsOptional.get());

        if (!accountOptional.isPresent()) {
            throw new BadCredentialsException("Unknown user with phone " + phone);
        }

        Account account = accountOptional.get();


        if (!bCryptPasswordEncoder.matches(password, account.getPassword())) {
            throw new BadCredentialsException("Bad password");
        }

        CustomPrincipal principal = new CustomPrincipal(account.getIdAccount(), account.getUserDetails().getIdUserDetails());

        return new UsernamePasswordAuthenticationToken(
                principal, password, account.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
