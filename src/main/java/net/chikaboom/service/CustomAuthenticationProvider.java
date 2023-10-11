package net.chikaboom.service;

import com.google.i18n.phonenumbers.NumberParseException;
import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.AccountFacadeConverter;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.UserDetailsFacade;
import net.chikaboom.model.database.CustomPrincipal;
import net.chikaboom.service.data.AccountDataService;
import net.chikaboom.service.data.UserDetailsDataService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Собственная реализация аутентификации пользователя
 */
@Service
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AccountDataService accountDataService;
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

        UserDetailsFacade userDetailsFacade;
        try {
            userDetailsFacade = userDetailsDataService.findUserDetailsByPhone(phoneDetails[0], phoneDetails[1]);
        } catch (NumberParseException e) {
            throw new IllegalArgumentException("Cannot find user details. Phone is incorrect");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Unknown user with phone " + phone);
        }
        AccountFacade accountFacade;
        try {
            accountFacade = accountDataService.findAccountByUserDetails(userDetailsFacade);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Unknown user with phone " + phone, e);
        }

        if (!bCryptPasswordEncoder.matches(password, accountFacade.getPassword())) {
            throw new BadCredentialsException("Bad password");
        }

        CustomPrincipal principal = new CustomPrincipal(accountFacade.getIdAccount(),
                accountFacade.getUserDetailsFacade().getIdUserDetails());

        return new UsernamePasswordAuthenticationToken(
                principal, password, AccountFacadeConverter.convertToModel(accountFacade).getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
